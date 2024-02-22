package org.jpa.ticketmanagerbackend.dao.service;

import com.google.common.base.Preconditions;
import org.hibernate.exception.ConstraintViolationException;
import org.jpa.ticketmanagerbackend.dao.domain.TagDao;
import org.jpa.ticketmanagerbackend.dao.repositories.TagRepository;
import org.jpa.ticketmanagerbackend.model.entities.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceException;
import java.util.List;
import java.util.Optional;

@Service
public class TagService implements TagDao {
    private final Logger logger = LoggerFactory.getLogger(TagService.class);
    private final TagRepository tagRepository;

    @Autowired
    public TagService(TagRepository tagRepository) {
        super();
        this.tagRepository = tagRepository;
    }

    @Transactional
    @Override
    public Tag save(Tag model) {
        logger.trace("exécution persist({})...", model);
        final long startTime = System.currentTimeMillis();
        Tag tag = null;
        Preconditions.checkNotNull(model, "Tag est requis !");
        try {
            tag = this.tagRepository.save(model);
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
        return tag;
    }

    @Override
    public Optional<Tag> findById(Long id) {
        logger.info("exécution findById({})...", id);
        final long startTime = System.currentTimeMillis();
        final Optional<Tag> opt = this.tagRepository.findById(id);
        if (!opt.isPresent()) logger.error("Aucun tag trouvé avec pour identifiant: {}", id);
        logger.info("exécution findById({}) terminée en {} ms", id,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }

    @Override
    public List<Tag> findAll() {
        logger.trace("exécution findAll()...");
        final long startTime = System.currentTimeMillis();
        final List<Tag> resultList = this.tagRepository.findAll();
        logger.info("Recherche de tous les comptes: {} trouvé(s)", Integer.valueOf(resultList.size()));
        logger.trace("exécution findAll() terminée en {} ms",
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public List<Tag> findAll(int startPosition, int maxResult) {
        logger.trace("exécution findAll({}, {})...",
                Integer.valueOf(maxResult), Integer.valueOf(startPosition));
        final long startTime = System.currentTimeMillis();
        List<Tag> resultList = null;
        final Page<Tag> tagPage = this.tagRepository.findAll(PageRequest.of(startPosition, maxResult));
        if (tagPage.getSize() > 0) resultList = tagPage.toList();
        logger.debug("Recherche de {} tags à partir de la position {} : {} trouvé(s)", Integer.valueOf(maxResult),
                Integer.valueOf(startPosition), resultList.size());
        logger.trace("exécution findAll({}, {}) terminée en {} ms", Integer.valueOf(maxResult), Integer.valueOf(startPosition),
                Long.valueOf(System.currentTimeMillis() - startTime));
        return resultList;
    }

    @Override
    public void deleteById(Long id) {

    }

    @Override
    public void delete(Tag model) {

    }

    @Override
    public long count() {
        logger.info("exécution count()...");
        final long startTime = System.currentTimeMillis();
        final long nbre = this.tagRepository.count();
        logger.info("Compte tous les tags: {}", nbre);
        logger.info("exécution count() terminée en {} ms",
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return nbre;
    }

    @Override
    public Optional<Tag> findByLabel(String label) {
        logger.info("exécution findByLabel({})...", label);
        final long startTime = System.currentTimeMillis();
        final Optional<Tag> opt = this.tagRepository.findByLabel(label);
        if (!opt.isPresent()) logger.error("Aucun compte trouvé avec pour libellé: {}", label);
        logger.info("exécution findByLabel({}) terminée en {} ms", label,
                java.lang.Long.valueOf(System.currentTimeMillis() - startTime)
        );
        return opt;
    }
}
