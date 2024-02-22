package org.jpa.ticketmanagerbackend.dao.domain;

import org.jpa.ticketmanagerbackend.dao.BaseDao;
import org.jpa.ticketmanagerbackend.model.entities.Department;
import org.jpa.ticketmanagerbackend.model.entities.Employee;

import java.util.List;

public interface EmployeeDao extends BaseDao<Employee, Long> {
    List<Employee> findAllByDepartment(Department department, int startPosition, int maxResult);
}
