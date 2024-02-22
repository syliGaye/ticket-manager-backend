package org.jpa.ticketmanagerbackend.dao.service;

import com.google.common.base.Preconditions;
import org.hibernate.exception.ConstraintViolationException;
import org.jpa.ticketmanagerbackend.dao.domain.CommentsDao;
import org.jpa.ticketmanagerbackend.dao.repositories.CommentsRepository;
import org.jpa.ticketmanagerbackend.model.entities.Comments;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.Ticket;
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
public class CommentsService implements CommentsDao {
    private Logger logger = LoggerFactory.getLogger(CommentsService.class);
    private CommentsRepository commentsRepository;

    @Autowired
    public CommentsService(CommentsRepository commentsRepository) {
        super();
        this.commentsRepository = commentsRepository;
    }

    @Transactional
    @Override
    public Comments save(Comments model) {
        logger.info("exécution save({})...", model);
        final long startTime = System.currentTimeMillis();
        Comments comments = null;
        Preconditions.checkNotNull(model, "Comments est requis !");
        try {
            comments = this.commentsRepository.save(model);
        } catch (final ConstraintViolationException e) {
            throw new ConstraintViolationException(e.getMessage(), e.getSQLException(), e.getConstraintName());
        } catch (final PersistenceException e) {
            if (e.getCause() instanceof ConstraintViolationException) {
                throw new ConstraintViolationException(e.getCause().getMessage(), ((ConstraintViolationException) e.getCause()).getSQLException(), ((ConstraintViolationException) e.getCause()).getConstraintName());
            }
            throw e;
        }
        logger.info("exécution save({}) terminée en {} ms", model,
                Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return comments;
    }

    @Override
    public Optional<Comments> findById(Long id) {
        logger.info("exécution findById({})...", id);
        final long startTime = System.currentTimeMillis();
        Optional<Comments> opt = this.commentsRepository.findById(id);
        if (!opt.isPresent()) logger.error("Aucun commentaire trouvé avec pour identifiant: {}", id);
        logger.info("exécution findById({}) terminée en {} ms", id,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public List<Comments> findAll() {
        logger.trace("exécution findAll()...");
        final long startTime = System.currentTimeMillis();
        final List<Comments> resultList = this.commentsRepository.findAll();
        logger.info("Recherche de tous les commentaires: {} trouvé(s)", Integer.valueOf(resultList.size()));
        logger.trace("exécution findAll() terminée en {} ms", Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public List<Comments> findAll(int startPosition, int maxResult) {
        logger.trace("exécution findAll({}, {})...", Integer.valueOf(maxResult), Integer.valueOf(startPosition));
        final long startTime = System.currentTimeMillis();
        List<Comments> resultList = new ArrayList<>();
        final Pageable pageable = PageRequest.of(startPosition, maxResult);
        final Page<Comments> commentsPage = this.commentsRepository.findAll(pageable);
        if (commentsPage.getSize() > 0) resultList = commentsPage.toList();
        logger.debug("Recherche de {} commentaires à partir de la position {} : {} trouvé(s)", Integer.valueOf(maxResult),
                Integer.valueOf(startPosition), resultList.size());
        logger.trace("exécution findAll({}, {}) terminée en {} ms", Integer.valueOf(maxResult), Integer.valueOf(startPosition),
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(Comments model) {

    }

    @Override
    public long count() {
        logger.info("exécution count()...");
        final long startTime = System.currentTimeMillis();
        final long nbre = this.commentsRepository.count();
        logger.info("Compte tous les commentaires: {}", nbre);
        logger.info("exécution count() terminée en {} ms",
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return nbre;
    }

    @Override
    public List<Comments> findAllByTicket(Ticket ticket, int startPosition, int maxResult) {
        logger.info("exécution findAllByTicket({}, {}, {})...", ticket, startPosition, maxResult);
        final long startTime = System.currentTimeMillis();
        final Pageable pageable = PageRequest.of(startPosition, maxResult);
        final List<Comments> list = this.commentsRepository.findAllByTicket(ticket, pageable);
        logger.info("Recherche de {} commentaires du ticket {} à partir de la position {} : {} trouvé(s)",
                maxResult, ticket.getNumber(),
                startPosition, Integer.valueOf(list.size())
        );
        logger.info("exécution findAllByTicket({}, {}, {}) terminée en {} ms", ticket, startPosition,
                maxResult, java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return list;
    }

    @Override
    public List<Comments> findAllByEmployee(Employee employee, int startPosition, int maxResult) {
        logger.info("exécution findAllByEmployee({}, {}, {})...", employee, startPosition, maxResult);
        final long startTime = System.currentTimeMillis();
        final Pageable pageable = PageRequest.of(startPosition, maxResult);
        final List<Comments> list = this.commentsRepository.findAllByEmployee(employee, pageable);
        logger.info("Recherche de {} commentaires de l'employé {} à partir de la position {} : {} trouvé(s)",
                maxResult, employee.getName(),
                startPosition, Integer.valueOf(list.size())
        );
        logger.info("exécution findAllByTicket({}, {}, {}) terminée en {} ms", employee, startPosition,
                maxResult, java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return list;
    }

    @Override
    public List<Comments> findAllByTicketAndEmployee(Ticket ticket, Employee employee, int startPosition, int maxResult) {
        logger.info("exécution findAllByTicketAndEmployee({}, {}, {}, {})...",
                ticket, employee, startPosition, maxResult);
        final long startTime = System.currentTimeMillis();
        final Pageable pageable = PageRequest.of(startPosition, maxResult);
        final List<Comments> list = this.commentsRepository.findAllByTicketAndEmployee(ticket, employee, pageable);
        logger.info("Recherche de {} commentaires de l'employé {} sur le ticket {} à partir de la position {} : {} trouvé(s)",
                maxResult, employee.getName(), ticket.getNumber(),
                startPosition, Integer.valueOf(list.size())
        );
        logger.info("exécution findAllByTicketAndEmployee({}, {}, {}, {}) terminée en {} ms", ticket, employee,
                startPosition, maxResult,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return list;
    }
}
