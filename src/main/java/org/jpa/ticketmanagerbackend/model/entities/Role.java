package org.jpa.ticketmanagerbackend.model.entities;

import org.jpa.ticketmanagerbackend.model.common.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "role", indexes = {@Index(name = "i_role_name", columnList = "name", unique = true)})
@NamedQueries({ @NamedQuery(name = "allRoleQuery", query = "SELECT a FROM Role a"),
        @NamedQuery(name = "allRoleCountQuery", query = "SELECT COUNT(*) FROM Role"),
        @NamedQuery(name = "roleByNameQuery", query = "SELECT a FROM Role a WHERE (a.name = :name)")})
public class Role extends BaseEntity {
    private String name;
    private String description;

    /**
     * Constructeur caché
     */
    public Role() {
    }
    public Role(String name) {
        this.name = name;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
