package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.envers.Audited;
import ru.tsconsulting.entities.deserialization.EmployeeDeserializer;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Audited
@Table(name = "EMPLOYEE")
@JsonDeserialize(using = EmployeeDeserializer.class)
public class Employee {
    @Id
    @SequenceGenerator(name = "employeeGenerator", sequenceName = "employee_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeGenerator")
    private Long id;

    @ApiModelProperty(value = "firstname, string with size between 1 and 32", example="John", required=true)
    @NotNull(message = "Firstname cannot be null.")
    @Size(min = 1, max = 32, message = "Invalid size of firstname string: must be between 1 and 32.")
    private String firstname;

    @ApiModelProperty(value = "lastname, string with size between 1 and 32", example="Doe", required=true)
    @NotNull(message = "Lastname cannot be null.")
    @Size(min = 1, max = 32, message = "Invalid size of lastname string: must be between 1 and 32.")
    private String lastname;


    @ApiModelProperty(value = "middlename, string with size between 1 and 32", example="Smith", required=true)
    @Size(min = 1, max = 32, message = "Invalid size of middlename string: must be between 1 and 32.")
    private String middlename;

//    @ApiModelProperty(value = "gender, M or F",
//            example="M", allowableValues = "M,m,F,f")
//    @Pattern(regexp="^([MmFf])$",message="Invalid gender.")
    @Enumerated(EnumType.ORDINAL)
    private Gender gender;

    @Column(name = "IS_FIRED")
    private Boolean isFired = false;

    @ApiModelProperty(value = "birth date in yyyy-MM-dd format", example="2008-10-27")
    @JsonFormat(pattern="yyyy-MM-dd")
    @Column(columnDefinition = "DATE")
    private LocalDate birthdate;

    @JsonIgnore

    @ManyToOne(fetch = FetchType.LAZY)
    private Position position;

    @NotNull
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Grade grade;

    @ApiModelProperty(value = "non-negative decimal number with precision = 14 and scale = 2", example="123.45")
    @DecimalMin("0.00")
    @Digits(integer=14, fraction=2, message = "The integer and the fraction should be less than or equal to 14 and 2 respectively.")
    @Column(name = "salary", precision = 14, scale = 2)
    private BigDecimal salary;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must contain only latin alphanumeric characters.")
    @Size(min = 1, max = 32)
    @Column(unique = true)
    @JsonProperty(required = true)
    private String username;

    @NotNull
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Password must contain only latin alphanumeric characters.")
    @Size(min = 1, max = 32)
    @JsonProperty(access = Access.WRITE_ONLY)
    private String password;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @JoinTable(name = "ROLES_LIST", joinColumns = @JoinColumn(name = "EMPLOYEE_ID"))
    @Column(name = "ROLE_ID", nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private Set<Role> roles = new HashSet<>();

    @NotNull(message = "Department cannot be null.")
    @ApiModelProperty(value = "id of department", example="1",required=true)
    @JsonGetter("department_id")
    public Long jsonGetDepartmentId() {
        if (department == null) {
            return null;
        }
        return department.getId();
    }

    @ApiModelProperty(value = "id of position", example="1")
    @JsonGetter("position_id")
    public Long getPositionId() {
        if (position == null) {
            return null;
        }
        return position.getId();
    }

    @ApiModelProperty(value = "id of grade", example="1")
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
    public void setId(Long id) {
        this.id = id;
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

    public Gender getGender() {
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

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setGender(String genderString) {
	    if (genderString == null) {
	        return;
        }
        if (Objects.equals(genderString.toLowerCase(), "m")) {
            setGender(Gender.M);
        } else if (Objects.equals(genderString.toLowerCase(), "f")) {
            setGender(Gender.F);
        }
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
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
