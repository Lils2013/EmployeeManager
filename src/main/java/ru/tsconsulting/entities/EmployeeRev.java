package ru.tsconsulting.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionListener;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import ru.tsconsulting.EmployeeListener;

import javax.persistence.*;

@Entity
@RevisionEntity(EmployeeListener.class)
@Table(name = "EMPLOYEE_REVINFO")
public class EmployeeRev {
    @Id
    @SequenceGenerator(name = "employeeRevGenerator", sequenceName = "employee_rev_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "employeeRevGenerator")
    @RevisionNumber
    private int id;
    @RevisionTimestamp
    private long timestamp;

}
