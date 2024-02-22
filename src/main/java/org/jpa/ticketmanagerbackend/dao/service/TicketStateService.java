package org.jpa.ticketmanagerbackend.dao.service;

import com.google.common.base.Preconditions;
import org.hibernate.exception.ConstraintViolationException;
import org.jpa.ticketmanagerbackend.dao.domain.TicketStateDao;
import org.jpa.ticketmanagerbackend.dao.repositories.TicketStateRepository;
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
public class TicketStateService implements TicketStateDao {
    private Logger logger = LoggerFactory.getLogger(TicketStateService.class);
    private TicketStateRepository ticketStateRepository;

    @Autowired
    public TicketStateService(TicketStateRepository ticketStateRepository) {
        super();
        this.ticketStateRepository = ticketStateRepository;
    }

    @Transactional
    @Override
    public TicketState save(TicketState model) {
        logger.trace("exécution persist({})...", model);
        final long startTime = System.currentTimeMillis();
        TicketState ticketState = null;
        Preconditions.checkNotNull(model, "TicketState est requis !");
        try {
            ticketState = this.ticketStateRepository.save(model);
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
        return ticketState;
    }

    @Override
    public Optional<TicketState> findById(Long id) {
        logger.info("exécution findById({})...", id);
        final long startTime = System.currentTimeMillis();
        final Optional<TicketState> opt = this.ticketStateRepository.findById(id);
        if (!opt.isPresent()) logger.error("Aucun état de ticket trouvé avec pour identifiant: {}", id);
        logger.info("exécution findById({}) terminée en {} ms", id,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public List<TicketState> findAll() {
        logger.trace("exécution findAll()...");
        final long startTime = System.currentTimeMillis();
        final List<TicketState> resultList = this.ticketStateRepository.findAll();
        logger.info("Recherche de tous les comptes: {} trouvé(s)", Integer.valueOf(resultList.size()));
        logger.trace("exécution findAll() terminée en {} ms",
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public List<TicketState> findAll(int startPosition, int maxResult) {
        logger.trace("exécution findAll({}, {})...", Integer.valueOf(maxResult), Integer.valueOf(startPosition));
        final long startTime = System.currentTimeMillis();
        List<TicketState> resultList = new ArrayList<>();
        final Page<TicketState> ticketStatePage = this.ticketStateRepository.findAll(PageRequest.of(startPosition, maxResult));
        if (ticketStatePage.getSize() > 0) resultList = ticketStatePage.toList();
        logger.debug("Recherche de {} états de ticket à partir de la position {} : {} trouvé(s)", Integer.valueOf(maxResult),
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
    public void delete(TicketState model) {

    }

    @Override
    public long count() {
        logger.info("exécution count()...");
        final long startTime = System.currentTimeMillis();
        final long nbre = this.ticketStateRepository.count();
        logger.info("Compte tous les états de tickets: {}", nbre);
        logger.info("exécution count() terminée en {} ms",
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return nbre;
    }

    @Override
    public Optional<TicketState> findByName(String name) {
        logger.info("exécution findByName({})...", name);
        final long startTime = System.currentTimeMillis();
        final Optional<TicketState> opt = this.ticketStateRepository.findByName(name);
        if (!opt.isPresent()) logger.error("Aucun état de ticket trouvé avec pour nom: {}", name);
        logger.info("exécution findByName({}) terminée en {} ms", name,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }
}
