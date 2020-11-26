package de.fabiankeck.schaetzmeisterinbackendserver.security;

import de.fabiankeck.schaetzmeisterinbackendserver.dao.SmUserDao;
import de.fabiankeck.schaetzmeisterinbackendserver.model.SmUser;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SmUserDetailsService implements UserDetailsService {
    private final SmUserDao smUserDao;

    public SmUserDetailsService(SmUserDao smUserDao) {
        this.smUserDao = smUserDao;
    }


    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        smUserDao.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new User(id, "", List.of());
    }
}
