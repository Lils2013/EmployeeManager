package ru.tsconsulting.entities;

import javax.persistence.*;
import java.sql.Time;

/**
 * Created by avtsoy on 22.08.2017.
 */
@Entity
@Table(name = "EMPLOYEE", schema = "TEST_B", catalog = "")
public class EmployeeEntity {
    private long id;
    private String firstname;
    private String lastname;
    private String middlename;
    private String gender;
    private Time birthdate;
    private Long departmentId;
    private String grade;
    private Long salary;

    @Id
    @Column(name = "ID")
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "FIRSTNAME")
    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    @Basic
    @Column(name = "LASTNAME")
    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    @Basic
    @Column(name = "MIDDLENAME")
    public String getMiddlename() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename = middlename;
    }

    @Basic
    @Column(name = "GENDER")
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

//    @Temporal(TemporalType.TIME)
    @Basic
    @Column(name = "BIRTHDATE")
    public Time getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Time birthdate) {
        this.birthdate = birthdate;
    }

    @Basic
    @Column(name = "DEPARTMENT_ID")
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @Basic
    @Column(name = "GRADE")
    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    @Basic
    @Column(name = "SALARY")
    public Long getSalary() {
        return salary;
    }

    public void setSalary(Long salary) {
        this.salary = salary;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EmployeeEntity that = (EmployeeEntity) o;

        if (id != that.id) return false;
        if (firstname != null ? !firstname.equals(that.firstname) : that.firstname != null) return false;
        if (lastname != null ? !lastname.equals(that.lastname) : that.lastname != null) return false;
        if (middlename != null ? !middlename.equals(that.middlename) : that.middlename != null) return false;
        if (gender != null ? !gender.equals(that.gender) : that.gender != null) return false;
        if (birthdate != null ? !birthdate.equals(that.birthdate) : that.birthdate != null) return false;
        if (departmentId != null ? !departmentId.equals(that.departmentId) : that.departmentId != null) return false;
        if (grade != null ? !grade.equals(that.grade) : that.grade != null) return false;
        if (salary != null ? !salary.equals(that.salary) : that.salary != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (middlename != null ? middlename.hashCode() : 0);
        result = 31 * result + (gender != null ? gender.hashCode() : 0);
        result = 31 * result + (birthdate != null ? birthdate.hashCode() : 0);
        result = 31 * result + (departmentId != null ? departmentId.hashCode() : 0);
        result = 31 * result + (grade != null ? grade.hashCode() : 0);
        result = 31 * result + (salary != null ? salary.hashCode() : 0);
        return result;
    }
}
