package org.jpa.ticketmanagerbackend.application.restcontroller;

import com.google.common.base.Preconditions;
import org.jpa.ticketmanagerbackend.dao.domain.DepartmentDao;
import org.jpa.ticketmanagerbackend.model.entities.Department;
import org.jpa.ticketmanagerbackend.utilities.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/department")
public class DepartmentRestController extends BaseRestController {
    /**
     * Logger
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(DepartmentRestController.class);
    private final DepartmentDao departmentDao;

    @Autowired
    public DepartmentRestController(DepartmentDao departmentDao) {
        super();
        this.departmentDao = departmentDao;
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<Department>> list(
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ) {
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        return new ResponseEntity<Collection<Department>>(
                this.departmentDao.findAll(Utility.toInt(min), Utility.toInt(max)),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get")
    public ResponseEntity<Department> get(
            @RequestParam(value = "id", defaultValue = "-1") String id
    ){
        Preconditions.checkNotNull(id, "Identifiant est requis !");
        AtomicReference<Department> department = new AtomicReference<>(new Department());
        this.departmentDao.findById(Utility.toLong(id)).ifPresent(department::set);
        return new ResponseEntity<Department>(
                department.get(),
                HttpStatus.FOUND
        );
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<Department> add(@RequestBody Department department){
        Preconditions.checkNotNull(department, "Departement est requis !");
        final Department departmentSaved = this.departmentDao.save(department);
        return new ResponseEntity<Department>(
                departmentSaved,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "id", defaultValue = "-1") String id){
        this.departmentDao.findById(Utility.toLong(id)).ifPresent(this.departmentDao::delete);
        return new ResponseEntity<Void>(HttpStatus.GONE);
    }
}
