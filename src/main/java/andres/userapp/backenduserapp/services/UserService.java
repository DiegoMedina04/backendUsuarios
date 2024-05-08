package andres.userapp.backenduserapp.services;

import java.util.List;
import java.util.Optional;

import andres.userapp.backenduserapp.models.dto.UserDto;
import andres.userapp.backenduserapp.models.entities.User;
import andres.userapp.backenduserapp.models.request.UserRequest;

public interface UserService {
     List<UserDto> findAll();

     Optional<UserDto> findById(Long id);

     UserDto save(User user);

     Optional<UserDto> update(Long id, UserRequest user);

     void remove(Long id);

}
