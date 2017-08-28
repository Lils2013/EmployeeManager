package ru.tsconsulting.entities;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.RevisionEntity;
import org.hibernate.envers.RevisionListener;
import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import ru.tsconsulting.EmployeeListener;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@RevisionEntity(EmployeeListener.class)
@Table(name = "EMPLOYEE_REVINFO_HIB_TEST", schema = "TEST_B")
public class EmployeeRevEntity {

    @Id
    @GenericGenerator(name="incrementGenerator3" , strategy="increment")
    @GeneratedValue(generator="incrementGenerator3")
    @RevisionNumber
    private int id;

    @RevisionTimestamp
    private long timestamp;

}
