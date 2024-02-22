package org.jpa.ticketmanagerbackend.application.facade;

import org.jpa.ticketmanagerbackend.model.entities.UserAccount;

import java.util.Optional;

public interface IAuthenticationFacade {
    Optional<UserAccount> getAuthenticatedUser();
    Optional<String> getAuthenticatedUsername();
    String getAuthenticatedUsers();
}
