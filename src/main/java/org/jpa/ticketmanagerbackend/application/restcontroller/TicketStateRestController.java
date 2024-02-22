package org.jpa.ticketmanagerbackend.application.restcontroller;

import com.google.common.base.Preconditions;
import org.jpa.ticketmanagerbackend.dao.domain.TicketStateDao;
import org.jpa.ticketmanagerbackend.model.entities.TicketState;
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
@RequestMapping("/ticket-state")
public class TicketStateRestController extends BaseRestController {
    private final static Logger LOGGER = LoggerFactory.getLogger(TicketStateRestController.class);
    private final TicketStateDao ticketStateDao;

    @Autowired
    public TicketStateRestController(TicketStateDao ticketStateDao) {
        super();
        this.ticketStateDao = ticketStateDao;
    }

    @GetMapping("/list")
    public ResponseEntity<Collection<TicketState>> list(
            @RequestParam(value = "startPosition", defaultValue = "0") String min,
            @RequestParam(value = "maxResult", defaultValue = "0") String max
    ) {
        Preconditions.checkNotNull(min, "startPosition est requis!");
        Preconditions.checkNotNull(max, "maxResult est requis!");
        return new ResponseEntity<Collection<TicketState>>(
                this.ticketStateDao.findAll(Utility.toInt(min), Utility.toInt(max)),
                HttpStatus.FOUND
        );
    }

    @GetMapping("/get")
    public ResponseEntity<TicketState> get(
            @RequestParam(value = "id", defaultValue = "-1") String id
    ){
        Preconditions.checkNotNull(id, "Identifiant est requis !");
        AtomicReference<TicketState> ticketState = new AtomicReference<>(new TicketState());
        this.ticketStateDao.findById(Utility.toLong(id)).ifPresent(ticketState::set);
        return new ResponseEntity<TicketState>(
                ticketState.get(),
                HttpStatus.FOUND
        );
    }

    @Transactional
    @PostMapping("/add")
    public ResponseEntity<TicketState> add(@RequestBody TicketState ticketState){
        Preconditions.checkNotNull(ticketState, "TicketState est requis !");
        final TicketState ticketStateSaved = this.ticketStateDao.save(ticketState);
        return new ResponseEntity<TicketState>(
                ticketStateSaved,
                HttpStatus.CREATED
        );
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> delete(@RequestParam(value = "id", defaultValue = "-1") String id){
        this.ticketStateDao.findById(Utility.toLong(id)).ifPresent(this.ticketStateDao::delete);
        return new ResponseEntity<Void>(HttpStatus.GONE);
    }
}
