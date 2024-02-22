package org.jpa.ticketmanagerbackend.dao.service;

import com.google.common.base.Preconditions;
import org.hibernate.exception.ConstraintViolationException;
import org.jpa.ticketmanagerbackend.dao.domain.DepartmentDao;
import org.jpa.ticketmanagerbackend.dao.repositories.DepartmentRepository;
import org.jpa.ticketmanagerbackend.model.entities.Department;
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
public class DepartmentService implements DepartmentDao {
    private final static Logger logger = LoggerFactory.getLogger(DepartmentService.class);
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DepartmentService(DepartmentRepository departmentRepository) {
        super();
        this.departmentRepository = departmentRepository;
    }

    @Transactional
    @Override
    public Department save(Department model) {
        logger.trace("exécution save({})...", model);
        final long startTime = System.currentTimeMillis();
        Department department = null;
        Preconditions.checkNotNull(model, "Departement est requis !");
        try {
            department = this.departmentRepository.save(model);
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
        return department;
    }

    @Override
    public Optional<Department> findById(Long id) {
        logger.info("exécution findById({})...", id);
        final long startTime = System.currentTimeMillis();
        Optional<Department> opt = this.departmentRepository.findById(id);
        if (!opt.isPresent()) logger.error("Aucun departement trouvé avec pour identifiant: {}", id);
        logger.info("exécution findById({}) terminée en {} ms", id,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public List<Department> findAll() {
        logger.trace("exécution findAll()...");
        final long startTime = System.currentTimeMillis();
        final List<Department> resultList = this.departmentRepository.findAll();
        logger.info("Recherche de tous les departements: {} trouvé(s)", Integer.valueOf(resultList.size()));
        logger.trace("exécution findAll() terminée en {} ms",
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public List<Department> findAll(int startPosition, int maxResult) {
        logger.trace("exécution findAll({}, {})...", Integer.valueOf(maxResult), Integer.valueOf(startPosition));
        final long startTime = System.currentTimeMillis();
        List<Department> resultList = new ArrayList<>();
        final Page<Department> departmentPage = this.departmentRepository.findAll(PageRequest.of(startPosition, maxResult));
        if (departmentPage.getSize() > 0) resultList = departmentPage.toList();
        logger.debug("Recherche de {} departements à partir de la position {} : {} trouvé(s)", Integer.valueOf(maxResult),
                Integer.valueOf(startPosition), resultList.size());
        logger.trace("exécution findAll({}, {}) terminée en {} ms", Integer.valueOf(maxResult), Integer.valueOf(startPosition),
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(Department model) {
    }

    @Override
    public long count() {
        logger.info("exécution count()...");
        final long startTime = System.currentTimeMillis();
        final long nbre = this.departmentRepository.count();
        logger.info("Compte tous les departements: {}", nbre);
        logger.info("exécution count() terminée en {} ms",
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return nbre;
    }

    @Override
    public Optional<Department> findByName(String name) {
        logger.info("exécution findByName({})...", name);
        final long startTime = System.currentTimeMillis();
        final Optional<Department> opt = this.departmentRepository.findByName(name);
        if (!opt.isPresent()) logger.error("Aucun departement trouvé avec pour nom: {}", name);
        logger.info("exécution findByName({}) terminée en {} ms", name,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }
}
