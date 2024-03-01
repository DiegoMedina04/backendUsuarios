package andres.userapp.backenduserapp.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {
    //deveulve una cadena de filtro con todas las reglas de configuracion, autorizacion

    // @Bean //crea un componente spring de configuracion
    // SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    //     return http.authorizeHttpRequests()
    //             // auth -> auth
    //             .requestMatchers(HttpMethod.GET, "/users")
    //         
    //             .permitAll()
    //             .anyRequest()
    //             .authenticated()
    //             .csrf(config -> config.disable())
    //             .sessionManagement(management -> management.sessionCreationPolicy(sessionCreationPolicy.STATELESS))
    //             .build();
    // }
    
        //UNA CADENA DE FILTRO PARA LA AUTORIZACION Y AUTENTICACION
     @Bean//CREA UN COMPONENTE DE CONFIGURACION, SE GUARDA EN EL CONTEXTO DE SPRING SIEMPRE Y CUANDO TENGA LA ANOTACION @CONFIGURACION 
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests( 
            (requests) -> requests
            .anyRequest().authenticated()//require autenticacion
            .requestMatchers(HttpMethod.GET, "/users")//CUANDO INVOQUE USERS 
            .permitAll()//PERMITIR A TODOS, NO REQUIRE AUTENTICACION SIEMPRE Y CUANDO SEAN GET
            
        )
            .csrf(config -> config.disable())
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //control del la sesion http todo se guarda en la sesion, eso queda guardada en la sesion http
            //STATELESS
            .build();
     }
     
}

 // .requestMatchers(HttpMethod.GET, "/users")
    // .permitAll()
    // .anyRequest().authenticated();
        //no permite auth esta ruta 