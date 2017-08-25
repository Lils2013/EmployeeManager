package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.DepartmentEntity;
import ru.tsconsulting.entities.EmployeeEntity;
import ru.tsconsulting.errorHandling.DepartmentNotFoundException;
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

    @RequestMapping(path="/{depId}/changeHierarchy",method = RequestMethod.POST)
    public DepartmentEntity changeHierarchy(@PathVariable Long depId,
                                        @RequestParam(value="newHeadDepartmentId") Long newHeadDepartmentId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        DepartmentEntity original = entityManager.find(DepartmentEntity.class,depId);
        original.setParent(entityManager.find(DepartmentEntity.class,newHeadDepartmentId));
        entityManager.getTransaction().commit();
        return original;
    }

    @RequestMapping(path="/{depId}/employees",method = RequestMethod.GET)
    public List<EmployeeEntity> employeeByDep(@PathVariable Long depId) {
        if (departmentRepository.findById(depId) != null) {
            return employeeRepository.findByDepartment_Id(depId);
        } else {
            throw new DepartmentNotFoundException(depId);
        }
    }

    @RequestMapping(path="/{depId}/subs",method = RequestMethod.GET)
    public List<DepartmentEntity> findSubDeps(@PathVariable Long depId) {
        if (departmentRepository.findById(depId) != null) {
            return departmentRepository.findByParent_Id(depId);
        } else {
            throw new DepartmentNotFoundException(depId);
        }
    }

    @RequestMapping(method = RequestMethod.POST)
    public DepartmentEntity createDepartment(@RequestBody DepartmentEntity department){
        return departmentRepository.save(department);
    }

    @ExceptionHandler(DepartmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError departmentNotFound(DepartmentNotFoundException e) {
        long departmentId = e.getDepartmentId();
        return new RestError(1, "Department [" + departmentId + "] not found");
    }
}
