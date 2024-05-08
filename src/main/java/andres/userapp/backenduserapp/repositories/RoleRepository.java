package andres.userapp.backenduserapp.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import andres.userapp.backenduserapp.models.entities.Role;


public interface RoleRepository extends CrudRepository<Role, Long> {

    Optional<Role> findByName(String name);


}
