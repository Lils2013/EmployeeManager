package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Audited
@Table(name = "DEPARTMENT")
public class Department {
    @Id
    @Access(AccessType.PROPERTY)
    @SequenceGenerator(name = "departmentGenerator", sequenceName = "department_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departmentGenerator")
    private Long id;
    private String name;

    @JsonIgnore
    @NotAudited
    @ManyToOne
    @JoinColumn(name = "CHIEF_ID")
    private Employee chief;

    @Column(name = "IS_DISMISSED")
    private Boolean isDismissed = false;

    @JsonIgnore
    @JoinColumn(name = "PARENT_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Department parent;

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<Department> childDepartments = new HashSet<>();

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private Set<Employee> employees = new HashSet<>();

    public Department() {
    }

    public Department(DepartmentDetails departmentDetails) {
        setName(departmentDetails.getName());
    }

    @JsonGetter("parent_id")
    public Long getDepartmentId() {
        if (parent == null) {
            return null;
        }
        return parent.getId();
    }

    @JsonGetter("chief_id")
    public Long getChiefId() {
        if (parent == null) {
            return null;
        }
        return parent.getId();
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Employee getChief() {
        return chief;
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setChief(Employee chief) {
        this.chief = chief;
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

    public Boolean isDismissed() {
        return isDismissed;
    }

    public void setDismissed(Boolean dismissed) {
        isDismissed = dismissed;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", chiefId='" + chief + '\'' +
                '}';
    }

    @ApiModel(value = "DepartmentDetails", description = "data for creating a new department")
    public static class DepartmentDetails {

        @NotNull(message = "Department name cannot be null.")
        @ApiModelProperty(value = "department name, string with size between 1 and 64", example="Development",
                required = true)
        @Size(min = 1, max = 64, message = "Invalid size of name string: must be between 1 and 64.")
        private String name;

        @ApiModelProperty(value = "id of chief, positive integer", example="1")
        private Long chiefId;

        @ApiModelProperty(value = "id of parent department, positive integer", example="1")
        private Long parent;

        public DepartmentDetails() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Long getChiefId() {
            return chiefId;
        }

        public void setChiefId(Long chiefId) {
            this.chiefId = chiefId;
        }

        public Long getParent() {
            return parent;
        }

        public void setParent(Long parent) {
            this.parent = parent;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DepartmentDetails that = (DepartmentDetails) o;

            if (name != null ? !name.equals(that.name) : that.name != null) return false;
            if (chiefId != null ? !chiefId.equals(that.chiefId) : that.chiefId != null) return false;
            return parent != null ? parent.equals(that.parent) : that.parent == null;
        }

        @Override
        public int hashCode() {
            int result = name != null ? name.hashCode() : 0;
            result = 31 * result + (chiefId != null ? chiefId.hashCode() : 0);
            result = 31 * result + (parent != null ? parent.hashCode() : 0);
            return result;
        }
    }
}
