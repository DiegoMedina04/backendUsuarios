package andres.userapp.backenduserapp.repositories;

import org.springframework.data.repository.CrudRepository;

import andres.userapp.backenduserapp.models.entities.User;

public interface UserRepository  extends CrudRepository<User, Long>{
    
}
