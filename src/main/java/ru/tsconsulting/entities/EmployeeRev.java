package ru.tsconsulting.entities;

import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import ru.tsconsulting.listeners.EmployeeListener;

import javax.persistence.*;

@Entity
@RevisionEntity(EmployeeListener.class)
@Table(name = "EMPLOYEE_REVINFO")
public class EmployeeRev {
    @Id
    @SequenceGenerator(name = "employeeRevGenerator", sequenceName = "employee_rev_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeRevGenerator")
    @RevisionNumber
    private int id;
    @RevisionTimestamp
    private long timestamp;

}
