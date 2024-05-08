
package andres.userapp.backenduserapp.models.dto.mapper;



import andres.userapp.backenduserapp.models.dto.UserDto;
import andres.userapp.backenduserapp.models.entities.User;

/**
 * DtoMapperUser
 */
public class DtoMapperUser {

    public User user;

    private DtoMapperUser() {

    }
    
    public static DtoMapperUser builder() {
        return  new DtoMapperUser();
        
    }

    public DtoMapperUser setUser(User user) {
        this.user = user;
        return this;
    }

    public UserDto build() {

        if (user == null) {
            throw new RuntimeException("Debe pasar la entidad user!");
        }
        return new UserDto(user.getId(), user.getUserName(), user.getEmail());
    }
    
}