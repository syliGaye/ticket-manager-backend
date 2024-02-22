package org.jpa.ticketmanagerbackend.application.restcontroller;

import com.google.common.base.Preconditions;
import org.jpa.ticketmanagerbackend.dao.domain.RoleDao;
import org.jpa.ticketmanagerbackend.model.entities.Role;
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
@RequestMapping("role")
public class RoleRestController extends BaseRestController {
    /**
     * Logger
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(RoleRestController.class);
    private final RoleDao roleDao;

    @Autowired
    public RoleRestController(RoleDao roleDao) {
        super();
        this.roleDao = roleDao;
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<Role>> list(
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ) {
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        return new ResponseEntity<Collection<Role>>(
                this.roleDao.findAll(Utility.toInt(min), Utility.toInt(max)),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get")
    public ResponseEntity<Role> get(
            @RequestParam(value = "id", defaultValue = "-1") String id
    ){
        Preconditions.checkNotNull(id, "Identifiant est requis !");
        AtomicReference<Role> role = new AtomicReference<>(new Role());
        this.roleDao.findById(Utility.toLong(id)).ifPresent(role::set);
        return new ResponseEntity<Role>(
                role.get(),
                HttpStatus.FOUND
        );
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<Role> add(@RequestBody Role role){
        Preconditions.checkNotNull(role, "Role est requis !");
        final Role roleSaved = this.roleDao.save(role);
        return new ResponseEntity<Role>(
                roleSaved,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "id", defaultValue = "-1") String id){
        this.roleDao.findById(Utility.toLong(id)).ifPresent(this.roleDao::delete);
        return new ResponseEntity<Void>(HttpStatus.GONE);
    }
}
