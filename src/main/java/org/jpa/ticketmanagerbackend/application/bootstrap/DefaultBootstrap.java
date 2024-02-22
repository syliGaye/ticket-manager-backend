package org.jpa.ticketmanagerbackend.application.bootstrap;

import org.jpa.ticketmanagerbackend.dao.domain.DepartmentDao;
import org.jpa.ticketmanagerbackend.dao.domain.EmployeeDao;
import org.jpa.ticketmanagerbackend.dao.domain.RoleDao;
import org.jpa.ticketmanagerbackend.dao.domain.UserAccountDao;
import org.jpa.ticketmanagerbackend.model.entities.Department;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.Role;
import org.jpa.ticketmanagerbackend.model.entities.UserAccount;
import org.springframework.scheduling.annotation.Async;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

public class DefaultBootstrap {
    private final EmployeeDao employeeDao;
    private final DepartmentDao departmentDao;
    private final UserAccountDao userAccountDao;
    private final RoleDao roleDao;

    public DefaultBootstrap(EmployeeDao employeeDao, DepartmentDao departmentDao, UserAccountDao userAccountDao, RoleDao roleDao) {
        this.employeeDao = employeeDao;
        this.departmentDao = departmentDao;
        this.userAccountDao = userAccountDao;
        this.roleDao = roleDao;
    }

    @Async
    public void loadDatabase() {
        if (this.roleDao.count() == 0L){
            this.roleDao.save(new Role("superadmin"));
        }

        if (this.departmentDao.count() == 0L && this.employeeDao.count() == 0L && this.userAccountDao.count() == 0L){
            Department department = this.departmentDao.save(new Department("Support"));

            this.roleDao.findByName("superadmin").ifPresent(role -> {
                this.userAccountDao.save(new UserAccount(
                        "admin",
                        "password",
                        "sylvestregaye@gmail.com",
                        new Employee("Sylvestre GAYE", department),
                        new HashSet<>(Collections.singletonList(role))
                ));
            });
        }
    }
}
