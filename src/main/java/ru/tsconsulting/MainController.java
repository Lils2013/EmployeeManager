package ru.tsconsulting;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.tsconsulting.entities.DepartmentTest;
import ru.tsconsulting.entities.DepartmentEntity;
import ru.tsconsulting.entities.EmployeeEntity;
import ru.tsconsulting.entities.EmployeeTest;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.DepartmentTestRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.EmployeeTestRepository;

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

    @RequestMapping(path="/second",method = RequestMethod.GET)
    public List<EmployeeTest> second(@RequestParam(value="name") String name) {
        return testRepository.findByDepartment_Name(name);
    }
}