package org.jpa.ticketmanagerbackend.dao.repositories;

import org.jpa.ticketmanagerbackend.model.entities.Department;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findAllByDepartment(Department department, Pageable pageable);
}
