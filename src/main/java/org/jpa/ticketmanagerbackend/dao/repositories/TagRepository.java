package org.jpa.ticketmanagerbackend.dao.repositories;

import org.jpa.ticketmanagerbackend.model.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 *
 */
public interface TagRepository extends JpaRepository<Tag, Long> {
    /**
     *
     * @param label
     * @return
     */
    Optional<Tag> findByLabel(String label);
}
