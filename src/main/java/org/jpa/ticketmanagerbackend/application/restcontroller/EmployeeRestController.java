package org.jpa.ticketmanagerbackend.application.restcontroller;

import com.google.common.base.Preconditions;
import org.jpa.ticketmanagerbackend.dao.domain.DepartmentDao;
import org.jpa.ticketmanagerbackend.dao.domain.EmployeeDao;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.utilities.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/employee")
public class EmployeeRestController extends BaseRestController{
    /**
     * Logger
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(EmployeeRestController.class);
    private final EmployeeDao employeeDao;
    private final DepartmentDao departmentDao;

    @Autowired
    public EmployeeRestController(EmployeeDao employeeDao, DepartmentDao departmentDao) {
        super();
        this.employeeDao = employeeDao;
        this.departmentDao = departmentDao;
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<Employee>> list(
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ) {
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        return new ResponseEntity<Collection<Employee>>(
                this.employeeDao.findAll(Utility.toInt(min), Utility.toInt(max)),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get")
    public ResponseEntity<Employee> get(
            @RequestParam(value = "id", defaultValue = "-1") String id
    ){
        Preconditions.checkNotNull(id, "Identifiant est requis !");
        AtomicReference<Employee> employee = new AtomicReference<>(new Employee());
        this.employeeDao.findById(Utility.toLong(id)).ifPresent(employee::set);
        return new ResponseEntity<Employee>(
                employee.get(),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get/department")
    public ResponseEntity<Collection<Employee>> getDepartment(
            @RequestParam(value = "idDepartment", defaultValue = "-1") String idDepartment,
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ){
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        Preconditions.checkNotNull(max, "Identifiant departement est requis!");
        List<Employee> employeeList = new ArrayList<>();
        this.departmentDao.findById(Utility.toLong(idDepartment)).ifPresent(t -> {
            employeeList.addAll((Collection<Employee>) this.employeeDao.findAllByDepartment(t, Utility.toInt(min), Utility.toInt(max)));
        });
        return new ResponseEntity<Collection<Employee>>(employeeList, HttpStatus.FOUND);
    }
}
