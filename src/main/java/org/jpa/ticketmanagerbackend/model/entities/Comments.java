package org.jpa.ticketmanagerbackend.model.entities;

import org.jpa.ticketmanagerbackend.model.common.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "comments", indexes = {
        @Index(name = "i_comments_ticket_id", columnList = "ticket_id"),
        @Index(name = "i_comments_employee_id", columnList = "employee_id")
})
@NamedQueries({ @NamedQuery(name = "allCommentsQuery", query = "SELECT a FROM Comments a"),
        @NamedQuery(name = "allCommentsCountQuery", query = "SELECT COUNT(*) FROM Comments"),
        @NamedQuery(name = "commentsByTicketQuery", query = "SELECT a FROM Comments a WHERE (a.ticket = :ticket)"),
        @NamedQuery(name = "commentsByEmployeeQuery", query = "SELECT a FROM Comments a WHERE (a.employee = :employee)"),
        @NamedQuery(name = "commentsByEmployeeAndTicketQuery", query = "SELECT a FROM Comments a WHERE (a.employee = :employee AND a.ticket = :ticket)")})
public class Comments
        extends BaseEntity {
    private String label;
    private Ticket ticket;
    private Employee employee;

    public Comments() {
    }

    @Column(name = "label")
    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comments_ticket"))
    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false, foreignKey = @ForeignKey(name = "fk_comments_employee"))
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
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
