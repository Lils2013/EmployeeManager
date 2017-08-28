package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.errorHandling.DepartmentIsNotEmptyException;
import ru.tsconsulting.errorHandling.DepartmentNotFoundException;
import ru.tsconsulting.errorHandling.EntityNotFoundException;
import ru.tsconsulting.errorHandling.RestError;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@RestController
@RequestMapping("/departments")
public class DepartmentsController {

    private final DepartmentRepository departmentRepository;

    private final EntityManagerFactory entityManagerFactory;

    private final EmployeeRepository employeeRepository;


    @Autowired
    public DepartmentsController(DepartmentRepository departmentRepository,
                                 EntityManagerFactory entityManagerFactory,
                                 EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.entityManagerFactory = entityManagerFactory;
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping(path = "/{depId}/changeHierarchy", method = RequestMethod.POST)
    public Department changeHierarchy(@PathVariable Long depId,
                                      @RequestParam(value = "newHeadDepartmentId") Long newHeadDepartmentId) {
        if (departmentRepository.findById(depId) == null) {
            throw new DepartmentNotFoundException(depId);
        }
        if (departmentRepository.findById(newHeadDepartmentId) == null) {
            throw new DepartmentNotFoundException(newHeadDepartmentId);
        }
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Department original = entityManager.find(Department.class, depId);
        original.setParent(entityManager.find(Department.class, newHeadDepartmentId));
        entityManager.getTransaction().commit();
        return original;
    }

    @RequestMapping(path = "/{depId}/employees", method = RequestMethod.GET)
    public List<Employee> employeeByDep(@PathVariable Long depId) {
        if (departmentRepository.findById(depId) != null) {
            return employeeRepository.findByDepartment_Id(depId);
        } else {
            throw new DepartmentNotFoundException(depId);
        }
    }

    @RequestMapping(path = "/{depId}/subs", method = RequestMethod.GET)
    public List<Department> findSubDeps(@PathVariable Long depId) {
        if (departmentRepository.findById(depId) != null) {
            return departmentRepository.findByParent_Id(depId);
        } else {
            throw new DepartmentNotFoundException(depId);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public Department createDepartment(@RequestBody Department department) {
        return departmentRepository.save(department);
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<Department> findAllDeps() {
        return departmentRepository.findAll();
    }

    @RequestMapping(path = "/{depId}", method = RequestMethod.GET)
    public Department selectEmployee(@PathVariable Long depId) {
        return departmentRepository.findById(depId);
    }

    @RequestMapping(path = "/{depId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteEmployee(@PathVariable Long depId) {
        Department current = departmentRepository.findById(depId);
        if (current != null) {
            if (employeeRepository.findByDepartment_Id(depId).isEmpty()) {
                departmentRepository.delete(depId);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                throw new DepartmentIsNotEmptyException(depId);
            }
        } else {
            throw new DepartmentNotFoundException(depId);
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError entityNotFound(EntityNotFoundException e) {
        return new RestError(1, e.getMessage());
    }

    @ExceptionHandler(DepartmentIsNotEmptyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError departmentIsNotEmpty(DepartmentIsNotEmptyException e) {
        long departmentId = e.getDepartmentId();
        return new RestError(2, "Department [" + departmentId + "] is not empty.");
    }

}
