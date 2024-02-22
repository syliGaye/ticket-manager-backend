package org.jpa.ticketmanagerbackend.dao.repositories;

import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 */
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUsername(String username);
    Optional<UserAccount> findByEmail(String email);
    Optional<UserAccount> findByEmployee(Employee employee);
}
