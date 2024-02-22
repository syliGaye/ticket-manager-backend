package org.jpa.ticketmanagerbackend.model.entities;

import org.jpa.ticketmanagerbackend.model.common.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "department",
        indexes = {@Index(name = "i_department_name", columnList = "name", unique = true)}
)
@NamedQueries({ @NamedQuery(name = "allDepartmentQuery", query = "SELECT a FROM Department a"),
        @NamedQuery(name = "allDepartmentCountQuery", query = "SELECT COUNT(*) FROM Department"),
        @NamedQuery(name = "departmentByNameQuery", query = "SELECT a FROM Department a WHERE (a.name = :name)") })
public class Department extends BaseEntity {
    private String name;

    public Department() {
    }

    public Department(String name) {
        this.name = name;
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
