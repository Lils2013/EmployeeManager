package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.DepartmentEntity;
import ru.tsconsulting.entities.EmployeeEntity;
import ru.tsconsulting.repositories.EmployeeRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@RestController
@RequestMapping("/employees")
public class EmployeesController {

    private final EntityManagerFactory entityManagerFactory;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeesController(EntityManagerFactory entityManagerFactory,
                               EmployeeRepository employeeRepository) {
        this.entityManagerFactory = entityManagerFactory;
        this.employeeRepository = employeeRepository;
    }

    @RequestMapping(path="/{employeeId}/transfer",method = RequestMethod.POST)
    public EmployeeEntity transfer(@PathVariable Long employeeId,
                                        @RequestParam(value="newDepartmentId") long newDepartmentId) {
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

}
