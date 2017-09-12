package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.errorHandling.*;
import ru.tsconsulting.errorHandling.not_found_exceptions.DepartmentNotFoundException;
import ru.tsconsulting.errorHandling.not_found_exceptions.EmployeeNotFoundException;
import ru.tsconsulting.errorHandling.not_found_exceptions.EntityNotFoundException;
import ru.tsconsulting.errorHandling.not_specified_exceptions.AttributeNotSpecifiedException;
import ru.tsconsulting.errorHandling.notification_exceptions.DepartmentHasSubdepartmentsException;
import ru.tsconsulting.errorHandling.notification_exceptions.DepartmentIsNotEmptyException;
import ru.tsconsulting.errorHandling.notification_exceptions.InvalidDepartmentHierarchyException;
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

    @ApiOperation(value = "Create new department")
    @RequestMapping(method = RequestMethod.POST)
    public Department createDepartment(@Validated @RequestBody Department.DepartmentDetails departmentDetails,
                                       HttpServletRequest request) {
        Department department = new Department(departmentDetails);
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
    public Department getDepartment(@PathVariable Long departmentId,
                                    HttpServletRequest request) {
        Department department = departmentRepository.findById(departmentId);
        if (department == null) {
            throw new DepartmentNotFoundException(departmentId.toString());
        }
        return department;
    }

    @ApiOperation(value = "Return department by name")
    @RequestMapping(method = RequestMethod.GET)
    public Department getDepartmentByName(@RequestParam(value = "name", required = true)String departmentName,
                                          HttpServletRequest request)
    {
        Department department = departmentRepository.findByName(departmentName);
        if (department == null)
        {
            throw new DepartmentNotFoundException(departmentName);
        }
        return department;
    }

    @ApiOperation(value = "Delete department")
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
                    throw new DepartmentHasSubdepartmentsException(departmentId.toString());
                }
            } else {
                throw new DepartmentIsNotEmptyException(departmentId.toString());
            }
        } else {
            throw new DepartmentNotFoundException(""+departmentId);
        }
    }

    @ApiOperation(value = "Return all direct sub departments of given department")
    @RequestMapping(path = "/{departmentId}/subs", method = RequestMethod.GET)
    public List<Department> findSubDepartments(@PathVariable Long departmentId,
                                               HttpServletRequest request) {
        if (departmentRepository.findByIdAndIsDismissedIsFalse(departmentId) != null) {
            return departmentRepository.findByParentIdAndIsDismissedIsFalse(departmentId);
        } else {
            throw new DepartmentNotFoundException(departmentId.toString());
        }
    }

    @ApiOperation(value = "Return all employees of department")
    @RequestMapping(path = "/{departmentId}/employees", method = RequestMethod.GET)
    public List<Employee> employeeByDepartment(@PathVariable Long departmentId, HttpServletRequest request) {
        if (departmentRepository.findByIdAndIsDismissedIsFalse(departmentId) != null) {
            return employeeRepository.findByDepartmentIdAndIsFiredIsFalse(departmentId);
        } else {
            throw new DepartmentNotFoundException(departmentId.toString());
        }
    }

	@ApiOperation(value = "Change parent department")
    @RequestMapping(path = "/{departmentId}/changeHierarchy", method = RequestMethod.POST)
    public Department changeHierarchy(
            @ApiParam(value = "id of a department", required = true)@PathVariable Long departmentId,
            @ApiParam(value = "must not be child of changed department to avoid circular " +
                    "dependency", required = true)@RequestParam(value = "newHeadDepartmentId") Long newHeadDepartmentId,
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
        if (isParent(newHead,original)) {
            throw new InvalidDepartmentHierarchyException();
        }
        original.setParent(newHead);
        return departmentRepository.save(original);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public RestError entityNotFound(EntityNotFoundException e) {
        return new RestError(Errors.ENTITY_NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(DepartmentIsNotEmptyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError departmentIsNotEmpty(DepartmentIsNotEmptyException e) {
        return new RestError(Errors.DEPARTMENT_NOT_EMPTY, e.getMessage());
    }

    @ExceptionHandler(DepartmentHasSubdepartmentsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError departmentHasSubDepartments(DepartmentHasSubdepartmentsException e) {
        return new RestError(Errors.DEPARTMENT_HAS_SUB_DEPARTMENTS, e.getMessage());
    }

    @ExceptionHandler(InvalidDepartmentHierarchyException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public RestError invalidHierarchy(InvalidDepartmentHierarchyException e) {
        return new RestError(Errors.INVALID_HIERARCHY, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError attributeNotSpecified(MethodArgumentNotValidException e) {
        BindingResult result = e.getBindingResult();
        FieldError error = result.getFieldError();
        return new RestError(Errors.INVALID_ATTRIBUTE, error.getDefaultMessage() +
                " Rejected value is: \'" + error.getRejectedValue() + "\'");
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