package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.*;
import ru.tsconsulting.errorHandling.Status;
import ru.tsconsulting.errorHandling.RestStatus;
import ru.tsconsulting.errorHandling.not_found_exceptions.UserNotFoundException;
import ru.tsconsulting.errorHandling.not_specified_exceptions.ParameterNotSpecifiedException;
import ru.tsconsulting.errorHandling.not_specified_exceptions.RolesNotSpecifiedException;
import ru.tsconsulting.errorHandling.not_specified_exceptions.UsernameNotSpecifiedException;
import ru.tsconsulting.errorHandling.notification_exceptions.RoleAlreadyExistsException;
import ru.tsconsulting.errorHandling.notification_exceptions.UserAlreadyExistsException;
import ru.tsconsulting.repositories.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DepartmentHistoryRepository departmentHistoryRepository;
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final AccessHistoryRepository accessHistoryRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdminController(DepartmentHistoryRepository departmentHistoryRepository,
                           EmployeeHistoryRepository employeeHistoryRepository, AccessHistoryRepository accessHistoryRepository,
                           UserRepository userRepository) {
        this.departmentHistoryRepository = departmentHistoryRepository;
        this.employeeHistoryRepository = employeeHistoryRepository;
        this.accessHistoryRepository = accessHistoryRepository;
        this.userRepository = userRepository;
    }

    @ApiOperation(value = "Get full access history")
    @RequestMapping(path = "/", method = RequestMethod.GET)
    public List<AccessHistory> getAccessHistory(@ApiParam(value = "Requires datetime, compliant to LocalDateTime format in Java, " +
            "e.g. 2007-12-03T10:15:30") @RequestParam(value = "from", required = false) String from,
                                                @ApiParam(value = "Requires datetime, compliant to LocalDateTime format in Java, " +
                                                        "e.g. 2007-12-03T10:15:30") @RequestParam(value = "to", required = false) String to,
                                                HttpServletRequest request) {
        List<AccessHistory> result = new ArrayList<>();
        if (from != null && to != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            LocalDateTime toDate = LocalDateTime.parse(to);
            result.addAll(accessHistoryRepository.findByDateTimeBetween(fromDate, toDate));
        } else if (from != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            result.addAll(accessHistoryRepository.findByDateTimeAfter(fromDate));
        } else if (to != null) {
            LocalDateTime toDate = LocalDateTime.parse(to);
            result.addAll(accessHistoryRepository.findByDateTimeBefore(toDate));
        } else {
            result.addAll(accessHistoryRepository.findAll());
        }
        result.sort((o1, o2) -> {
            if (o1.getId() > o2.getId()) {
                return 1;
            } else if (o1.getId() < o2.getId()) {
                return -1;
            } else {
                return 0;
            }
        });
        return result;
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
                    "e.g. 2007-12-03T10:15:30") @RequestParam(value = "to", required = false) String to,
            HttpServletRequest request) {
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
                    "e.g. 2007-12-03T10:15:30") @RequestParam(value = "to", required = false) String to,
            HttpServletRequest request) {
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

    @Transactional
    @ApiOperation(value = "Register new user")
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public User registerUser(@Validated @RequestBody User.UserDetails userDetails,
                                   HttpServletRequest request){
        if(userDetails.getUsername() == null || userDetails.getPassword() == null || userDetails.getEnabled() == null) {
            throw new ParameterNotSpecifiedException();
        }
        else if(userRepository.findByUsername(userDetails.getUsername()) != null) {
            throw new UserAlreadyExistsException(userDetails.getUsername());
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDetails.setPassword(encoder.encode(userDetails.getPassword()));
        User user = new User(userDetails);
        user.getRoles().add(Role.ROLE_USER);
        userRepository.save(user);
        return user;
    }

    @ApiOperation(value = "Give privileges to user")
    @RequestMapping(path = "/user/grant", method = RequestMethod.POST)
    public User giveRoleToUser(@Validated @RequestBody String userName, String[] roles,
                               HttpServletRequest request){
        User user;
        if(userName == null) {
            throw new UsernameNotSpecifiedException();
        } else if(roles == null) {
            throw new RolesNotSpecifiedException();
        } else {
            if((user = userRepository.findByUsername(userName)) != null) {
                for(String r : roles) {
                    try {
                        Role role = Role.valueOf(r);
                        user.getRoles().add(role);
                    } catch(IllegalArgumentException ignored) {
                    }
                }
            } else {
                throw new UserNotFoundException(userName);
            }
        }
        userRepository.save(user);
        return user;
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestStatus failedToParse(DateTimeParseException e) {
        return new RestStatus(Status.PARSE_FAIL, "DateTime could not be parsed");
    }

    @ExceptionHandler(RoleAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public RestStatus failedToParse(RoleAlreadyExistsException e) {
        return new RestStatus(Status.ALREADY_EXISTS, "Role is already exists");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public RestStatus failedToParse(UserAlreadyExistsException e) {
        return new RestStatus(Status.ALREADY_EXISTS, "Username is already taken. Please try another one");
    }
}
