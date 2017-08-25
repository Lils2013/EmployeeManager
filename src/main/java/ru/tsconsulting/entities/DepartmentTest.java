package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

@Entity
public class DepartmentTest {

    public DepartmentTest() {}
    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String chiefId;

    @JsonIgnore
    @ManyToOne(cascade={CascadeType.ALL},fetch=FetchType.EAGER)
    private DepartmentTest parentDepartment;

   // @JsonIgnore
    @OneToMany(mappedBy="parentDepartment",fetch=FetchType.EAGER)
    private Set<DepartmentTest> childDepartments = new HashSet<>();

    @JsonIgnore
    @OneToMany(cascade=ALL, mappedBy = "department")
    private Set<EmployeeTest> employees = new HashSet<>();

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getChiefId() {
        return chiefId;
    }

    public Set<EmployeeTest> getEmployees() {
        return employees;
    }

    public DepartmentTest getParentDepartment() {
        return parentDepartment;
    }

    public Set<DepartmentTest> getChildDepartments() {
        return childDepartments;
    }

    @Override
    public String toString() {
        return "DepartmentTest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chiefId='" + chiefId + '\'' +
                '}';
    }
}
