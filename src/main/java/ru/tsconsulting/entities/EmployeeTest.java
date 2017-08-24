package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Time;

@Entity
public class EmployeeTest {

    public EmployeeTest() {}
    @Id
    @GeneratedValue
    private long id;
    private String firstname;
    private String lastname;
    private String middlename;
    private String gender;
    private Time birthdate;

    @JsonIgnore
    @ManyToOne
    private DepartmentTest department;
    private String grade;
    private Long salary;

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

    public Time getBirthdate() {
        return birthdate;
    }

    public DepartmentTest getDepartment() {
        return department;
    }

    public String getGrade() {
        return grade;
    }

    public Long getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "EmployeeTest{" +
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
