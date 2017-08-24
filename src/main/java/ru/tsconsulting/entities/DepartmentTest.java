package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
    private Long parentId;

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

    public Long getParentId() {
        return parentId;
    }

    public Set<EmployeeTest> getEmployees() {
        return employees;
    }
}
