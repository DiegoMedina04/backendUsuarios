package andres.userapp.backenduserapp.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * SimpleGrantedAuthorityJsonCreator
 */
public abstract class SimpleGrantedAuthorityJsonCreator {
    
    @JsonCreator
    public  SimpleGrantedAuthorityJsonCreator( @JsonProperty("authority") String role){

    }
}