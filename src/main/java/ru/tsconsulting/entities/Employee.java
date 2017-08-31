package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import ru.tsconsulting.details.EmployeeDetails;
import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "EMPLOYEE_HIB_TEST", schema = "TEST_B")
public class Employee {

    public Employee() {}
    public Employee(EmployeeDetails employeeDetails) {
        setFirstname(employeeDetails.getFirstname());
        setLastname(employeeDetails.getLastname());
        setMiddlename(employeeDetails.getMiddlename());
        setGender(employeeDetails.getGender());
        setBirthdate(employeeDetails.getBirthdate());
        setSalary(employeeDetails.getSalary());
    }
    @Id
    @GenericGenerator(name="incrementGenerator1" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator1")
    private Long id;
    private String firstname;
    private String lastname;
    private String middlename;
    private String gender;
    private Boolean isFired = false;

    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(columnDefinition = "DATE")
    private LocalDate birthdate;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER)
    private Position position;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER)
    private Department department;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER)
    private Grade grade;
    private Long salary;

    @JsonGetter("department_id")
    public Long getDepartmentId() {
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

    public Long getSalary() {
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

    public void setSalary(Long salary) {
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
}
