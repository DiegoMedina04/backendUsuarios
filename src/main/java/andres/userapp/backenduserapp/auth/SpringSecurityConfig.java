package andres.userapp.backenduserapp.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import andres.userapp.backenduserapp.auth.filters.JwtAuthenticationFilter;
import andres.userapp.backenduserapp.auth.filters.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
    PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
        // el por debajo excriptara la password que manden y la comparar en la otra
        // funcion loadUserByUsername

        // return NoOpPasswordEncoder.getInstance();
        // para que pueda funcionar sin encriptar el password toca utilizarlo y a pesar
        // de que esta desprecavido
        // es un passwordEncoder no hace nada solo es seguro para testing
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(
                (requests) -> requests
                        .requestMatchers(HttpMethod.GET, "api/v1/users")
                        .permitAll()
                        .anyRequest().authenticated()

        )

                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }
}

// @Bean
// SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// http
// .csrf()
// .disable()
// .authorizeHttpRequests()
// .requestMatchers("api/v1/users")
// .permitAll()
// .anyRequest()
// .authenticated()
// .and()
// .sessionManagement()
// .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
// .and();
// // .authenticationProvider(authenticationProvider)
// // .addFilterBefore(jwtAuthFilter,
// UsernamePasswordAuthenticationFilter.class);

// return http.build();
// }

// UNA CADENA DE FILTRO PARA LA AUTORIZACION Y AUTENTICACION
// CREA UN COMPONENTE DE CONFIGURACION, SE GUARDA EN EL CONTEXTO DE SPRING
// SIEMPRE Y CUANDO TENGA LA ANOTACION @CONFIGURACION
// @Bean
// SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception
// {

// return http.authorizeHttpRequests(
// (requests) -> requests
// .requestMatchers(HttpMethod.GET, "api/v1/users")// CUANDO INVOQUE USERS SOLO
// // ACEPTARA PETICIONES GET
// .permitAll()// SERA UNA RUTA PUBLICA NO REQUIRE AUTENTICACION PERMITIDA PARA
// // TODOS MIENTRAS
// // SEA GET SI ES OTRA SI PEDIRA AUTENTICACION
// .anyRequest().authenticated()// require autenticacion SI LAS PETICIONES SON
// // POST,PUT, DELETE
// // TODO: TIENEN QUE IR EN ESTE ORDEN PRIMERO LOS QUE NO REQUIEREN PERMISO Y
// // DESPUES LO QUE SI LO REQUIEREN

// )
// creamos la instacion y le pasamos el getAuthenticationManager
// .addFilter(new
// JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
// .csrf(config -> config.disable()) // PARA EVITAR UN EXPLOY, HAY QUE
// // DESABILITARLO PORQUE ES RESTFULL
// .sessionManagement(management ->
// management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
// // control del la sesion http todo se guarda en la sesion, eso queda guardado
// // en
// // la sesion http
// // STATELESS
// .build();

// }