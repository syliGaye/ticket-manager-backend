package org.jpa.ticketmanagerbackend.dao.service;

import com.google.common.base.Preconditions;
import org.hibernate.exception.ConstraintViolationException;
import org.jpa.ticketmanagerbackend.dao.domain.UserAccountDao;
import org.jpa.ticketmanagerbackend.dao.repositories.UserAccountRepository;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.UserAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService implements UserAccountDao {
    private Logger logger = LoggerFactory.getLogger(UserAccountService.class);
    private UserAccountRepository userAccountRepository;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository) {
        super();
        this.userAccountRepository = userAccountRepository;
    }

    @Transactional
    @Override
    public UserAccount save(UserAccount model) {
        logger.trace("exécution save({})...", model);
        final long startTime = System.currentTimeMillis();
        UserAccount user = null;
        Preconditions.checkNotNull(model, "User est requis !");
        try {
            user = this.userAccountRepository.save(model);
        } catch (final ConstraintViolationException e) {
            throw new ConstraintViolationException(e.getMessage(), e.getSQLException(), e.getConstraintName());
        } catch (final PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new ConstraintViolationException(e.getCause().getMessage(), ((ConstraintViolationException) e.getCause()).getSQLException(), ((ConstraintViolationException) e.getCause()).getConstraintName());
            }
            throw e;
        }
        logger.trace("exécution save({}) terminée en {} ms", model,
                Long.valueOf(System.currentTimeMillis() - startTime));
        return user;
    }

    @Override
    public Optional<UserAccount> findById(Long id) {
        logger.info("exécution findById({})...", id);
        final long startTime = System.currentTimeMillis();
        final Optional<UserAccount> opt = this.userAccountRepository.findById(id);
        if (!opt.isPresent()) logger.error("Aucun utilisateur trouvé avec pour identifiant: {}", id);
        logger.info("exécution findById({}) terminée en {} ms", id,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public List<UserAccount> findAll() {
        logger.trace("exécution findAll()...");
        final long startTime = System.currentTimeMillis();
        final List<UserAccount> resultList = this.userAccountRepository.findAll();
        logger.info("Recherche de tous les utilisateurs: {} trouvé(s)", Integer.valueOf(resultList.size()));
        logger.trace("exécution findAll() terminée en {} ms",
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public List<UserAccount> findAll(int startPosition, int maxResult) {
        logger.trace("exécution findAll({}, {})...",
                Integer.valueOf(maxResult), Integer.valueOf(startPosition));
        final long startTime = System.currentTimeMillis();
        List<UserAccount> resultList = new ArrayList<>();
        final Page<UserAccount> userAccountPage = this.userAccountRepository.findAll(PageRequest.of(startPosition, maxResult));
        if (userAccountPage.getSize() > 0) resultList = userAccountPage.toList();
        logger.debug("Recherche de {} utilisateurs à partir de la position {} : {} trouvé(s)", Integer.valueOf(maxResult),
                Integer.valueOf(startPosition), resultList.size());
        logger.trace("exécution findAll({}, {}) terminée en {} ms",
                Integer.valueOf(maxResult), Integer.valueOf(startPosition),
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(UserAccount model) {

    }

    @Override
    public long count() {
        logger.info("exécution count()...");
        final long startTime = System.currentTimeMillis();
        final long nbre = this.userAccountRepository.count();
        logger.info("Compte tous les utilisateurs: {}", nbre);
        logger.info("exécution count() terminée en {} ms",
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return nbre;
    }

    @Override
    public Optional<UserAccount> findByUsername(String username) {
        logger.info("exécution findByUsername({})...", username);
        final long startTime = System.currentTimeMillis();
        final Optional<UserAccount> opt = this.userAccountRepository.findByUsername(username);
        if (!opt.isPresent()) logger.error("Aucun utilisateur trouvé avec pour nom d'utilisateur: {}", username);
        logger.info("exécution findByUsername({}) terminée en {} ms", username,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public Optional<UserAccount> findByEmail(String email) {
        logger.info("exécution findByEmail({})...", email);
        final long startTime = System.currentTimeMillis();
        final Optional<UserAccount> opt = this.userAccountRepository.findByEmail(email);
        if (!opt.isPresent()) logger.error("Aucun utilisateur trouvé avec pour adresse email: {}", email);
        logger.info("exécution findByEmail({}) terminée en {} ms", email,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public Optional<UserAccount> findByEmployee(Employee employee) {
        logger.info("exécution findByEmployee({})...", employee);
        final long startTime = System.currentTimeMillis();
        final Optional<UserAccount> opt = this.userAccountRepository.findByEmployee(employee);
        if (!opt.isPresent()) logger.error("Aucun utilisateur trouvé comme employé: {}", employee.getName());
        logger.info("exécution findByEmployee({}) terminée en {} ms", employee,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }
}
