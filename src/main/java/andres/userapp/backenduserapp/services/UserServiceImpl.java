package andres.userapp.backenduserapp.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import andres.userapp.backenduserapp.models.entities.User;
import andres.userapp.backenduserapp.models.request.UserRequest;
import andres.userapp.backenduserapp.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<User> findAll() {
        return (List<User>) userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return (Optional<User>) userRepository.findById(id);
    }

    @Override
    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<User> update(Long id, UserRequest user) {
        Optional<User> userOptional = this.findById(id);
        User userNullable = null;
        if (userOptional.isPresent()) {

            User userDb = userOptional.orElseThrow();

            userDb.setUserName(user.getUserName());
            userDb.setEmail(user.getEmail());

            userNullable = this.save(userDb);

        }
        return Optional.ofNullable(userNullable);
    }

}
