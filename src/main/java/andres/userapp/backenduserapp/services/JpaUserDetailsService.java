package andres.userapp.backenduserapp.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// JpaUserDetailsService ESTA CLASE TRABAJA POR DEBAJO COMO TAL NO LO VEMOS
@Service
public class JpaUserDetailsService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!username.equals("admin")) {
            throw new UsernameNotFoundException(String.format("El username no existe en el sistema %s", username));
        }

        // EN CASO DE EXISTA LO AUTENTICAMOS
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // TODOS LOS ROLES EMPIEZAN CON EL PREFIJO Y EN MAYUSCULA ROLE EJEMPLO EL ROL ES
        // USUARIO (ROLE_USER)

        // $2a$10$DOMDxjYyfZ/e7RcBfUpzqeaCs8pLgcizuiQWXPkU35nOhZlFcE9MS
        return new User(username, "$2a$10$DOMDxjYyfZ/e7RcBfUpzqeaCs8pLgcizuiQWXPkU35nOhZlFcE9MS", true, true, true,
                true, authorities);
        // 1. true significa que el usuario esta habilitado
        // 2. la cuenta no expira
        // 3. que las credenciales no expiran
        // 4. la cuenta no esta bloqueda
    }

}
