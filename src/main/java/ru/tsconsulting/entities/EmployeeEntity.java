package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import javax.persistence.*;
import java.util.Date;

@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "EMPLOYEE_HIB_TEST", schema = "TEST_B")
public class EmployeeEntity {

    public EmployeeEntity() {}
    @Id
    @GenericGenerator(name="incrementGenerator1" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator1")
    private long id;
    private String firstname;
    private String lastname;
    private String middlename;
    private String gender;

    @JsonFormat(pattern="yyyy-MM-dd")
    private Date birthdate;

    @ManyToOne(fetch=FetchType.EAGER)
    private PositionEntity position;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER)
    private DepartmentEntity department;

    @ManyToOne(fetch=FetchType.EAGER)
    private GradeEntity grade;
    private Long salary;

    @JsonGetter("department_id")
    public Long getDepartmentId() {
        if (department == null) {
            return null;
        }
        return department.getId();
    }

    public long getId() {
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

    public Date getBirthdate() {
        return birthdate;
    }

    public DepartmentEntity getDepartment() {
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

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    public void setDepartment(DepartmentEntity department) {
        this.department = department;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PositionEntity getPosition() {
        return position;
    }

    public void setPosition(PositionEntity position) {
        this.position = position;
    }

    public GradeEntity getGrade() {
        return grade;
    }

    public void setGrade(GradeEntity grade) {
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "EmployeeEntity{" +
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
