package ru.tsconsulting.controllers;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.DepartmentEntity;
import ru.tsconsulting.entities.EmployeeEntity;
import ru.tsconsulting.errorHandling.DepartmentNotFoundException;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

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

    @RequestMapping(path="/{employeeId}/transfer",method = RequestMethod.POST)
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

    @RequestMapping(path="/{employeeId}",method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        employeeRepository.delete(employeeId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public EmployeeEntity createEmployee(@RequestBody EmployeeEntity employee){
        return employeeRepository.save(employee);
    }

    @RequestMapping(path="/{employeeId}",method = RequestMethod.GET)
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
        List list = query.getResultList();
        List<EmployeeEntity> result = new ArrayList<>();
        for (Object e : list) {
            if (e instanceof EmployeeEntity) {
                EmployeeEntity employeeEntity = (EmployeeEntity) e;
                System.out.println(employeeEntity);
                if (employeeEntity.getId() == employeeId) {
                    result.add(employeeEntity);
                }
            }
        }
        entityManager.getTransaction().commit();
        return result;
    }

    @RequestMapping(path="/{employeeId}/edit",method = RequestMethod.POST)
    public EmployeeEntity editEmployee(@PathVariable Long employeeId,
                                       @RequestParam(value="newPositionId") long newPositionId,
                                       @RequestParam(value="newGrade") long newGrade,
                                       @RequestParam(value="newSalary") long newSalary) {
        EmployeeEntity current = employeeRepository.findById(employeeId);
        current.setPosition(positionRepository.findById(newPositionId));
        current.setGrade(gradeRepository.findById(newGrade));
        current.setSalary(newSalary);
        employeeRepository.save(current);
        return current;
    }
}
