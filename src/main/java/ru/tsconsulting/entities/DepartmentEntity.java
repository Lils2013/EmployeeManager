package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name = "DEPARTMENT", schema = "TEST_B")
public class DepartmentEntity {

    public DepartmentEntity() {}
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String chiefId;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    private DepartmentEntity parentDepartment;

   // @JsonIgnore
    @OneToMany(mappedBy="parentDepartment",fetch=FetchType.EAGER)
    private Set<DepartmentEntity> childDepartments = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade=ALL, mappedBy = "department")
    private Set<EmployeeEntity> employees = new HashSet<>();

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChiefId() {
        return chiefId;
    }

    public Set<EmployeeEntity> getEmployees() {
        return employees;
    }

    public DepartmentEntity getParentDepartment() {
        return parentDepartment;
    }

    public Set<DepartmentEntity> getChildDepartments() {
        return childDepartments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChiefId(String chiefId) {
        this.chiefId = chiefId;
    }

    public void setParentDepartment(DepartmentEntity parentDepartment) {
        this.parentDepartment = parentDepartment;
    }

    public void setChildDepartments(Set<DepartmentEntity> childDepartments) {
        this.childDepartments = childDepartments;
    }

    public void setEmployees(Set<EmployeeEntity> employees) {
        this.employees = employees;
    }

    @Override
    public String toString() {
        return "DepartmentEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chiefId='" + chiefId + '\'' +
                '}';
    }
}
