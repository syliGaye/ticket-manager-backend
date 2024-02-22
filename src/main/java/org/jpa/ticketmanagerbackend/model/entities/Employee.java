package org.jpa.ticketmanagerbackend.model.entities;

import org.jpa.ticketmanagerbackend.model.common.BaseEntity;

import javax.persistence.*;

@Entity
@Table(name = "employee", indexes = {
        @Index(name = "i_employee_department_id", columnList = "department_id")
})
@NamedQueries({ @NamedQuery(name = "allEmployeeQuery", query = "SELECT a FROM Employee a"),
        @NamedQuery(name = "allEmployeeCountQuery", query = "SELECT COUNT(*) FROM Employee"),
        @NamedQuery(name = "employeeByDepartmentQuery", query = "SELECT a FROM Employee a WHERE (a.department = :department)") })
public class Employee extends BaseEntity {
    private String name;
    private Department department;

    public Employee() {
    }

    public Employee(String name, Department department) {
        this.name = name;
        this.department = department;
    }

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_employee_department"))
    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
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
