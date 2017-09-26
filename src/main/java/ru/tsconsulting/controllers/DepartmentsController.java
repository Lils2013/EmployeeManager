package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.error_handling.*;
import ru.tsconsulting.error_handling.already_exist_exceptions.DepartmentAlreadyExistsException;
import ru.tsconsulting.error_handling.already_exist_exceptions.EntityAlreadyExistsException;
import ru.tsconsulting.error_handling.not_found_exceptions.DepartmentNotFoundException;
import ru.tsconsulting.error_handling.not_found_exceptions.EmployeeNotFoundException;
import ru.tsconsulting.error_handling.not_found_exceptions.EntityNotFoundException;
import ru.tsconsulting.error_handling.notification_exceptions.DepartmentHasSubdepartmentsException;
import ru.tsconsulting.error_handling.notification_exceptions.DepartmentIsNotEmptyException;
import ru.tsconsulting.error_handling.notification_exceptions.InvalidDepartmentHierarchyException;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;

import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
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

    @ApiOperation(value = "Create new department")
    @RequestMapping(method = RequestMethod.POST)
    public Department createDepartment(@ApiParam(value = "Department details", required = true) @Validated @RequestBody Department.DepartmentDetails departmentDetails,
                                       HttpServletRequest request) {
        Department department = new Department(departmentDetails);
        if(departmentRepository.findByName(departmentDetails.getName()) != null) {
            throw new DepartmentAlreadyExistsException(departmentDetails.getName());
        }
        Long parentId = departmentDetails.getParent();
        if (parentId != null) {
            Department parentDepartment = departmentRepository.findByIdAndIsDismissedIsFalse(parentId);
            if (parentDepartment == null) {
                throw new DepartmentNotFoundException(parentId.toString());
            } else {
                department.setParent(parentDepartment);
            }
        }
        Long chiefId = departmentDetails.getChiefId();
        if (chiefId != null) {
            Employee chief = employeeRepository.findByIdAndIsFiredIsFalse(chiefId);
            if (chief == null) {
                throw new EmployeeNotFoundException(chiefId.toString());
            } else {
                department.setChief(chief);
            }
        }
        return departmentRepository.save(department);
    }

    @ApiOperation(value = "Return department by id")
    @RequestMapping(path = "/{departmentId}", method = RequestMethod.GET)
    public Department getDepartment(@ApiParam(value = "Id of department, positive integer", required = true) @PathVariable Long departmentId,
                                    HttpServletRequest request) {
        Department department = departmentRepository.findById(departmentId);
        if (department == null) {
            throw new DepartmentNotFoundException(departmentId.toString());
        }
        return department;
    }

    @ApiOperation(value = "Return department by name")
    @RequestMapping(method = RequestMethod.GET)
    public Department getDepartmentByName(@ApiParam(value = "Department name, string with size between 1 and 64",
            required = true)
                                          @RequestParam(value = "name") String departmentName,
                                          HttpServletRequest request) {
        Department department = departmentRepository.findByName(departmentName);
        if (department == null) {
            throw new DepartmentNotFoundException(departmentName);
        }
        return department;
    }

    @ApiOperation(value = "Delete department")
    @RequestMapping(path = "/{departmentId}", method = RequestMethod.DELETE)
    public void deleteDepartment(@ApiParam(value = "Id of a department, positive integer",
            required = true) @PathVariable Long departmentId,
                                 HttpServletRequest request) {
        Department current = departmentRepository.findByIdAndIsDismissedIsFalse(departmentId);
        if (current != null) {
            if (employeeRepository.findByDepartmentIdAndIsFiredIsFalseOrderByIdAsc(departmentId).isEmpty()) {
                if (departmentRepository.findByParentIdAndIsDismissedIsFalseOrderByIdAsc(departmentId).isEmpty()) {
                    current.setDismissed(true);
                    departmentRepository.save(current);
                } else {
                    throw new DepartmentHasSubdepartmentsException(departmentId.toString());
                }
            } else {
                throw new DepartmentIsNotEmptyException(departmentId.toString());
            }
        } else {
            throw new DepartmentNotFoundException("" + departmentId);
        }
    }

    @ApiOperation(value = "Edit department")
    @RequestMapping(path = "/{departmentId}", method = RequestMethod.POST)
    public Department changeDepartmentName(@ApiParam(value = "Id of a department, positive integer",
            required = true) @PathVariable Long departmentId,
                                     @ApiParam(value = "Id of a new chief, positive integer")
                                     @RequestParam(value = "newChiefId", required = false)Long newChiefId,
                                     @ApiParam(value = "New department name, string with size between 1 and 64")
                                     @RequestParam(value = "newName", required = false)String newName,
                                     HttpServletRequest request) {
        Department department = departmentRepository.findById(departmentId);

        if (newName != null) {

            department.setName(newName);
            if(departmentRepository.findByName(newName) != null) {
                throw new DepartmentAlreadyExistsException(newName);
            }
        }

        if (newChiefId != null) {
            Employee chief = employeeRepository.findByIdAndIsFiredIsFalse(newChiefId);
            if (chief != null) {
                department.setChief(chief);
            } else {
                throw new EmployeeNotFoundException(newChiefId.toString());
            }
        }

       return departmentRepository.save(department);
    }

    @ApiOperation(value = "Return all direct sub departments of given department")
    @RequestMapping(path = "/{departmentId}/subs", method = RequestMethod.GET)
    public List<Department> findSubDepartments(@ApiParam(value = "Id of a department, positive integer",
            required = true) @PathVariable Long departmentId,
                                               HttpServletRequest request) {
        if (departmentRepository.findByIdAndIsDismissedIsFalse(departmentId) != null) {
            return departmentRepository.findByParentIdAndIsDismissedIsFalseOrderByIdAsc(departmentId);
        } else {
            throw new DepartmentNotFoundException(departmentId.toString());
        }
    }

    @ApiOperation(value = "Return all employees of department")
    @RequestMapping(path = "/{departmentId}/employees", method = RequestMethod.GET)
    public List<Employee> employeeByDepartment(@ApiParam(value = "Id of a department, positive integer",
            required = true) @PathVariable Long departmentId, HttpServletRequest request) {
        if (departmentRepository.findByIdAndIsDismissedIsFalse(departmentId) != null) {
            return employeeRepository.findByDepartmentIdAndIsFiredIsFalseOrderByIdAsc(departmentId);
        } else {
            throw new DepartmentNotFoundException(departmentId.toString());
        }
    }

    @ApiOperation(value = "Change parent department")
    @RequestMapping(path = "/{departmentId}/changeHierarchy", method = RequestMethod.POST)
    public Department changeHierarchy(
            @ApiParam(value = "Id of a department, positive integer", required = true)
            @PathVariable Long departmentId,
            @ApiParam(value = "Positive integer, must not be child of changed department to avoid circular dependency",
                    required = true)
            @RequestParam(value = "newHeadDepartmentId") Long newHeadDepartmentId,
            HttpServletRequest request) {
        if (Objects.equals(departmentId, newHeadDepartmentId)) {
            throw new InvalidDepartmentHierarchyException();
        }
        Department original = departmentRepository.findByIdAndIsDismissedIsFalse(departmentId);
        Department newHead = departmentRepository.findByIdAndIsDismissedIsFalse(newHeadDepartmentId);
        if (original == null) {
            throw new DepartmentNotFoundException(departmentId.toString());
        }
        if (newHead == null) {
            throw new DepartmentNotFoundException(newHeadDepartmentId.toString());
        }
        if (isParent(newHead, original)) {
            throw new InvalidDepartmentHierarchyException();
        }
        original.setParent(newHead);
        return departmentRepository.save(original);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestStatus entityNotFound(EntityNotFoundException e) {
        return new RestStatus(Status.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DepartmentIsNotEmptyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestStatus departmentIsNotEmpty(DepartmentIsNotEmptyException e) {
        return new RestStatus(Status.DEPARTMENT_NOT_EMPTY, e.getMessage());
    }

    @ExceptionHandler(DepartmentHasSubdepartmentsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestStatus departmentHasSubDepartments(DepartmentHasSubdepartmentsException e) {
        return new RestStatus(Status.DEPARTMENT_HAS_SUB_DEPARTMENTS, e.getMessage());
    }

    @ExceptionHandler(InvalidDepartmentHierarchyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestStatus invalidHierarchy(InvalidDepartmentHierarchyException e) {
        return new RestStatus(Status.INVALID_HIERARCHY, e.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RestStatus alreadyExists(EntityAlreadyExistsException e) {
        return new RestStatus(Status.ALREADY_EXISTS, e.getMessage());
    }

    private boolean isParent(Department potentialChild, Department potentialParent) {
        if (potentialChild == potentialParent) {
            return false;
        }
        Department current = potentialChild;
        while (current.getParent() != null) {
            current = current.getParent();
            if (current.getId().equals(potentialParent.getId())) {
                return true;
            }
        }
        return false;
    }
}