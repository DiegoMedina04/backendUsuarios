package andres.userapp.backenduserapp.auth.filters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import andres.userapp.backenduserapp.auth.TokenJwtConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtValidationFilter extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);

    }

    // esta funcion ya es para cuando se vaya a utilizar los endpoints que si
    // necesitan la autenticacion
    // es un metodo protegido
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // obtener las cabeceras
        String header = request.getHeader(TokenJwtConfig.HEADER_AUTHORIZATION);
        if (header == null || !header.startsWith(TokenJwtConfig.PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            // seguimos con la cadena de los filtros pasando la request y el responde y
            // SALIMOS del filtro actual
            return;
        }
        String token = header.replace(TokenJwtConfig.PREFIX_TOKEN, "");
        byte[] tokenDecodeByte = Base64.getDecoder().decode(token);
        String tokenDecode = new String(tokenDecodeByte);
        String[] tokenArr = tokenDecode.split("\\.");
        String secret = tokenArr[0];
        String userName = tokenArr[1];
        if (!TokenJwtConfig.SECRET_KEY.equals(secret)) {
            System.out.println("Token no valido!!!");
            Map<String, String> body = new HashMap<>();
            body.put("message", "el token no es valido");
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(403);
            response.setContentType("application/json");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // autenticamos aca
        // el null es el password o las credenciales seria null, el passwrod es para
        // generarlo no para validarlo
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userName, null,
                authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        // con esto nos autenticamos y dejamos pasar al usuario a este recuros protegido
        // con el token

        // para decirle que siga con el request
        chain.doFilter(request, response);
    }

}
