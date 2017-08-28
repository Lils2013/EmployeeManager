package ru.tsconsulting.controllers;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.DepartmentEntity;
import ru.tsconsulting.entities.EmployeeEntity;
import ru.tsconsulting.errorHandling.DepartmentNotFoundException;
import ru.tsconsulting.entities.GradeEntity;
import ru.tsconsulting.entities.PositionEntity;
import ru.tsconsulting.errorHandling.*;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

@RestController

@RequestMapping("/employees")
public class EmployeesController {

    private final EntityManagerFactory entityManagerFactory;
    private final EmployeeRepository employeeRepository;
    private final PositionRepository positionRepository;
    private final GradeRepository gradeRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public EmployeesController(EntityManagerFactory entityManagerFactory,
                               EmployeeRepository employeeRepository,
                               PositionRepository positionRepository,
                               GradeRepository gradeRepository,
                               DepartmentRepository departmentRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.employeeRepository = employeeRepository;
        this.positionRepository = positionRepository;
        this.gradeRepository = gradeRepository;
        this.departmentRepository = departmentRepository;
    }

    @Transactional(Transactional.TxType.REQUIRED)
    @RequestMapping(path = "/{employeeId}/transfer", method = RequestMethod.POST)
    public EmployeeEntity transfer(@PathVariable Long employeeId,
                                        @RequestParam(value="newDepartmentId") long newDepartmentId) {
        if (departmentRepository.findById(newDepartmentId) == null) {
            throw new DepartmentNotFoundException(newDepartmentId);
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        EmployeeEntity employee = entityManager.find(EmployeeEntity.class,employeeId);
        employee.setDepartment(entityManager.find(DepartmentEntity.class,newDepartmentId));
        entityManager.getTransaction().commit();
        return employee;
    }

    @RequestMapping(path = "/{employeeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        EmployeeEntity employee = employeeRepository.findById(employeeId);
        if (employee != null) {
            employeeRepository.delete(employeeId);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public EmployeeEntity createEmployee(@RequestBody EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    @RequestMapping(path = "/{employeeId}", method = RequestMethod.GET)
    public EmployeeEntity selectEmployee(@PathVariable Long employeeId) {
        return employeeRepository.findById(employeeId);
    }

    @RequestMapping(path="/{employeeId}/history",method = RequestMethod.GET)
    public List<EmployeeEntity> getHistory(@PathVariable Long employeeId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditQuery query = reader.createQuery().forRevisionsOfEntity(EmployeeEntity.class,
                true, false);
        query.add(AuditEntity.id().eq(employeeId));
        List<EmployeeEntity> list = query.getResultList();
        entityManager.getTransaction().commit();
        return list;
    }

    @RequestMapping(path="/{employeeId}/edit",method = RequestMethod.POST)
    public EmployeeEntity editEmployee(@PathVariable Long employeeId,
                                       @RequestParam(value = "newPositionId") long newPositionId,
                                       @RequestParam(value = "newGrade") long newGrade,
                                       @RequestParam(value = "newSalary") long newSalary) {
        EmployeeEntity employee = employeeRepository.findById(employeeId);
        if (employee==null)throw new EmployeeNotFoundException(employeeId);
        PositionEntity position = positionRepository.findById(newPositionId);
        if (position==null)throw new PositionNotFoundException(employeeId);
        GradeEntity grade = gradeRepository.findById(newGrade);
        if (grade==null)throw new GradeNotFoundException(employeeId);
        employee.setPosition(position);
        employee.setGrade(grade);
        employee.setSalary(newSalary);
        employeeRepository.save(employee);
        return employee;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError entityNotFound(EntityNotFoundException e) {
        return new RestError(1, e.getMessage());
    }

}
