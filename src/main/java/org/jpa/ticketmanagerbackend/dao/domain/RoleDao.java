package org.jpa.ticketmanagerbackend.dao.domain;

import org.jpa.ticketmanagerbackend.dao.BaseDao;
import org.jpa.ticketmanagerbackend.model.entities.Role;

import java.util.Optional;

public interface RoleDao extends BaseDao<Role, Long> {
    Optional<Role> findByName(String name);
}
