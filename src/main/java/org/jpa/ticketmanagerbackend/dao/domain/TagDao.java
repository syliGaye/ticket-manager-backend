package org.jpa.ticketmanagerbackend.dao.domain;

import org.jpa.ticketmanagerbackend.dao.BaseDao;
import org.jpa.ticketmanagerbackend.model.entities.Tag;

import java.util.Optional;

public interface TagDao extends BaseDao<Tag, Long> {
    Optional<Tag> findByLabel(String label);
}
