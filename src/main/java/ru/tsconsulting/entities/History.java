package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "HISTORY", schema = "TEST_B")
public class History {

    public History(){}
    @Id
    @GenericGenerator(name="incrementGenerator2" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator2")
    private Long id;

    private LocalDateTime dateTime;

    private Long operationId;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER)
    private Department department;

    @JsonIgnore
    @ManyToOne(fetch=FetchType.EAGER)
    private Employee employee;

    private String ipAdress;

    public String getIpAdress() {
        return ipAdress;
    }

    public void setIpAdress(String ipAdress) {
        this.ipAdress = ipAdress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Long getOperationId() {
        return operationId;
    }

    public void setOperationId(Long operationId) {
        this.operationId = operationId;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}