package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import ru.tsconsulting.details.GradeDetails;

import javax.persistence.*;
import java.util.Set;

@Entity
@Audited

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "GRADE", schema = "TEST_B", catalog = "")
public class Grade {

    @Id
    @GenericGenerator(name="incrementGenerator1" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator1")
    private Long id;

    @JsonIgnore
    @NotAudited
  //  @OneToMany(mappedBy = "grade",fetch=FetchType.EAGER)
    @OneToMany(mappedBy = "grade")
    private Set<Employee> employees;

    private String grade;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Grade() {
    }

    public Grade(GradeDetails gradeDetails) {

        setGrade(gradeDetails.getGrade());
    }
}
