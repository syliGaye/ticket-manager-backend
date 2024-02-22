package org.jpa.ticketmanagerbackend.dao.domain;

import org.jpa.ticketmanagerbackend.dao.BaseDao;
import org.jpa.ticketmanagerbackend.model.entities.TicketState;

import java.util.Optional;

public interface TicketStateDao extends BaseDao<TicketState, Long> {
    Optional<TicketState> findByName(String name);
}
