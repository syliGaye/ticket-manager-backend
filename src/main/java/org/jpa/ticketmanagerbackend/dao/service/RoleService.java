package org.jpa.ticketmanagerbackend.dao.service;

import com.google.common.base.Preconditions;
import org.hibernate.exception.ConstraintViolationException;
import org.jpa.ticketmanagerbackend.dao.domain.RoleDao;
import org.jpa.ticketmanagerbackend.dao.repositories.RoleRepository;
import org.jpa.ticketmanagerbackend.model.entities.Role;
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
public class RoleService implements RoleDao {
    private Logger logger = LoggerFactory.getLogger(RoleService.class);
    private RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        super();
        this.roleRepository = roleRepository;
    }

    @Transactional
    @Override
    public Role save(Role model) {
        logger.trace("exécution save({})...", model);
        final long startTime = System.currentTimeMillis();
        Role role = null;
        Preconditions.checkNotNull(model, "Rôle est requis !");
        try {
            role = this.roleRepository.save(model);
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
        return role;
    }

    @Override
    public Optional<Role> findById(Long id) {
        logger.info("exécution findById({})...", id);
        final long startTime = System.currentTimeMillis();
        final Optional<Role> opt = this.roleRepository.findById(id);
        if (!opt.isPresent()) logger.error("Aucun rôle trouvé avec pour identifiant: {}", id);
        logger.info("exécution findById({}) terminée en {} ms", id,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public List<Role> findAll() {
        logger.trace("exécution findAll()...");
        final long startTime = System.currentTimeMillis();
        final List<Role> resultList = this.roleRepository.findAll();
        logger.info("Recherche de tous les comptes: {} trouvé(s)", Integer.valueOf(resultList.size()));
        logger.trace("exécution findAll() terminée en {} ms",
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public List<Role> findAll(int startPosition, int maxResult) {
        logger.trace("exécution findAll({}, {})...", Integer.valueOf(maxResult), Integer.valueOf(startPosition));
        final long startTime = System.currentTimeMillis();
        List<Role> resultList = new ArrayList<>();
        final Page<Role> rolePage = this.roleRepository.findAll(PageRequest.of(startPosition, maxResult));
        if (rolePage.getSize() > 0) resultList = rolePage.toList();
        logger.debug("Recherche de {} rôles à partir de la position {} : {} trouvé(s)", Integer.valueOf(maxResult),
                Integer.valueOf(startPosition), resultList.size());
        logger.trace("exécution findAll({}, {}) terminée en {} ms", Integer.valueOf(maxResult), Integer.valueOf(startPosition),
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(Role model) {

    }

    @Override
    public long count() {
        logger.info("exécution count()...");
        final long startTime = System.currentTimeMillis();
        final long nbre = this.roleRepository.count();
        logger.info("Compte tous les rôles: {}", nbre);
        logger.info("exécution count() terminée en {} ms",
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return nbre;
    }

    @Override
    public Optional<Role> findByName(String name) {
        logger.info("exécution findByName({})...", name);
        final long startTime = System.currentTimeMillis();
        final Optional<Role> opt = this.roleRepository.findByName(name);
        if (!opt.isPresent()) logger.error("Aucun rôle trouvé avec pour nom: {}", name);
        logger.info("exécution findByName({}) terminée en {} ms", name,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }
}
