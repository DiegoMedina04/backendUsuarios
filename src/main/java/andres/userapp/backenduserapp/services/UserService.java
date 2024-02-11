package andres.userapp.backenduserapp.services;

import java.util.List;
import java.util.Optional;

import andres.userapp.backenduserapp.models.entities.User;
import andres.userapp.backenduserapp.models.request.UserRequest;

public interface UserService {
     List<User> findAll();

     Optional<User> findById(Long id);

     User save(User user);

     Optional<User> update(Long id, UserRequest user);

     void remove(Long id);

}
