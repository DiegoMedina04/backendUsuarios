
package andres.userapp.backenduserapp.auth.filters;

import java.io.IOException;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import andres.userapp.backenduserapp.auth.TokenJwtConfig;
import andres.userapp.backenduserapp.models.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * JwtAuthenticationFilter
 */
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    // JwtAuthenticationFilter el por debajo maneja la ruta /login, si se quiere
    // modificar la ruta login para añadirle algo mas
    // ejemplo: v1/login, hay que entrar a la clase
    // UsernamePasswordAuthenticationFilter
    // UsernamePasswordAuthenticationFilter ahi adentro esata
    // UsernamePasswordAuthenticationFilter para que soporte la autentica ApiRest

    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        // Cuando la ruta de login y metodo POST se invocara el filtro en el fondo
        // estara escuchando todos los request y cuando detecto que es login y post se
        // ejecutara attemptAuthentication y tratara de iniciar sesion
    }

    @Override
    // intentar realizar la autenticacion osea el login
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        String userName = "";
        String password = "";
        try {
            // (request.getInputStream() retorna el body de la peticion
            // User.class en la clase que los va a poblar los datos
            User user = new ObjectMapper().readValue(request.getInputStream(), User.class);

            userName = user.getUserName();
            password = user.getPassword();

            System.out.println("User name es: " + userName);

            logger.info("El username es " + userName);
            logger.info("La password es " + password);
            // despues de este paso entra a encriptar la contraseña
        } catch (StreamReadException e) {
            e.printStackTrace();
        } catch (DatabindException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userName, password);

        return this.authenticationManager.authenticate(authToken);
        // el trabaja con el JpaUserDetailsService lo ejecuta por debajo
        // aca es donde realiza la autenticacion

        // orden
        // 1. autentica ACA
        // 2. va al UserDetails
        // 3. Sino existe ira al metodo unsuccessfulAuthentication
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        // con authResult podemos tener el nombre del usuario, PILAS EL user que se
        // invoca para castear no es el de la entidad sino el de security

        String userName = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal())
                .getUsername();

        Collection<? extends GrantedAuthority> rolesUser = authResult.getAuthorities();
        Boolean isAdmin = rolesUser.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));
        // Claims claim = Jwts.claims();
        Map<String, Object> claim = new HashMap<>();
        // Claims claims = Jwts.claims();

        // convierte un objeto a una cadena JSON.
        claim.put("authorities", new ObjectMapper().writeValueAsString(rolesUser));
        claim.put("isAdmin", isAdmin);
        claim.put("userName", userName);
        // obtenemos el username hacemos el cast teniendo del getPrincipal(), generamos
        // un
        // token falso donde se le concatena el user y se codifica en base 64,
        // String orginalInput = TokenJwtConfig.SECRET_KEY + "." + userName;
        // String token = Base64.getEncoder().encodeToString(orginalInput.getBytes());
        System.out.println("Sin parsear " + rolesUser);
        System.out.println("Json: " + new ObjectMapper().writeValueAsString(rolesUser));

        String token = Jwts.builder()
                .claims(claim)
                .subject(userName)
                // .claim("company", 28)
                .signWith(TokenJwtConfig.SECRET_KEY)
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .issuedAt(new Date())
                .compact();

        response.addHeader(TokenJwtConfig.HEADER_AUTHORIZATION, TokenJwtConfig.PREFIX_TOKEN + token);
        response.addHeader("Nueva data", "clave2");

        Map<String, Object> body = new HashMap<>();
        body.put("token", token);
        body.put("message", String.format("Iniciado sesion con exito bienvenido %s", userName));
        body.put("username", userName);

        // con esto transforma el map a un JSON
        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(200);
        response.setContentType("application/json");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Error en la autenticacion username o password incorrecto!");
        body.put("error", failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);
        response.setContentType("application/json");

    }

}