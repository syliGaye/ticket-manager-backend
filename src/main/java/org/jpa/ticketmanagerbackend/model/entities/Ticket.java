package org.jpa.ticketmanagerbackend.model.entities;

import org.jpa.ticketmanagerbackend.model.common.BaseEntity;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un ticket.
 * Chaque ticket est associé à un numéro unique et possède des informations telles que la description, le tag,
 * l'utilisateur qui l'a créé (creePar) et l'utilisateur auquel il est assigné (assigneA).
 */
@Entity
@Table(
        name = "ticket",
        indexes = {@Index(
                name = "i_ticket_number",
                columnList = "number",
                unique = true),
                @Index(name = "i_ticket_ticket_state_id", columnList = "ticket_state_id"),
                @Index(name = "i_ticket_employee_create_by_id", columnList = "create_by_id"),
                @Index(name = "i_ticket_employee_assigned_to_id", columnList = "assigned_to_id")
        })
@NamedQueries({ @NamedQuery(name = "allTicketQuery", query = "SELECT t FROM Ticket t"),
        @NamedQuery(name = "allTicketCountQuery", query = "SELECT COUNT(*) FROM Ticket"),
        @NamedQuery(name = "ticketByNumberQuery", query = "SELECT t FROM Ticket t WHERE (t.number = :number)"),
        @NamedQuery(name = "ticketByTicketStateQuery", query = "SELECT t FROM Ticket t WHERE (t.ticketState = :ticketState)"),
        @NamedQuery(name = "ticketByCreateByQuery", query = "SELECT t FROM Ticket t WHERE (t.createBy = :createBy)"),
        @NamedQuery(name = "ticketByAssignedToQuery", query = "SELECT t FROM Ticket t WHERE (t.assignedTo = :assignedTo)")})
public class Ticket extends BaseEntity {

    /**
     * Le numéro unique du ticket.
     */
    private String number;

    /**
     * Le titre du ticket.
     */
    private String title;

    /**
     * La date de création du ticket
     */
    private Date createDate;

    /**
     * La description du ticket.
     */
    private String description;

    /**
     * Le statut pris par le ticket à un moment.
     */
    private TicketState ticketState;

    /**
     * L'utilisateur qui crée et assigne un ticket.
     */
    private Employee createBy;

    /**
     * L'utilisateur à qui le ticket a été assigné.
     */
    private Employee assignedTo;

    private Set<Tag> tags = new HashSet<>();

    /**
     * Constructeur par défaut de la classe Ticket.
     */
    public Ticket() {
        super();
    }

    /**
     * Obtient le numéro unique du ticket.
     *
     * @return Le numéro unique du ticket.
     */
    @Column(name = "number", nullable = false)
    public String getNumber() {
        return number;
    }

    /**
     * Définit le numéro unique du ticket.
     *
     * @param numero Le nouveau numéro unique du ticket.
     */
    public void setNumber(String numero) {
        this.number = numero;
    }

    @Column(name = "title", nullable = false)
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Obtient la description du ticket.
     *
     * @return La description du ticket.
     */
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    /**
     * Définit la description du ticket.
     *
     * @param description La nouvelle description du ticket.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Obtient le statut pris par le ticket.
     *
     * @return Le statut pris par le ticket.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_state_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_ticket_state"))
    public TicketState getTicketState() {
        return ticketState;
    }

    /**
     * Définit le statut pris par le ticket.
     *
     * @param ticketState Le nouveau statut pris par le ticket.
     */
    public void setTicketState(TicketState ticketState) {
        this.ticketState = ticketState;
    }

    /**
     * Obtient l'utilisateur qui a créé et assigné le ticket.
     *
     * @return L'utilisateur qui a créé et assigné le ticket.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "create_by_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_employee_create_by"))
    public Employee getCreateBy() {
        return createBy;
    }

    /**
     * Définit l'utilisateur qui a créé et assigné le ticket.
     *
     * @param createBy Le nouvel utilisateur qui a créé et assigné le ticket.
     */
    public void setCreateBy(Employee createBy) {
        this.createBy = createBy;
    }

    /**
     * Obtient l'utilisateur auquel le ticket est assigné.
     *
     * @return L'utilisateur auquel le ticket est assigné.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id", nullable = false, foreignKey = @ForeignKey(name = "fk_ticket_employee_assigned_to"))
    public Employee getAssignedTo() {
        return assignedTo;
    }

    /**
     * Définit l'utilisateur auquel le ticket est assigné.
     *
     * @param assignedTo Le nouvel utilisateur auquel le ticket est assigné.
     */
    public void setAssignedTo(Employee assignedTo) {
        this.assignedTo = assignedTo;
    }

    @ManyToMany(targetEntity = Tag.class, fetch = FetchType.EAGER)
    @JoinTable(name = "ticket_tag",
            joinColumns = {@JoinColumn(name = "ticket_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")})
    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    @Column(name = "create_date", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
