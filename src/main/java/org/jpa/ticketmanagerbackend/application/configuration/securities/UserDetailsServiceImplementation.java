package org.jpa.ticketmanagerbackend.application.configuration.securities;

import org.jpa.ticketmanagerbackend.dao.domain.UserAccountDao;
import org.jpa.ticketmanagerbackend.model.entities.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImplementation implements UserDetailsService {

    private final UserAccountDao userAccountDao;

    @Autowired
    public UserDetailsServiceImplementation(@Lazy UserAccountDao userAccountDao) {
        this.userAccountDao = userAccountDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Optional<UserAccount> optUserAccount = userAccountDao.findByUsername(username);
        if (optUserAccount.isEmpty()) {
            throw new UsernameNotFoundException("Invalid username and password");
        }
        return new CustomUserPrincipal(optUserAccount.get());
    }
}