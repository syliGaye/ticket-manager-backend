package org.jpa.ticketmanagerbackend.dao.domain;

import org.jpa.ticketmanagerbackend.dao.BaseDao;
import org.jpa.ticketmanagerbackend.model.entities.Comments;
import org.jpa.ticketmanagerbackend.model.entities.Employee;
import org.jpa.ticketmanagerbackend.model.entities.Ticket;

import java.util.List;

public interface CommentsDao extends BaseDao<Comments, Long> {
    List<Comments> findAllByTicket(Ticket ticket, int startPosition, int maxResult);
    List<Comments> findAllByEmployee(Employee employee, int startPosition, int maxResult);
    List<Comments> findAllByTicketAndEmployee(Ticket ticket, Employee employee, int startPosition, int maxResult);
}
