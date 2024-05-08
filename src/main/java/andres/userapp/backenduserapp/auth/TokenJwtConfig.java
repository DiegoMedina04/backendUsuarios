package andres.userapp.backenduserapp.auth;

import java.security.Key;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

public class TokenJwtConfig {

    // public final static String SECRET_KEY = "aqui_ira_el_token";
    public final static SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public final static String PREFIX_TOKEN = "Bearer ";
    public final static String HEADER_AUTHORIZATION = "Authorization";

}
