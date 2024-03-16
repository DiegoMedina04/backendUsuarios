package andres.userapp.backenduserapp.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import andres.userapp.backenduserapp.models.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByUserName(String userName);

    @Query("SELECT u FROM User u WHERE u.userName = ?1") // el User es de la entidad no de la tabla
    Optional<User> getUserByUsername(String userName);

}
