package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import ru.tsconsulting.details.PositionDetails;

import javax.persistence.*;
import java.util.Set;

@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "POSITION", schema = "TEST_B")
public class Position {

    @Id
    @GenericGenerator(name="incrementGenerator1" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator1")
    private Long id;

    @JsonIgnore
    @NotAudited
   // @OneToMany(mappedBy = "position",fetch=FetchType.EAGER)
    @OneToMany(mappedBy = "position")
    private Set<Employee> employees;

    private String name;

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

    public Position() {
    }

    public Position(PositionDetails positionDetails) {
        setName(positionDetails.getName());
    }
}
