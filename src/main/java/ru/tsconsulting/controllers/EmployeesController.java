package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tsconsulting.entities.DepartmentEntity;
import ru.tsconsulting.entities.EmployeeEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@RestController
@RequestMapping("/employees")
public class EmployeesController {

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public EmployeesController(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @RequestMapping(path="/transfer",method = RequestMethod.POST)
    public EmployeeEntity fifth(@RequestParam(value="employeeId") long employeeId,
                                        @RequestParam(value="newDepartmentId") long newDepartmentId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        EmployeeEntity employee = entityManager.find(EmployeeEntity.class,employeeId);
        employee.setDepartment(entityManager.find(DepartmentEntity.class,newDepartmentId));
        entityManager.getTransaction().commit();
        return employee;
    }
}
