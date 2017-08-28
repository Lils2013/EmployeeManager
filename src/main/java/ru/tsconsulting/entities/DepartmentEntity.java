package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
@Entity
@Audited

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "DEPARTMENT_HIB_TEST", schema = "TEST_B")
public class DepartmentEntity {

    public DepartmentEntity() {}
    @Id
    @GenericGenerator(name="incrementGenerator2" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator2")
    private long id;
    private String name;
    private String chiefId;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER)
    private DepartmentEntity parent;

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy="parent",fetch=FetchType.EAGER)
    private Set<DepartmentEntity> childDepartments = new HashSet<>();

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy = "department",fetch=FetchType.EAGER)
    private Set<EmployeeEntity> employees = new HashSet<>();

    @JsonGetter("parent_id")
    public Long getDepartmentId() {
        if (parent == null) {
            return null;
        }
        return parent.getId();
    }

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

    public DepartmentEntity getParent() {
        return parent;
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

    public void setParent(DepartmentEntity parent) {
        this.parent = parent;
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
