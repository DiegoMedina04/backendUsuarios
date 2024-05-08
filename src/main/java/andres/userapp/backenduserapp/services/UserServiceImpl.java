package andres.userapp.backenduserapp.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import andres.userapp.backenduserapp.models.dto.UserDto;
import andres.userapp.backenduserapp.models.dto.mapper.DtoMapperUser;
import andres.userapp.backenduserapp.models.entities.Role;
import andres.userapp.backenduserapp.models.entities.User;
import andres.userapp.backenduserapp.models.request.UserRequest;
import andres.userapp.backenduserapp.repositories.RoleRepository;
import andres.userapp.backenduserapp.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
         
        return users.stream().map(
                user -> 
                DtoMapperUser.builder().setUser(user).build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map( user ->
            DtoMapperUser.builder().setUser(user).build()
         );
         
    }

    @Override
    @Transactional
    public UserDto save(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            Optional<User> userOptinal = userRepository.findByUserName(user.getUserName());
            if (userOptinal.isPresent()) {
                throw new Error("username ya existen");
            }

            Optional<Role> findRole = roleRepository.findByName("ROLE_USER");
            List<Role> role = new ArrayList<>();
            
            if(findRole.isPresent()){
                role.add(findRole.orElseThrow());
            }
            user.setRoles(role);
            User userCreated = userRepository.save(user);

            return DtoMapperUser.builder().setUser(userCreated).build();
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<UserDto> update(Long id, UserRequest user) {
        Optional<User> userOptional = userRepository.findById(id);
        User userNullable = null;
        if (userOptional.isPresent()) {

            User userDb = userOptional.orElseThrow();

            userDb.setUserName(user.getUserName());
            userDb.setEmail(user.getEmail());

            userNullable = userRepository.save(userDb);

        }
        return Optional.ofNullable(DtoMapperUser.builder().setUser(userNullable).build());
    }

}
