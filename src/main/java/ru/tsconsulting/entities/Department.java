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
@Table(name = "DEPARTMENT")
public class Department {
    @Id
    @GenericGenerator(name="incrementGenerator2" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator2")
    private Long id;
    private String name;
    @Column(name = "CHIEF_ID")
    private String chiefId;
    @Column(name = "IS_DISMISSED")
    private Boolean isDismissed = false;
    @JsonIgnore
    @JoinColumn(name = "PARENT_ID")
 //   @ManyToOne(fetch=FetchType.EAGER)
    @ManyToOne
    private Department parent;

    @JsonIgnore
    @NotAudited
  //  @OneToMany(mappedBy="parent",fetch=FetchType.EAGER)
    @OneToMany(mappedBy="parent")
    private Set<Department> childDepartments = new HashSet<>();

    @JsonIgnore
    @NotAudited
 //   @OneToMany(mappedBy = "department",fetch=FetchType.EAGER)
    @OneToMany(mappedBy = "department")
    private Set<Employee> employees = new HashSet<>();

	public Department() {}
	public Department(DepartmentDetails departmentDetails) {
		setChiefId(departmentDetails.getChiefId());
		setName(departmentDetails.getName());
	}

    @JsonGetter("parent_id")
    public Long getDepartmentId() {
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

    public void setId(Long id) {
        this.id = id;
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
                ", chiefId='" + chiefId + '\'' +
                '}';
    }

	public static class DepartmentDetails {
	    private String name;
	    private String chiefId;
	    private Long parent;

	    public DepartmentDetails() {}

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getChiefId() {
	        return chiefId;
	    }

	    public void setChiefId(String chiefId) {
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
