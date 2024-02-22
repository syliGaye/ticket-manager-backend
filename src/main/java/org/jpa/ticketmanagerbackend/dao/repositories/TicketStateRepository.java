package org.jpa.ticketmanagerbackend.dao.repositories;

import org.jpa.ticketmanagerbackend.model.entities.TicketState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketStateRepository extends JpaRepository<TicketState, Long> {
    Optional<TicketState> findByName(String name);
}
