package org.jpa.ticketmanagerbackend.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jpa.ticketmanagerbackend.model.common.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "ticket_state",
        indexes = {@Index(name = "i_ticket_state_name", columnList = "name", unique = true)}
)
@NamedQueries({ @NamedQuery(name = "allTicketStateQuery", query = "SELECT t FROM TicketState t"),
        @NamedQuery(name = "allTicketStateCountQuery", query = "SELECT COUNT(*) FROM TicketState"),
        @NamedQuery(name = "ticketStateByNameQuery", query = "SELECT t FROM TicketState t WHERE (t.name = :name)") })
public class TicketState extends BaseEntity {
    /**
     * Le nom du statut du ticket
     */
    private String name;

    /**
     * Constructeur caché
     */
    public TicketState() {
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void onPrePersist() {
        super.onPrePersist();
        this.setActive(true);
    }

    @Override
    protected void onPreUpdate() {
        super.onPreUpdate();
        this.setActive(true);
    }
}
