package org.jpa.ticketmanagerbackend.application.restcontroller;

import com.google.common.base.Preconditions;
import org.jpa.ticketmanagerbackend.dao.domain.CommentsDao;
import org.jpa.ticketmanagerbackend.dao.domain.EmployeeDao;
import org.jpa.ticketmanagerbackend.dao.domain.TicketDao;
import org.jpa.ticketmanagerbackend.model.entities.Comments;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.Ticket;
import org.jpa.ticketmanagerbackend.utilities.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/comments")
public class CommentsRestController extends BaseRestController {
    /**
     * Logger
     */
    private final static Logger LOGGER = LoggerFactory.getLogger(CommentsRestController.class);
    private final CommentsDao commentsDao;
    private final EmployeeDao employeeDao;
    private final TicketDao ticketDao;

    @Autowired
    public CommentsRestController(
            CommentsDao commentsDao,
            EmployeeDao employeeDao,
            TicketDao ticketDao
    ) {
        super();
        this.commentsDao = commentsDao;
        this.employeeDao = employeeDao;
        this.ticketDao = ticketDao;
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<Comments>> list(
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ) {
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        return new ResponseEntity<Collection<Comments>>(
                this.commentsDao.findAll(Utility.toInt(min), Utility.toInt(max)),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get")
    public ResponseEntity<Comments> get(
            @RequestParam(value = "id", defaultValue = "-1") String id
    ){
        Preconditions.checkNotNull(id, "Identifiant est requis !");
        AtomicReference<Comments> comments = new AtomicReference<>(new Comments());
        this.commentsDao.findById(Utility.toLong(id)).ifPresent(comments::set);
        return new ResponseEntity<Comments>(
                comments.get(),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get/employee")
    public ResponseEntity<Collection<Comments>> getEmployee(
            @RequestParam(value = "idEmployee", defaultValue = "-1") String idEmployee,
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ){
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        Preconditions.checkNotNull(idEmployee, "Identifiant employé est requis !");
        List<Comments> commentsList = new ArrayList<>();
        this.employeeDao.findById(Utility.toLong(idEmployee)).ifPresent(e -> {
            commentsList.addAll((Collection<Comments>) this.commentsDao.findAllByEmployee(e, Utility.toInt(min), Utility.toInt(max)));
        });
        return new ResponseEntity<Collection<Comments>>(commentsList, HttpStatus.FOUND);
    }

    @GetMapping("/get/ticket")
    public ResponseEntity<Collection<Comments>> getTicket(
            @RequestParam(value = "idTicket", defaultValue = "-1") String idTicket,
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ){
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        Preconditions.checkNotNull(idTicket, "Identifiant ticket est requis !");
        List<Comments> commentsList = new ArrayList<>();
        this.ticketDao.findById(Utility.toLong(idTicket)).ifPresent(t -> {
            commentsList.addAll((Collection<Comments>) this.commentsDao.findAllByTicket(t, Utility.toInt(min), Utility.toInt(max)));
        });
        return new ResponseEntity<Collection<Comments>>(commentsList, HttpStatus.FOUND);
    }

    @GetMapping("/get/employee-ticket")
    public ResponseEntity<Collection<Comments>> getEmployeeTicket(
            @RequestParam(value = "idEmployee", defaultValue = "-1") String idEmployee,
            @RequestParam(value = "idTicket", defaultValue = "-1") String idTicket,
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ){
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        Preconditions.checkNotNull(idEmployee, "Identifiant employé est requis !");
        Preconditions.checkNotNull(idTicket, "Identifiant ticket est requis !");
        List<Comments> commentsList = new ArrayList<>();
        final Optional<Ticket> optTicket = this.ticketDao.findById(Utility.toLong(idTicket));
        final Optional<Employee> optEmployee = this.employeeDao.findById(Utility.toLong(idEmployee));
        if (optEmployee.isPresent() && optTicket.isPresent()){
            commentsList.addAll((Collection<Comments>) this.commentsDao.findAllByTicketAndEmployee(
                    optTicket.get(),
                    optEmployee.get(),
                    Utility.toInt(min),
                    Utility.toInt(max))
            );
        }
        return new ResponseEntity<Collection<Comments>>(commentsList, HttpStatus.FOUND);
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<Comments> add(@RequestBody Comments comments){
        Preconditions.checkNotNull(comments, "Commentaire est requis !");
        final Comments commentsSaved = this.commentsDao.save(comments);
        return new ResponseEntity<Comments>(
                commentsSaved,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "id", defaultValue = "-1") String id){
        this.commentsDao.findById(Utility.toLong(id)).ifPresent(this.commentsDao::delete);
        return new ResponseEntity<Void>(HttpStatus.GONE);
    }
}
