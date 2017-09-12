package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "EMPLOYEE")
public class Employee {
    @Id
    @SequenceGenerator(name = "employeeGenerator", sequenceName = "employee_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeGenerator")
    private Long id;


    private String firstname;
    private String lastname;
    private String middlename;
    private String gender;
    @Column(name = "IS_FIRED")
    private Boolean isFired = false;
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(columnDefinition = "DATE")
    private LocalDate birthdate;
    @JsonIgnore
  //  @ManyToOne(fetch=FetchType.EAGER)
    @ManyToOne
    private Position position;
    @JsonIgnore
   // @ManyToOne(fetch=FetchType.EAGER)
    @ManyToOne
    private Department department;
    @JsonIgnore
   // @ManyToOne(fetch=FetchType.EAGER)
    @ManyToOne
    private Grade grade;
    @Column(name = "salary", precision = 20, scale = 2)
    private BigDecimal salary;

	public Employee() {}
	public Employee(EmployeeDetails employeeDetails) {
		setFirstname(employeeDetails.getFirstname());
		setLastname(employeeDetails.getLastname());
		setMiddlename(employeeDetails.getMiddlename());
		setGender(employeeDetails.getGender());
		setBirthdate(employeeDetails.getBirthdate());
		setSalary(employeeDetails.getSalary());
	}

    @JsonGetter("department_id")
    public Long jsonGetDepartmentId() {
        if (department == null) {
            return null;
        }
        return department.getId();
    }

    @JsonGetter("position_id")
    public Long getPositionId() {
        if (position == null) {
            return null;
        }
        return position.getId();
    }

    @JsonGetter("grade_id")
    public Long getGradeId() {
        if (grade == null) {
            return null;
        }
        return grade.getId();
    }

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getMiddlename() {
        return middlename;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Department getDepartment() {
        return department;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

    public Boolean isFired() {
        return isFired;
    }

    public void setFired(Boolean fired) {
        isFired = fired;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", middlename='" + middlename + '\'' +
                ", gender='" + gender + '\'' +
                ", birthdate=" + birthdate +
                ", grade='" + grade + '\'' +
                ", salary=" + salary +
                '}';
    }

    @ApiModel(value="EmployeeDetails", description="data for creating a new employee")
	public static class EmployeeDetails {

        @ApiModelProperty(required=true)
	    private String firstname;
        @ApiModelProperty(required=true)
	    private String lastname;
	    private String middlename;

        @ApiModelProperty(value = "gender, M or F, although in current implementation it can be any string value", example="M")
	    private String gender;

        @ApiModelProperty(value = "birth date in yyyy-MM-dd format", example="2008-10-27")
	    @JsonFormat(pattern="yyyy-MM-dd")
	    private LocalDate birthdate;

        @ApiModelProperty(value = "id of position", example="1")
	    private Long position;
        @ApiModelProperty(value = "id of department", example="1",required=true)
	    private Long department;
        @ApiModelProperty(value = "id of grade", example="1")
	    private Long grade;
	    private BigDecimal salary;

	    public EmployeeDetails() {
	    }

	    public String getFirstname() {
	        return firstname;
	    }

	    public void setFirstname(String firstname) {
	        this.firstname = firstname;
	    }

	    public String getLastname() {
	        return lastname;
	    }

	    public void setLastname(String lastname) {
	        this.lastname = lastname;
	    }

	    public String getMiddlename() {
	        return middlename;
	    }

	    public void setMiddlename(String middlename) {
	        this.middlename = middlename;
	    }

	    public String getGender() {
	        return gender;
	    }

	    public void setGender(String gender) {
	        this.gender = gender;
	    }

	    public LocalDate getBirthdate() {
	        return birthdate;
	    }

	    public void setBirthdate(LocalDate birthdate) {
	        this.birthdate = birthdate;
	    }

	    public Long getPosition() {
	        return position;
	    }

	    public void setPosition(Long position) {
	        this.position = position;
	    }

	    public Long getDepartment() {
	        return department;
	    }

	    public void setDepartment(Long department) {
	        this.department = department;
	    }

	    public BigDecimal getSalary() {
	        return salary;
	    }

	    public void setSalary(BigDecimal salary) {
	        this.salary = salary;
	    }

	    public Long getGrade() {
	        return grade;
	    }

	    public void setGrade(Long grade) {
	        this.grade = grade;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        EmployeeDetails that = (EmployeeDetails) o;

	        if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
	        if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
	        if (middlename != null ? !middlename.equals(that.middlename) : that.middlename != null) return false;
	        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
	        if (birthdate != null ? !birthdate.equals(that.birthdate) : that.birthdate != null) return false;
	        if (position != null ? !position.equals(that.position) : that.position != null) return false;
	        if (department != null ? !department.equals(that.department) : that.department != null) return false;
	        if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
	        return salary != null ? salary.equals(that.salary) : that.salary == null;
	    }

	    @Override
	    public int hashCode() {
	        int result = firstname != null ? firstname.hashCode() : 0;
	        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
	        result = 31 * result + (middlename != null ? middlename.hashCode() : 0);
	        result = 31 * result + (gender != null ? gender.hashCode() : 0);
	        result = 31 * result + (birthdate != null ? birthdate.hashCode() : 0);
	        result = 31 * result + (position != null ? position.hashCode() : 0);
	        result = 31 * result + (department != null ? department.hashCode() : 0);
	        result = 31 * result + (grade != null ? grade.hashCode() : 0);
	        result = 31 * result + (salary != null ? salary.hashCode() : 0);
	        return result;
	    }
	}
}
