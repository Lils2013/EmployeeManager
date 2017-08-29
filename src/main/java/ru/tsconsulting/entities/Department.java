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
public class Department {

    public Department() {}
    @Id
    @GenericGenerator(name="incrementGenerator2" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator2")
    private long id;
    private String name;
    private String chiefId;
    private boolean isDismissed = false;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER)
    private Department parent;

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy="parent",fetch=FetchType.EAGER)
    private Set<Department> childDepartments = new HashSet<>();

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy = "department",fetch=FetchType.EAGER)
    private Set<Employee> employees = new HashSet<>();

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

    public Set<Employee> getEmployees() {
        return employees;
    }

    public Department getParent() {
        return parent;
    }

    public Set<Department> getChildDepartments() {
        return childDepartments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChiefId(String chiefId) {
        this.chiefId = chiefId;
    }

    public void setParent(Department parent) {
        this.parent = parent;
    }

    public void setChildDepartments(Set<Department> childDepartments) {
        this.childDepartments = childDepartments;
    }

    public void setEmployees(Set<Employee> employees) {
        this.employees = employees;
    }

    public boolean isDismissed() {
        return isDismissed;
    }

    public void setDismissed(boolean dismissed) {
        isDismissed = dismissed;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chiefId='" + chiefId + '\'' +
                '}';
    }
}
