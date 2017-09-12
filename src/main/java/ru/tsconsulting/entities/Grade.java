package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Audited

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "GRADE")
public class Grade {
    @Id
	@SequenceGenerator(name = "gradeGenerator", sequenceName = "grade_sequence",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gradeGenerator")
    private Long id;
    @JsonIgnore
    @NotAudited
  //  @OneToMany(mappedBy = "grade",fetch=FetchType.EAGER)
    @OneToMany(mappedBy = "grade")
    private Set<Employee> employees;

	@NotNull
	@Size(min = 1, max = 2)
    private String grade;

	public Grade() {
	}

	public Grade(GradeDetails gradeDetails) {
		setGrade(gradeDetails.getGrade());
	}

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

	public static class GradeDetails {

		@NotNull(message = "Grade cannot be null.")
		@Size(min = 1, max = 2, message = "Invalid size of grade string: must be between 1 and 2.")
	    private String grade;

	    public GradeDetails() {
	    }

	    public String getGrade() {
	        return grade;
	    }

	    public void setGrade(String grade) {
	        this.grade = grade;
	    }

	    @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;

	        GradeDetails that = (GradeDetails) o;

	        return grade != null ? grade.equals(that.grade) : that.grade == null;
	    }

	    @Override
	    public int hashCode() {
	        return grade != null ? grade.hashCode() : 0;
	    }
	}
}
