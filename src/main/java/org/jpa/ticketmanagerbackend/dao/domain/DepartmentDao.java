package org.jpa.ticketmanagerbackend.dao.domain;

import org.jpa.ticketmanagerbackend.dao.BaseDao;
import org.jpa.ticketmanagerbackend.model.entities.Department;

import java.util.Optional;

public interface DepartmentDao extends BaseDao<Department, Long> {
    Optional<Department> findByName(String name);
}
