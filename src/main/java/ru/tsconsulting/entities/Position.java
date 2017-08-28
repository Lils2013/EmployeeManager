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
@Table(name = "POSITION_HIB_TEST", schema = "TEST_B")
public class Position {

    @Id
    @GenericGenerator(name="incrementGenerator1" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator1")
    private long id;

    @JsonIgnore
    @NotAudited
    @OneToMany(mappedBy = "position",fetch=FetchType.EAGER)
    private Set<Employee> employees;

    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
