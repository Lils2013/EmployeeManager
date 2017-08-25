package ru.tsconsulting;

import java.util.ArrayList;
import java.util.List;

import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.jpa.internal.EntityManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.DepartmentTest;
import ru.tsconsulting.entities.DepartmentEntity;
import ru.tsconsulting.entities.EmployeeEntity;
import ru.tsconsulting.entities.EmployeeTest;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.DepartmentTestRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.EmployeeTestRepository;

import javax.persistence.*;
import javax.transaction.Transactional;

@RestController

public class MainController {

    private final
    EmployeeRepository employeeRepository;

    private final
    DepartmentRepository departmentRepository;

    private final DepartmentTestRepository departmentTestRepository;

    private final EmployeeTestRepository testRepository;

    @Autowired
    public MainController(EmployeeRepository employeeRepository, DepartmentRepository departmentRepository,
                          DepartmentTestRepository departmentTestRepository, EmployeeTestRepository testRepository) {
        this.employeeRepository = employeeRepository;
        this.departmentRepository = departmentRepository;
        this.departmentTestRepository = departmentTestRepository;
        this.testRepository = testRepository;
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
    public List<DepartmentTest> test(@RequestParam(value="name") String name) {
        return departmentTestRepository.findByEmployees_Firstname(name);
    }

    @RequestMapping(path="/department/{depId}/employees",method = RequestMethod.GET)
    public List<EmployeeEntity> employeeByDep(@PathVariable Long depId) {
        return employeeRepository.findByDepartmentId(depId);
    }

    @RequestMapping(path="/first",method = RequestMethod.GET)
    public List<DepartmentTest> first(@RequestParam(value="name") String name) {
//        List<DepartmentTest> result = new ArrayList<>();
//        for (DepartmentTest departmentTest : departmentTestRepository.findByParentDepartment_Name(name)) {
//            findChildDepartments(departmentTest,result);
//        }
//        return result;
        return departmentTestRepository.findByParentDepartment_Name(name);
    }

    @RequestMapping(path="/employee",method = RequestMethod.POST, headers = "Content-Type=application/json")
    public EmployeeEntity createEmployee(@RequestBody EmployeeEntity employee){
        return employeeRepository.save(employee);
    }

    @RequestMapping(path="/department/{depId}/childs")
    public List<DepartmentTest> findChildDepartments(@PathVariable Long depId) {
        return departmentTestRepository.findByParentDepartment_Id(depId);
    }
}