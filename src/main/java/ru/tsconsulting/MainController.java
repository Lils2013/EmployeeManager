package ru.tsconsulting;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.DepartmentEntity;
import ru.tsconsulting.entities.EmployeeEntity;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.DepartmentTestRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.EmployeeTestRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@RestController
public class MainController {

    private final
    EmployeeRepository employeeRepository;

    private final
    DepartmentRepository departmentRepository;

    private final DepartmentTestRepository departmentTestRepository;

    private final EmployeeTestRepository testRepository;

    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public MainController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
                          DepartmentTestRepository departmentTestRepository, EmployeeTestRepository testRepository,
                          EntityManagerFactory entityManagerFactory) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.departmentTestRepository = departmentTestRepository;
        this.testRepository = testRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    @RequestMapping("/departments")
    public List<DepartmentEntity> departments(@RequestParam(value="name") String name) {
        return departmentRepository.findByName(name);
    }

    @RequestMapping("/employees")
    public List<EmployeeEntity> employees(@RequestParam(value="name") String name) {
        return employeeRepository.findByFirstname(name);
    }

    @RequestMapping("/test")
    public List<DepartmentEntity> test(@RequestParam(value="name") String name) {
        return departmentTestRepository.findByEmployees_Firstname(name);
    }


    @RequestMapping(path="/department/{depId}/employees",method = RequestMethod.GET)
    public List<EmployeeEntity> employeeByDep(@PathVariable Long depId) {
        return employeeRepository.findByDepartmentId(depId);
    }

    @RequestMapping(path="/first",method = RequestMethod.GET)
    public List<DepartmentEntity> first(@RequestParam(value="name") String name) {
//        List<DepartmentEntity> result = new ArrayList<>();
//        for (DepartmentEntity departmentTest : departmentTestRepository.findByParentDepartment_Name(name)) {
//            findChildDepartments(departmentTest,result);
//        }
//        return result;
        return departmentTestRepository.findByParentDepartment_Name(name);
    }

    @RequestMapping(path="/fifth",method = RequestMethod.GET)
    public List<DepartmentEntity> fifth(@RequestParam(value="departmentId") long departmentId,
                                        @RequestParam(value="newHead") long newHeadDepartmentId) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        DepartmentEntity original = entityManager.find(DepartmentEntity.class,departmentId);
        original.setParentDepartment(entityManager.find(DepartmentEntity.class,newHeadDepartmentId));
        entityManager.getTransaction().commit();
        return new ArrayList<>();
    }

    @RequestMapping(path="/employee",method = RequestMethod.POST, headers = "Content-Type=application/json")
    public EmployeeEntity createEmployee(@RequestBody EmployeeEntity employee){
        return employeeRepository.save(employee);
    }

}