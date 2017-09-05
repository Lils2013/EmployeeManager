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
import java.util.Objects;

@RestController
@RequestMapping("/departments")
public class DepartmentsController {
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public DepartmentsController(DepartmentRepository departmentRepository,
                                 EntityManagerFactory entityManagerFactory,
                                 EmployeeRepository employeeRepository) {
        this.departmentRepository = departmentRepository;
        this.employeeRepository = employeeRepository;
    }

	@ApiOperation(value = "Change parent department")
    @RequestMapping(path = "/{departmentId}/changeHierarchy", method = RequestMethod.POST)
    public Department changeHierarchy(@PathVariable Long depId,
                                      @RequestParam(value = "newHeadDepartmentId") Long newHeadDepartmentId,
                                      HttpServletRequest request) {
        if (Objects.equals(depId, newHeadDepartmentId)) {
            throw new InvalidDepartmentHierarchyException();
        }
        Department original = departmentRepository.findByIdAndIsDismissedIsFalse(depId);
        Department newHead = departmentRepository.findByIdAndIsDismissedIsFalse(newHeadDepartmentId);
        if (original == null) {
            throw new DepartmentNotFoundException(depId);
        }
        if (newHead == null) {
            throw new DepartmentNotFoundException(newHeadDepartmentId);
        }
        if (isParent(newHead,original)) {
            throw new InvalidDepartmentHierarchyException();
        }
        original.setParent(newHead);
        return departmentRepository.save(original);
    }

	@ApiOperation(value = "Return all employees of department by id")
    @RequestMapping(path = "/{departmentId}/employees", method = RequestMethod.GET)
    public List<Employee> employeeByDepartment(@PathVariable Long departmentId, HttpServletRequest request) {
        if (departmentRepository.findByIdAndIsDismissedIsFalse(departmentId) != null) {
            return employeeRepository.findByDepartmentIdAndIsFiredIsFalse(departmentId);
        } else {
            throw new DepartmentNotFoundException(departmentId);
        }
    }

	@ApiOperation(value = "Return all sub departments of given department by id")
    @RequestMapping(path = "/{departmentId}/subs", method = RequestMethod.GET)
    public List<Department> findSubDepartments(@PathVariable Long departmentId,
                                        HttpServletRequest request) {
        if (departmentRepository.findByIdAndIsDismissedIsFalse(departmentId) != null) {
            return departmentRepository.findByParentIdAndIsDismissedIsFalse(departmentId);
        } else {
            throw new DepartmentNotFoundException(departmentId);
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

    @RequestMapping(path = "/{departmentId}", method = RequestMethod.GET)
    public Department getDepartment(@PathVariable Long departmentId,
                                    HttpServletRequest request) {
        Department department = departmentRepository.findById(departmentId);
        if (department == null) {
            throw new DepartmentNotFoundException(departmentId);
        }

        return department;
    }

	@ApiOperation(value = "Delete department by id")
    @RequestMapping(path = "/{departmentId}", method = RequestMethod.DELETE)
    public void deleteDepartment(@PathVariable Long departmentId,
                                              HttpServletRequest request) {
        Department current = departmentRepository.findByIdAndIsDismissedIsFalse(departmentId);

        if (current != null) {
            if (employeeRepository.findByDepartmentIdAndIsFiredIsFalse(departmentId).isEmpty()) {
                if (departmentRepository.findByParentIdAndIsDismissedIsFalse(departmentId).isEmpty()) {
                    current.setDismissed(true);
                    departmentRepository.save(current);
                } else {
                    throw new DepartmentHasSubdepartmentsException(departmentId);
                }
            } else {
                throw new DepartmentIsNotEmptyException(departmentId);
            }
        } else {
            throw new DepartmentNotFoundException(departmentId);
        }
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError entityNotFound(EntityNotFoundException e) {
        return new RestError(Errors.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DepartmentIsNotEmptyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError departmentIsNotEmpty(DepartmentIsNotEmptyException e) {
        long departmentId = e.getDepartmentId();

        return new RestError(Errors.DEPARTMENT_NOT_EMPTY, "Department [" + departmentId + "] is not empty.");
    }

    @ExceptionHandler(DepartmentHasSubdepartmentsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError departmentHasSubDepartments(DepartmentHasSubdepartmentsException e) {
        long departmentId = e.getDepartmentId();

        return new RestError(Errors.DEPARTMENT_HAS_SUB_DEPARTMENTS, "Department [" + departmentId + "] has subdepartments.");
    }

    @ExceptionHandler(InvalidDepartmentHierarchyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError invalidHierarchy(InvalidDepartmentHierarchyException e) {
        return new RestError(Errors.INVALID_HIERARCHY, "Invalid hierarchy of departments.");
    }

    private boolean isParent(Department potentialChild, Department potentialParent) {
        if (potentialChild == potentialParent) {
            return false;
        }
        Department current = potentialChild;
        while(current.getParent() != null) {
            current = current.getParent();
            if (current.getId().equals(potentialParent.getId())) {
                return true;
            }
        }
        return false;
    }
}