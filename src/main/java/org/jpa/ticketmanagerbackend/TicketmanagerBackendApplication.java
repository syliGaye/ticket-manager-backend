package org.jpa.ticketmanagerbackend;

import org.jpa.ticketmanagerbackend.application.bootstrap.DefaultBootstrap;
import org.jpa.ticketmanagerbackend.dao.domain.DepartmentDao;
import org.jpa.ticketmanagerbackend.dao.domain.EmployeeDao;
import org.jpa.ticketmanagerbackend.dao.domain.RoleDao;
import org.jpa.ticketmanagerbackend.dao.domain.UserAccountDao;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TicketmanagerBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketmanagerBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(
            EmployeeDao employeeDao,
            DepartmentDao departmentDao,
            UserAccountDao userAccountDao,
            RoleDao roleDao
    ) {
        return args -> {
            new DefaultBootstrap(employeeDao, departmentDao, userAccountDao, roleDao).loadDatabase();
        };
    }
}
