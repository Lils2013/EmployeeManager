package ru.tsconsulting.entities;

import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "EMPLOYEE_HISTORY", schema = "TEST_B")
public class EmployeeHistory {

    public EmployeeHistory(){}
    @Id
    @GenericGenerator(name="incrementGenerator2" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator2")
    private Long id;

    private LocalDateTime dateTime;

    private Long operationId;

    private Long employeeId;

    private String ipAddress;

    private Boolean isSuccessful;

    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
}