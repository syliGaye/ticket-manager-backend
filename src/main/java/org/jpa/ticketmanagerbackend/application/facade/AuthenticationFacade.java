package org.jpa.ticketmanagerbackend.application.facade;

import org.jpa.ticketmanagerbackend.dao.domain.UserAccountDao;
import org.jpa.ticketmanagerbackend.model.entities.UserAccount;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AuthenticationFacade implements IAuthenticationFacade{
    private UserAccountDao userAccountDao;

    @Autowired
    public AuthenticationFacade(UserAccountDao userAccountDao) {
        this.userAccountDao = userAccountDao;
    }

    @Override
    public Optional<UserAccount> getAuthenticatedUser() {
        Optional<UserAccount> optionalUser = Optional.empty();
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            optionalUser = userAccountDao.findByUsername(authentication.getName());
        }
        return optionalUser;
    }

    @Override
    public Optional<String> getAuthenticatedUsername() {
        String username = null;
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }
        return Optional.ofNullable(username);
    }

    @Override
    public String getAuthenticatedUsers() {
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            username = authentication.getName();
        }
        return username;
    }
}
