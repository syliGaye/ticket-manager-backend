package org.jpa.ticketmanagerbackend.dao.service;

import com.google.common.base.Preconditions;
import org.hibernate.exception.ConstraintViolationException;
import org.jpa.ticketmanagerbackend.dao.domain.TicketDao;
import org.jpa.ticketmanagerbackend.dao.repositories.TicketRepository;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.Ticket;
import org.jpa.ticketmanagerbackend.model.entities.TicketState;
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
public class TicketService implements TicketDao {
    private Logger logger = LoggerFactory.getLogger(TicketService.class);
    private TicketRepository ticketRepository;

    @Autowired
    public TicketService(TicketRepository ticketRepository) {
        super();
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    @Override
    public Ticket save(Ticket model) {
        logger.trace("exécution persist({})...", model);
        final long startTime = System.currentTimeMillis();
        Ticket ticket = null;
        Preconditions.checkNotNull(model, "Ticket est requis !");
        try {
            ticket = this.ticketRepository.save(model);
        } catch (final ConstraintViolationException e) {
            throw new ConstraintViolationException(e.getMessage(), e.getSQLException(), e.getConstraintName());
        } catch (final PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new ConstraintViolationException(e.getCause().getMessage(), ((ConstraintViolationException) e.getCause()).getSQLException(), ((ConstraintViolationException) e.getCause()).getConstraintName());
            }
            throw e;
        }
        logger.trace("exécution persist({}) terminée en {} ms", model,
                Long.valueOf(System.currentTimeMillis() - startTime));
        return ticket;
    }

    @Override
    public Optional<Ticket> findById(Long id) {
        logger.info("exécution findById({})...", id);
        final long startTime = System.currentTimeMillis();
        final Optional<Ticket> opt = this.ticketRepository.findById(id);
        if (!opt.isPresent()) logger.error("Aucun ticket trouvé avec pour identifiant: {}", id);
        logger.info("exécution findById({}) terminée en {} ms", id,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public List<Ticket> findAll() {
        logger.trace("exécution findAll()...");
        final long startTime = System.currentTimeMillis();
        final List<Ticket> resultList = this.ticketRepository.findAll();
        logger.info("Recherche de tous les tickets: {} trouvé(s)", Integer.valueOf(resultList.size()));
        logger.trace("exécution findAll() terminée en {} ms",
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public List<Ticket> findAll(int startPosition, int maxResult) {
        logger.trace("exécution findAll({}, {})...", Integer.valueOf(maxResult), Integer.valueOf(startPosition));
        final long startTime = System.currentTimeMillis();
        List<Ticket> resultList = new ArrayList<>();
        final Page<Ticket> ticketPage = this.ticketRepository.findAll(PageRequest.of(startPosition, maxResult));
        if (ticketPage.getSize() > 0) resultList = ticketPage.toList();
        logger.debug("Recherche de {} tickets à partir de la position {} : {} trouvé(s)", Integer.valueOf(maxResult),
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
    public void delete(Ticket model) {

    }

    @Override
    public long count() {
        logger.info("exécution count()...");
        final long startTime = System.currentTimeMillis();
        final long nbre = this.ticketRepository.count();
        logger.info("Compte tous les tickets: {}", nbre);
        logger.info("exécution count() terminée en {} ms",
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return nbre;
    }

    @Override
    public Optional<Ticket> findByNumber(String number) {
        logger.info("exécution findByNumber({})...", number);
        final long startTime = System.currentTimeMillis();
        final Optional<Ticket> opt = this.ticketRepository.findByNumber(number);
        if (!opt.isPresent()) logger.error("Aucun ticket trouvé avec pour numéro: {}", number);
        logger.info("exécution findByNumber({}) terminée en {} ms", number,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public List<Ticket> findAllByTicketState(TicketState ticketState, int startPosition, int maxResult) {
        logger.info("exécution findAllByTicketState({}, {}, {})...",
                ticketState, startPosition, maxResult);
        final long startTime = System.currentTimeMillis();
        final List<Ticket> list = this.ticketRepository.findAllByTicketState(ticketState, PageRequest.of(startPosition, maxResult));
        logger.info("Recherche de {} tickets de l'état {} à partir de la position {} : {} trouvé(s)",
                maxResult, ticketState.getName(),
                startPosition, Integer.valueOf(list.size())
        );
        logger.info("exécution findAllByTicketState({}, {}, {}) terminée en {} ms", ticketState,
                startPosition, maxResult,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return list;
    }

    @Override
    public List<Ticket> findAllByCreateBy(Employee createBy, int startPosition, int maxResult) {
        logger.info("exécution findAllByCreateBy({}, {}, {})...",
                createBy, startPosition, maxResult);
        final long startTime = System.currentTimeMillis();
        final List<Ticket> list = this.ticketRepository.findAllByCreateBy(createBy, PageRequest.of(startPosition, maxResult));
        logger.info("Recherche de {} tickets créés par l'employé {} à partir de la position {} : {} trouvé(s)",
                maxResult, createBy.getName(),
                startPosition, Integer.valueOf(list.size())
        );
        logger.info("exécution findAllByCreateBy({}, {}, {}) terminée en {} ms", createBy,
                startPosition, maxResult,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return list;
    }

    @Override
    public List<Ticket> findAllByAssignedTo(Employee assignedTo, int startPosition, int maxResult) {
        logger.info("exécution findAllByAssignedTo({}, {}, {})...",
                assignedTo, startPosition, maxResult);
        final long startTime = System.currentTimeMillis();
        final List<Ticket> list = this.ticketRepository.findAllByAssignedTo(assignedTo, PageRequest.of(startPosition, maxResult));
        logger.info("Recherche de {} tickets assignés à l'employé {} à partir de la position {} : {} trouvé(s)",
                maxResult, assignedTo.getName(),
                startPosition, Integer.valueOf(list.size())
        );
        logger.info("exécution findAllByAssignedTo({}, {}, {}) terminée en {} ms", assignedTo,
                startPosition, maxResult,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return list;
    }
}
