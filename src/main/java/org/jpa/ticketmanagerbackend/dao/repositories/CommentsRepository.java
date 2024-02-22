package org.jpa.ticketmanagerbackend.dao.repositories;

import org.jpa.ticketmanagerbackend.model.entities.Comments;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.Ticket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 */
public interface CommentsRepository extends JpaRepository<Comments, Long> {
    List<Comments> findAllByTicket(Ticket ticket, Pageable pageable);
    List<Comments> findAllByEmployee(Employee employee, Pageable pageable);
    List<Comments> findAllByTicketAndEmployee(Ticket ticket, Employee employee, Pageable pageable);
}
