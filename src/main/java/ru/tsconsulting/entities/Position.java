package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Audited
@Table(name = "POSITION")

public class Position {
    @Id
	@Access(AccessType.PROPERTY)
	@SequenceGenerator(name = "positionGenerator", sequenceName = "position_sequence",
			allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "positionGenerator")
    private Long id;
    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy = "position", fetch = FetchType.LAZY)
    private Set<Employee> employees;

    @NotNull
    @Size(min = 1, max = 32)
    @Column(unique = true)
    private String name;

	public Position() {
	}

	public Position(PositionDetails positionDetails) {
		setName(positionDetails.getName());
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public static class PositionDetails {

		@NotNull(message = "Position name cannot be null.")
		@Size(min = 1, max = 32, message = "Invalid size of position name string: must be between 1 and 32.")
        @ApiModelProperty(value = "grade name, string with size between 1 and 32", example="Director",
                required = true)
	    private String name;

	    public PositionDetails() {
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

        @Override
        public String toString() {
            return "PositionDetails{" +
                    "name='" + name + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            PositionDetails that = (PositionDetails) o;

            return name != null ? name.equals(that.name) : that.name == null;
        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }
}
