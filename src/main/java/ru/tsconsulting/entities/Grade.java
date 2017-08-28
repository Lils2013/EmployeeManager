package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import java.util.Set;

@Entity
@Audited

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "GRADE_HIB_TEST", schema = "TEST_B", catalog = "")
public class Grade {

    @Id
    @GenericGenerator(name="incrementGenerator1" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator1")
    private long id;

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy = "grade",fetch=FetchType.EAGER)
    private Set<Employee> employees;

    private String grade;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
