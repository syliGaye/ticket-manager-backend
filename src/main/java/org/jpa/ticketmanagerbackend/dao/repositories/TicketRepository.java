package org.jpa.ticketmanagerbackend.dao.repositories;

import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.Ticket;
import org.jpa.ticketmanagerbackend.model.entities.TicketState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    Optional<Ticket> findByNumber(String number);
    List<Ticket> findAllByTicketState(TicketState ticketState, Pageable pageable);
    List<Ticket> findAllByCreateBy(Employee createBy, Pageable pageable);
    List<Ticket> findAllByAssignedTo(Employee assignedTo, Pageable pageable);
}
