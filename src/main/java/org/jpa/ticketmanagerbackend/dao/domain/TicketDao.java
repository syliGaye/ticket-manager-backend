package org.jpa.ticketmanagerbackend.dao.domain;

import org.jpa.ticketmanagerbackend.dao.BaseDao;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.Ticket;
import org.jpa.ticketmanagerbackend.model.entities.TicketState;

import java.util.List;
import java.util.Optional;

public interface TicketDao extends BaseDao<Ticket, Long> {
    Optional<Ticket> findByNumber(String number);
    List<Ticket> findAllByTicketState(TicketState ticketState, int startPosition, int maxResult);
    List<Ticket> findAllByCreateBy(Employee createBy, int startPosition, int maxResult);
    List<Ticket> findAllByAssignedTo(Employee assignedTo, int startPosition, int maxResult);
}
