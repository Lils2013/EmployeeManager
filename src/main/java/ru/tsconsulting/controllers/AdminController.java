package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.*;
import ru.tsconsulting.errorHandling.Status;
import ru.tsconsulting.errorHandling.RestStatus;
import ru.tsconsulting.errorHandling.not_found_exceptions.RoleNotFoundException;
import ru.tsconsulting.errorHandling.not_specified_exceptions.ParameterNotSpecifiedException;
import ru.tsconsulting.errorHandling.notification_exceptions.RoleAlreadyExistsException;
import ru.tsconsulting.errorHandling.notification_exceptions.UserAlreadyExistsException;
import ru.tsconsulting.repositories.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DepartmentHistoryRepository departmentHistoryRepository;
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final AccessHistoryRepository accessHistoryRepository;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RolesListRepository rolesListRepository;

    @Autowired
    public AdminController(DepartmentHistoryRepository departmentHistoryRepository,
                           EmployeeHistoryRepository employeeHistoryRepository, AccessHistoryRepository accessHistoryRepository,
                           UserRepository userRepository, RoleRepository roleRepository, RolesListRepository rolesListRepository) {
        this.departmentHistoryRepository = departmentHistoryRepository;
        this.employeeHistoryRepository = employeeHistoryRepository;
        this.accessHistoryRepository = accessHistoryRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.rolesListRepository = rolesListRepository;
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

    @ApiOperation(value = "Register new user")
    @RequestMapping(path = "/user", method = RequestMethod.POST)
    public String registerUser(@Validated @RequestBody User.UserDetails userDetails,
                                   HttpServletRequest request){
        if(userDetails.getUsername() == null || userDetails.getPassword() == null || userDetails.getEnabled() == null) {
            throw new ParameterNotSpecifiedException();
        }
        else if(userRepository.findByUsername(userDetails.getUsername()) != null) {
            throw new UserAlreadyExistsException(userDetails.getUsername());
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        userDetails.setPassword( encoder.encode(userDetails.getPassword()));
        userRepository.save(new User(userDetails));
        return "User with username " + userDetails.getUsername() + " successfully registered";
    }

    @ApiOperation(value = "Give privileges to user")
    @RequestMapping(path = "/user/role/grant", method = RequestMethod.POST)
    public String registerUser(@Validated @RequestBody String userName, String[] roles,
                               HttpServletRequest request){
        User user = new User();
        Role role = new Role();

        if(userName == null) {
            throw new UsernameNotFoundException(userName);
        }
        else if(roles == null) {
            return "Roles is null";
        }
        else {
            user = userRepository.findByUsername(userName);

            for(String r : roles) {
                if((role = roleRepository.findByName(r)) != null) {

                    RolesList rolesList = new RolesList();
                    rolesList.setUser(user);
                    rolesList.setRole(role);
                    rolesListRepository.save(rolesList);
                }
                else {
                    throw new RoleNotFoundException(r);
                }

            }
        }
        List<String> rolesOutput = new ArrayList<>(Arrays.asList(roles));
        return "User with username " + userName + " has been granted following roles:\n "
                 + rolesOutput;
    }

    @ApiOperation(value = "Create new role")
    @RequestMapping(path = "/user/role", method = RequestMethod.POST)
    public Role createRole(@Validated @RequestBody Role.RoleDetails roleDetails,
                             HttpServletRequest request){
        if(roleDetails.getName() == null) {
            throw new ParameterNotSpecifiedException();
        }
        else if(roleRepository.findByName(roleDetails.getName()) != null) {
            throw new RoleAlreadyExistsException(roleDetails.getName());
        }

        return roleRepository.save(new Role(roleDetails));
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
