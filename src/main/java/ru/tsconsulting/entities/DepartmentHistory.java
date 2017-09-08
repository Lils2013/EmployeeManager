package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DEPARTMENT_HISTORY")
public class DepartmentHistory {
    @Id
    @SequenceGenerator(name = "departmentHistoryGenerator", sequenceName = "department_history_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "departmentHistoryGenerator")
    private Long id;
    @JsonFormat(pattern="yyyy-MM-dd hh:mm:ss")
    private LocalDateTime dateTime;
    @Column(name = "OPERATION_ID")
    private Long operationId;
    @Column(name = "DEPARTMENT_ID")
    private Long departmentId;
    @Column(name = "IP_ADDRESS")
    private String ipAddress;
    @Column(name = "IS_SUCCESSFUL")
    private Boolean isSuccessful;

    public Boolean getIsSuccessful() {
        return isSuccessful;
    }

    public void setIsSuccessful(Boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
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

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAdress) {
        this.ipAddress = ipAdress;
    }
}
