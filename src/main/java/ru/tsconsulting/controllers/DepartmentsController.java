package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.errorHandling.*;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
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

	@ApiOperation(value = "Change parent department")
    @RequestMapping(path = "/{depId}/changeHierarchy", method = RequestMethod.POST)
    public Department changeHierarchy(@PathVariable Long depId,
                                      @RequestParam(value = "newHeadDepartmentId") Long newHeadDepartmentId,
                                      HttpServletRequest request) {
        if (departmentRepository.findByIdAndIsDismissedIsFalse(depId) == null) {
            throw new DepartmentNotFoundException(depId);
        }
        if (departmentRepository.findByIdAndIsDismissedIsFalse(newHeadDepartmentId) == null) {
            throw new DepartmentNotFoundException(newHeadDepartmentId);
        }
        Department original = departmentRepository.findByIdAndIsDismissedIsFalse(depId);
        original.setParent(departmentRepository.findByIdAndIsDismissedIsFalse(newHeadDepartmentId));
        return departmentRepository.save(original);
    }

	@ApiOperation(value = "Return all employees of department by id")
    @RequestMapping(path = "/{depId}/employees", method = RequestMethod.GET)
    public List<Employee> employeeByDep(@PathVariable Long depId, HttpServletRequest request) {
        if (departmentRepository.findByIdAndIsDismissedIsFalse(depId) != null) {
            return employeeRepository.findByDepartmentIdAndIsFiredIsFalse(depId);
        } else {
            throw new DepartmentNotFoundException(depId);
        }
    }

	@ApiOperation(value = "Return all sub departments of given department by id")
    @RequestMapping(path = "/{depId}/subs", method = RequestMethod.GET)
    public List<Department> findSubDeps(@PathVariable Long depId,
                                        HttpServletRequest request) {
        if (departmentRepository.findByIdAndIsDismissedIsFalse(depId) != null) {
            return departmentRepository.findByParentIdAndIsDismissedIsFalse(depId);
        } else {
            throw new DepartmentNotFoundException(depId);
        }
    }

	@ApiOperation(value = "Create new department")
    @RequestMapping(method = RequestMethod.POST)
    public Department createDepartment(@RequestBody Department.DepartmentDetails departmentDetails,
                                       HttpServletRequest request) {
        Department department = new Department(departmentDetails);
        Long parentId = departmentDetails.getParent();
        if (parentId != null) {
            Department parentDepartment = departmentRepository.findByIdAndIsDismissedIsFalse(parentId);
            if (parentDepartment == null) {
                throw new DepartmentNotFoundException(parentId);
            } else {
                department.setParent(parentDepartment);
            }
        }
        return departmentRepository.save(department);
    }

    @RequestMapping(path = "/{depId}", method = RequestMethod.GET)
    public Department getDepartment(@PathVariable Long depId,
                                    HttpServletRequest request) {
        Department department = departmentRepository.findById(depId);
        if (department == null) {
            throw new DepartmentNotFoundException(depId);
        }
        return department;
    }

	@ApiOperation(value = "Delete department by id")
    @RequestMapping(path = "/{depId}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteDepartment(@PathVariable Long depId,
                                              HttpServletRequest request) {
        Department current = departmentRepository.findByIdAndIsDismissedIsFalse(depId);
        if (current != null) {
            if (employeeRepository.findByDepartmentIdAndIsFiredIsFalse(depId).isEmpty()) {
                if (departmentRepository.findByParentIdAndIsDismissedIsFalse(depId).isEmpty()) {
                    current.setDismissed(true);
                    departmentRepository.save(current);
                    return new ResponseEntity<>(HttpStatus.OK);
                } else {
                    throw new DepartmentHasSubdepartmentsException(depId);
                }
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

    @ExceptionHandler(DepartmentHasSubdepartmentsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError departmentHasSubdepartments(DepartmentHasSubdepartmentsException e) {
        long departmentId = e.getDepartmentId();
        return new RestError(4, "Department [" + departmentId + "] has subdepartments.");
    }
}
