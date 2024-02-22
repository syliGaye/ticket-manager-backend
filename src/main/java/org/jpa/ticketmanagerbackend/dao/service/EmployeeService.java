package org.jpa.ticketmanagerbackend.dao.service;

import com.google.common.base.Preconditions;
import org.hibernate.exception.ConstraintViolationException;
import org.jpa.ticketmanagerbackend.dao.domain.EmployeeDao;
import org.jpa.ticketmanagerbackend.dao.repositories.EmployeeRepository;
import org.jpa.ticketmanagerbackend.model.entities.Department;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService implements EmployeeDao {
    private Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        super();
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    @Override
    public Employee save(Employee model) {
        logger.trace("exécution save({})...", model);
        final long startTime = System.currentTimeMillis();
        Employee employee = null;
        Preconditions.checkNotNull(model, "Employee est requis !");
        try {
            employee = this.employeeRepository.save(model);
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
        return employee;
    }

    @Override
    public Optional<Employee> findById(Long id) {
        logger.info("exécution findById({})...", id);
        final long startTime = System.currentTimeMillis();
        final Optional<Employee> opt = this.employeeRepository.findById(id);
        if (opt.isEmpty()) logger.error("Aucun employé trouvé avec pour identifiant: {}", id);
        logger.info("exécution findById({}) terminée en {} ms", id,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public List<Employee> findAll() {
        logger.trace("exécution findAll()...");
        final long startTime = System.currentTimeMillis();
        final List<Employee> resultList = this.employeeRepository.findAll();
        logger.info("Recherche de tous les employés: {} trouvé(s)", Integer.valueOf(resultList.size()));
        logger.trace("exécution findAll() terminée en {} ms",
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public List<Employee> findAll(int startPosition, int maxResult) {
        logger.trace("exécution findAll({}, {})...", Integer.valueOf(maxResult), Integer.valueOf(startPosition));
        final long startTime = System.currentTimeMillis();
        List<Employee> resultList = new ArrayList<>();
        final Page<Employee> employeePage = this.employeeRepository.findAll(PageRequest.of(startPosition, maxResult));
        if (employeePage.getSize() > 0) resultList = employeePage.toList();
        logger.debug("Recherche de {} employés à partir de la position {} : {} trouvé(s)", Integer.valueOf(maxResult),
                Integer.valueOf(startPosition), resultList.size());
        logger.trace("exécution findAll({}, {}) terminée en {} ms", Integer.valueOf(maxResult), Integer.valueOf(startPosition),
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(Employee model) {

    }

    @Override
    public long count() {
        logger.info("exécution count()...");
        final long startTime = System.currentTimeMillis();
        final long nbre = this.employeeRepository.count();
        logger.info("Compte tous les employés: {}", nbre);
        logger.info("exécution count() terminée en {} ms",
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return nbre;
    }

    @Override
    public List<Employee> findAllByDepartment(Department department, int startPosition, int maxResult) {
        logger.info("exécution findAllByDepartment({}, {}, {})...",
                department, startPosition, maxResult);
        final long startTime = System.currentTimeMillis();
        final Pageable pageable = PageRequest.of(startPosition, maxResult);
        final List<Employee> list = this.employeeRepository.findAllByDepartment(department, pageable);
        logger.info("Recherche de {} commentaires du departement {}, à partir de la position {} : {} trouvé(s)",
                maxResult, department.getName(),
                startPosition, Integer.valueOf(list.size())
        );
        logger.info("exécution findAllByDepartment({}, {}, {}) terminée en {} ms", department,
                startPosition, maxResult,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return list;
    }
}
