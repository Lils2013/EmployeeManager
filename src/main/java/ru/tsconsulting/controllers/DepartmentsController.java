package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.DepartmentEntity;
import ru.tsconsulting.entities.EmployeeEntity;
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

    @RequestMapping(path="/changeHierarchy",method = RequestMethod.POST)
    public DepartmentEntity changeHierarchy(@RequestParam(value="departmentId") long departmentId,
                                        @RequestParam(value="newHeadDepartmentId") long newHeadDepartmentId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        DepartmentEntity original = entityManager.find(DepartmentEntity.class,departmentId);
        original.setParent(entityManager.find(DepartmentEntity.class,newHeadDepartmentId));
        entityManager.getTransaction().commit();
        return original;
    }

    @RequestMapping(path="/{depId}/employees",method = RequestMethod.GET)
    public List<EmployeeEntity> employeeByDep(@PathVariable Long depId) {
        return employeeRepository.findByDepartment_Id(depId);
    }

    @RequestMapping(path="/{depId}/subs",method = RequestMethod.GET)
    public List<DepartmentEntity> findSubDeps(@PathVariable Long depId) {
        return departmentRepository.findByParent_Id(depId);
    }
}
