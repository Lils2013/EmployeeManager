package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.Grade;
import ru.tsconsulting.entities.Position;
import ru.tsconsulting.errorHandling.*;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.GradeRepository;
import ru.tsconsulting.repositories.PositionRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
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

	@ApiOperation(value = "Transfer employee from one department to another")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful transfer of employee"),
			@ApiResponse(code = 404, message = "Employee with given id does not exist or one of the departments does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path = "/{employeeId}/transfer", method = RequestMethod.POST)
    public Employee transferEmployee(@PathVariable Long employeeId,
                                     @RequestParam(value="newDepartmentId") Long newDepartmentId,
                                     HttpServletRequest request) {
        if (employeeRepository.findByIdAndIsFiredIsFalse(employeeId) == null) {
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

	@ApiOperation(value = "Fire employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful firing of employee"),
			@ApiResponse(code = 404, message = "Employee with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path = "/{employeeId}", method = RequestMethod.DELETE)
    public void fireEmployee(@PathVariable Long employeeId,
	                         HttpServletRequest request) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(employeeId);
        if (employee != null) {
            employee.setFired(true);
            employeeRepository.save(employee);
        } else {
            throw new EmployeeNotFoundException(employeeId);
        }
    }

	@ApiOperation(value = "Create employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful creation of employee"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(method = RequestMethod.POST)
    public Employee createEmployee(@RequestBody Employee.EmployeeDetails employeeDetails,
                                   HttpServletRequest request) {
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
            throw new DepartmentNotSpecifiedException(employeeDetails.getDepartment());
        } else {
            Department department = departmentRepository.findByIdAndIsDismissedIsFalse(employeeDetails.getDepartment());
            if (department == null) {
                throw new DepartmentNotFoundException(employeeDetails.getDepartment());
            } else {
                employee.setDepartment(department);
            }
        }

        Employee result = employeeRepository.save(employee);

        return result;
    }

	@ApiOperation(value = "Return employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of employee"),
			@ApiResponse(code = 404, message = "Employee with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path = "/{employeeId}", method = RequestMethod.GET)
    public Employee getEmployee(@PathVariable Long employeeId,
                                HttpServletRequest request) {
        Employee employee = employeeRepository.findById(employeeId);

        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }

        return employee;
    }

	@ApiOperation(value = "Return history of employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful retrieval of the history of employee"),
			@ApiResponse(code = 404, message = "History for given employee does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path="/{employeeId}/history",method = RequestMethod.GET)
    public List<Employee> getHistory(@PathVariable Long employeeId,
                                     HttpServletRequest request) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        if (employeeRepository.findByIdAndIsFiredIsFalse(employeeId) == null) {
            throw new EmployeeNotFoundException(employeeId);
        }

        AuditReader reader = AuditReaderFactory.get(entityManager);
        AuditQuery query = reader.createQuery().forRevisionsOfEntity(Employee.class,
                true, false);
        query.add(AuditEntity.id().eq(employeeId));
        List<Employee> list = query.getResultList();
        entityManager.getTransaction().commit();

        return list;
    }

	@ApiOperation(value = "Edit employee by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful edition of employee"),
			@ApiResponse(code = 404, message = "Employee with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
    @RequestMapping(path="/{employeeId}/edit",method = RequestMethod.POST)
    public Employee editEmployee(@PathVariable Long employeeId,
                                 @RequestParam(value = "newPositionId", required=false) Long newPositionId,
                                 @RequestParam(value = "newGrade", required=false) Long newGrade,
                                 @RequestParam(value = "newSalary", required=false) Long newSalary,
                                             HttpServletRequest request) {
        Employee employee = employeeRepository.findByIdAndIsFiredIsFalse(employeeId);

        if (employee==null)
        {
            throw new EmployeeNotFoundException(employeeId);
        }

        Position position = positionRepository.findById(newPositionId);

        if (position==null)
        {
            throw new PositionNotFoundException(employeeId);
        }

        Grade grade = gradeRepository.findById(newGrade);

        if (grade==null)
        {
            throw new GradeNotFoundException(employeeId);
        }

        employee.setPosition(position);
        employee.setGrade(grade);
        employee.setSalary(newSalary);
        employeeRepository.save(employee);

        return employee;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError entityNotFound(EntityNotFoundException e) {
        return new RestError(Errors.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DepartmentNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError departmentNotSpecified(DepartmentNotSpecifiedException e) {
        return new RestError(Errors.DEPARTMENT_NOT_SPECIFIED, e.getMessage());
    }
}
