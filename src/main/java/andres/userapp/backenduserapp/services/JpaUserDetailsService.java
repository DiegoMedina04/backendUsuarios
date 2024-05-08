package andres.userapp.backenduserapp.services;


import java.util.List;
import java.util.Optional;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import andres.userapp.backenduserapp.repositories.UserRepository;

// JpaUserDetailsService ESTA CLASE TRABAJA POR DEBAJO COMO TAL NO LO VEMOS
@Service // <---------------------------------
public class JpaUserDetailsService implements UserDetailsService {

    @Autowired

    UserRepository userRepository;

    @Override
    @Transactional(readOnly = true) // TODO: ACA REALIZA EL LOGIN
    // la validacion de la contraseña se hace indirectamente
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<andres.userapp.backenduserapp.models.entities.User> userOptional = userRepository
                .findByUserName(username);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException(String.format("El username no existe en el sistema %s", username));
        }

        andres.userapp.backenduserapp.models.entities.User user = userOptional.orElseThrow();

    
        List<GrantedAuthority> authorities =    
                user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    
        // Forma Vieja
        // EN CASO DE EXISTA LO AUTENTICAMOS
        // List<GrantedAuthority> authorities = new ArrayList<>();
        // authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // TODOS LOS ROLES EMPIEZAN CON EL PREFIJO Y EN MAYUSCULA ROLE EJEMPLO EL ROL ES
        // USUARIO (ROLE_USER)

        // $2a$10$DOMDxjYyfZ/e7RcBfUpzqeaCs8pLgcizuiQWXPkU35nOhZlFcE9MS
        return new User(user.getUserName(), user.getPassword(), true, true,
                true,
                true, authorities);
        // 1. true significa que el usuario esta habilitado
        // 2. la cuenta no expira
        // 3. que las credenciales no expiran
        // 4. la cuenta no esta bloqueda
    }

}
