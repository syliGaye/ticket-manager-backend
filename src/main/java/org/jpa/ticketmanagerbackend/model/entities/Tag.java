package org.jpa.ticketmanagerbackend.model.entities;

import org.jpa.ticketmanagerbackend.model.common.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "tag", indexes = {@Index(name = "i_tag_label", columnList = "label", unique = true)})
@NamedQueries({ @NamedQuery(name = "allTagQuery", query = "SELECT t FROM Tag t"),
        @NamedQuery(name = "allTagCountQuery", query = "SELECT COUNT(*) FROM Tag"),
        @NamedQuery(name = "tagByLabelQuery", query = "SELECT t FROM Tag t WHERE (t.label = :label)") })
public class Tag extends BaseEntity {
    /**
     * Le nom du tag
     */
    private String label;

    /**
     * Constructeur caché
     */
    public Tag() {
    }

    /**
     *
     * @return
     */
    @Column(name = "label", nullable = false)
    public String getLabel() {
        return label;
    }

    /**
     *
     * @param libelle
     */
    public void setLabel(String libelle) {
        this.label = libelle;
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
