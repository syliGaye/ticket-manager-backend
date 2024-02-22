package org.jpa.ticketmanagerbackend.application.restcontroller;

import com.google.common.base.Preconditions;
import org.jpa.ticketmanagerbackend.application.restcontroller.exception.ErrorResponse;
import org.jpa.ticketmanagerbackend.dao.domain.EmployeeDao;
import org.jpa.ticketmanagerbackend.dao.domain.TicketDao;
import org.jpa.ticketmanagerbackend.dao.domain.TicketStateDao;
import org.jpa.ticketmanagerbackend.model.entities.Ticket;
import org.jpa.ticketmanagerbackend.utilities.Utility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@RestController
@RequestMapping("/ticket")
public class TicketRestController extends BaseRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TicketRestController.class);
    private final TicketDao ticketDao;
    private final TicketStateDao ticketStateDao;
    private final EmployeeDao employeeDao;

    @Autowired
    public TicketRestController(TicketDao ticketDao, TicketStateDao ticketStateDao, EmployeeDao employeeDao) {
        super();
        this.ticketDao = ticketDao;
        this.ticketStateDao = ticketStateDao;
        this.employeeDao = employeeDao;
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<Ticket>> list(
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ) {
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        return new ResponseEntity<Collection<Ticket>>(
                this.ticketDao.findAll(Utility.toInt(min), Utility.toInt(max)),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get")
    public ResponseEntity<Ticket> get(
            @RequestParam(value = "id", defaultValue = "-1") String id
    ){
        Preconditions.checkNotNull(id, "Identifiant est requis !");
        AtomicReference<Ticket> ticket = new AtomicReference<>(new Ticket());
        this.ticketDao.findById(Utility.toLong(id)).ifPresent(ticket::set);
        return new ResponseEntity<Ticket>(
                ticket.get(),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get/employee-create-by")
    public ResponseEntity<Collection<Ticket>> getCreateBy(
            @RequestParam(value = "idEmployee", defaultValue = "-1") String idEmployee,
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ){
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        Preconditions.checkNotNull(idEmployee, "Identifiant employé est requis !");
        List<Ticket> ticketList = new ArrayList<>();
        this.employeeDao.findById(Utility.toLong(idEmployee)).ifPresent(e -> {
            ticketList.addAll((Collection<Ticket>) this.ticketDao.findAllByCreateBy(e, Utility.toInt(min), Utility.toInt(max)));
        });
        return new ResponseEntity<Collection<Ticket>>(ticketList, HttpStatus.FOUND);
    }

    @GetMapping("/get/employee-assigne-to")
    public ResponseEntity<Collection<Ticket>> getAssigneTo(
            @RequestParam(value = "idEmployee", defaultValue = "-1") String idEmployee,
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ){
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        Preconditions.checkNotNull(idEmployee, "Identifiant employé est requis !");
        List<Ticket> ticketList = new ArrayList<>();
        this.employeeDao.findById(Utility.toLong(idEmployee)).ifPresent(e -> {
            ticketList.addAll((Collection<Ticket>) this.ticketDao.findAllByAssignedTo(e, Utility.toInt(min), Utility.toInt(max)));
        });
        return new ResponseEntity<Collection<Ticket>>(ticketList, HttpStatus.FOUND);
    }

    @GetMapping("/get/ticket-state")
    public ResponseEntity<Collection<Ticket>> getTicketState(
            @RequestParam(value = "idTicketState", defaultValue = "-1") String idTicketState,
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ){
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        Preconditions.checkNotNull(idTicketState, "Identifiant état ticket est requis !");
        List<Ticket> ticketList = new ArrayList<>();
        this.ticketStateDao.findById(Utility.toLong(idTicketState)).ifPresent(t -> {
            ticketList.addAll((Collection<Ticket>) this.ticketDao.findAllByTicketState(t, Utility.toInt(min), Utility.toInt(max)));
        });
        return new ResponseEntity<Collection<Ticket>>(ticketList, HttpStatus.FOUND);
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<Object> add(@RequestBody Ticket ticket){
        Preconditions.checkNotNull(ticket, "Ticket est requis !");
        if (ticket.getId() > 0L){
            final Optional<Ticket> optTicket = this.ticketDao.findById(ticket.getId());
            if (optTicket.isEmpty()){
                final ErrorResponse errorResponse = new ErrorResponse("Aucun ticket trouvé!", HttpStatus.CONFLICT);
                return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
            }
            else {
                if (optTicket.get().getCreateDate() != ticket.getCreateDate()){
                    final ErrorResponse errorResponse = new ErrorResponse("Impossible de modifier la date de création du ticket!", HttpStatus.CONFLICT);
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
                }
            }
        }
        ticket.setCreateDate(new Date());
        final Ticket ticketSaved = this.ticketDao.save(ticket);
        return new ResponseEntity<Object>(
                ticketSaved,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "id", defaultValue = "-1") String id){
        this.ticketDao.findById(Utility.toLong(id)).ifPresent(this.ticketDao::delete);
        return new ResponseEntity<Void>(HttpStatus.GONE);
    }
}
