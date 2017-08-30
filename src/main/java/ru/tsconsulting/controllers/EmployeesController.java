package ru.tsconsulting.controllers;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.details.EmployeeDetails;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.errorHandling.DepartmentNotFoundException;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.errorHandling.*;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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

    @RequestMapping(path = "/{employeeId}/transfer", method = RequestMethod.POST)
    public Employee transfer(@PathVariable Long employeeId,
                             @RequestParam(value="newDepartmentId") Long newDepartmentId) {
        if (employeeRepository.findByDepartment_IdAndIsFiredIsFalse(employeeId) == null) {
            throw new EmployeeNotFoundException(employeeId);
        }
        if (departmentRepository.findByIdAndIsDismissedIsFalse(newDepartmentId) == null) {
            throw new DepartmentNotFoundException(newDepartmentId);
        }
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(employeeId);
        employee.setDepartment(departmentRepository.findByIdAndIsDismissedIsFalse(newDepartmentId));
        Employee result = employeeRepository.save(employee);
        return result;
    }

    @RequestMapping(path = "/{employeeId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEmployee(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(employeeId);
        if (employee != null) {
            employee.setFired(true);
            employeeRepository.save(employee);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public Employee createEmployee(@RequestBody EmployeeDetails employeeDetails) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Employee employee = new Employee(employeeDetails);
        if (employeeDetails.getGrade() != null) {
            if (gradeRepository.findById(employeeDetails.getGrade()) == null) {
                throw new GradeNotFoundException(employeeDetails.getGrade());
            } else {
                employee.setGrade(gradeRepository.findById(employeeDetails.getGrade()));
            }
        }
        if (employeeDetails.getPosition() != null) {
            if (positionRepository.findById(employeeDetails.getPosition()) == null) {
                throw new GradeNotFoundException(employeeDetails.getPosition());
            } else {
                employee.setPosition(positionRepository.findById(employeeDetails.getPosition()));
            }
        }
        if (employeeDetails.getDepartment() == null) {
            throw new DepartmentNotSpecifiedException();
        } else {
            Department department = departmentRepository.findByIdAndIsDismissedIsFalse(employeeDetails.getDepartment());
            if (department == null) {
                throw new DepartmentNotFoundException(employeeDetails.getDepartment());
            } else {
                employee.setDepartment(department);
            }
        }
        Employee result = employeeRepository.save(employee);
        entityManager.getTransaction().commit();
        return result;
    }

    @RequestMapping(path = "/{employeeId}", method = RequestMethod.GET)
    public Employee getEmployee(@PathVariable Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }
        return employee;
    }

    @RequestMapping(path="/{employeeId}/history",method = RequestMethod.GET)
    public List<Employee> getHistory(@PathVariable Long employeeId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditQuery query = reader.createQuery().forRevisionsOfEntity(Employee.class,
                true, false);
        query.add(AuditEntity.id().eq(employeeId));
        List<Employee> list = query.getResultList();
        entityManager.getTransaction().commit();
        return list;
    }

    @RequestMapping(path="/{employeeId}/edit",method = RequestMethod.POST)
    public Employee editEmployee(@PathVariable Long employeeId,
                                 @RequestParam(value = "newPositionId") Long newPositionId,
                                 @RequestParam(value = "newGrade") Long newGrade,
                                 @RequestParam(value = "newSalary") Long newSalary) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(employeeId);
        if (employee==null)throw new EmployeeNotFoundException(employeeId);
        Position position = positionRepository.findById(newPositionId);
        if (position==null)throw new PositionNotFoundException(employeeId);
        Grade grade = gradeRepository.findById(newGrade);
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

    @ExceptionHandler(DepartmentNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError departmentNotSpecified(DepartmentNotSpecifiedException e) {
        return new RestError(3, "Department was not specified");
    }
}
