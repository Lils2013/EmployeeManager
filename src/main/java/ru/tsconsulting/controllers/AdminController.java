package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.DepartmentHistory;
import ru.tsconsulting.entities.EmployeeHistory;
import ru.tsconsulting.errorHandling.Errors;
import ru.tsconsulting.errorHandling.RestError;
import ru.tsconsulting.repositories.DepartmentHistoryRepository;
import ru.tsconsulting.repositories.EmployeeHistoryRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DepartmentHistoryRepository departmentHistoryRepository;
    private final EmployeeHistoryRepository employeeHistoryRepository;

    @Autowired
    public AdminController(DepartmentHistoryRepository departmentHistoryRepository, EmployeeHistoryRepository employeeHistoryRepository) {
        this.departmentHistoryRepository = departmentHistoryRepository;
        this.employeeHistoryRepository = employeeHistoryRepository;
    }

    @ApiOperation(value = "Get access history for an employee",
            notes = "History can be accessed even for non-existing employees")
    @ApiResponses(value = {@ApiResponse(code = 404, message = "There is no user with that id.")})
    @RequestMapping(path = "/employee/{employeeId}", method = RequestMethod.GET)
    public List<EmployeeHistory> getEmployeeAccessHistory(
            @ApiParam(value = "Id of an employee, a whole number in the range of (0) to (1,0E19)", required = true) @PathVariable Long employeeId,
            @ApiParam(value = "Requires datetime, compliant to LocalDateTime format in Java, " +
                    "e.g. 2007-12-03T10:15:30") @RequestParam(value = "from", required = false) String from,
            @ApiParam(value = "Requires datetime, compliant to LocalDateTime format in Java, " +
                    "e.g. 2007-12-03T10:15:30") @RequestParam(value = "to", required = false) String to) {
        if (from != null && to != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            LocalDateTime toDate = LocalDateTime.parse(to);
            return employeeHistoryRepository.findByEmployeeIdAndDateTimeBetween(employeeId, fromDate, toDate);
        } else if (from != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            return employeeHistoryRepository.findByEmployeeIdAndDateTimeAfter(employeeId, fromDate);
        } else if (to != null) {
            LocalDateTime toDate = LocalDateTime.parse(to);
            return employeeHistoryRepository.findByEmployeeIdAndDateTimeBefore(employeeId, toDate);
        }
        return employeeHistoryRepository.findByEmployeeId(employeeId);
    }

    @ApiOperation(value = "Get access history for a  department",
            notes = "History can be accessed even for non-existing departmentss")
    @RequestMapping(path = "/department/{departmentId}", method = RequestMethod.GET)
    public List<DepartmentHistory> getDepartmentAccessHistory(
            @ApiParam(value = "Id of a department, a whole number in the range of (0) to (1,0E19)", required = true) @PathVariable Long departmentId,
            @ApiParam(value = "Requires datetime, compliant to LocalDateTime format in Java, " +
                    "e.g. 2007-12-03T10:15:30") @RequestParam(value = "from", required = false) String from,
            @ApiParam(value = "Requires datetime, compliant to LocalDateTime format in Java, " +
                    "e.g. 2007-12-03T10:15:30") @RequestParam(value = "to", required = false) String to) {
        if (from != null && to != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            LocalDateTime toDate = LocalDateTime.parse(to);
            return departmentHistoryRepository.findByDepartmentIdAndDateTimeBetween(departmentId, fromDate, toDate);
        } else if (from != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            return departmentHistoryRepository.findByDepartmentIdAndDateTimeAfter(departmentId, fromDate);
        } else if (to != null) {
            LocalDateTime toDate = LocalDateTime.parse(to);
            return departmentHistoryRepository.findByDepartmentIdAndDateTimeBefore(departmentId, toDate);
        }
        return departmentHistoryRepository.findByDepartmentId(departmentId);
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError failedToParse(DateTimeParseException e) {
        return new RestError(Errors.PARSE_FAIL, "DateTime could not be parsed");
    }
}
