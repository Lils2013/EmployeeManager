package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.AccessHistory;
import ru.tsconsulting.entities.DepartmentHistory;
import ru.tsconsulting.entities.EmployeeHistory;
import ru.tsconsulting.error_handling.RestStatus;
import ru.tsconsulting.error_handling.Status;
import ru.tsconsulting.repositories.AccessHistoryRepository;
import ru.tsconsulting.repositories.DepartmentHistoryRepository;
import ru.tsconsulting.repositories.EmployeeHistoryRepository;

import javax.jms.*;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final DepartmentHistoryRepository departmentHistoryRepository;
    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final AccessHistoryRepository accessHistoryRepository;
    private final JmsTemplate jmsTemplate;
    private final ConnectionFactory cf;

    @Autowired
    public AdminController(DepartmentHistoryRepository departmentHistoryRepository,
                           EmployeeHistoryRepository employeeHistoryRepository,
                           AccessHistoryRepository accessHistoryRepository, JmsTemplate jmsTemplate,
                           ConnectionFactory cf) {
        this.departmentHistoryRepository = departmentHistoryRepository;
        this.employeeHistoryRepository = employeeHistoryRepository;
        this.accessHistoryRepository = accessHistoryRepository;
        this.jmsTemplate = jmsTemplate;
        this.cf = cf;
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
            result.addAll(accessHistoryRepository.findByDateTimeBetweenOrderByIdAsc(fromDate, toDate));
        } else if (from != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            result.addAll(accessHistoryRepository.findByDateTimeAfterOrderByIdAsc(fromDate));
        } else if (to != null) {
            LocalDateTime toDate = LocalDateTime.parse(to);
            result.addAll(accessHistoryRepository.findByDateTimeBeforeOrderByIdAsc(toDate));
        } else {
            result.addAll(accessHistoryRepository.findAllByOrderByIdAsc());
        }
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

    @RequestMapping(path = "/soap", method = RequestMethod.POST)
    public void sendSoapRequest(@RequestParam(value = "queue") String queue,
                                @RequestParam(value = "soap") String soap,
                                  HttpServletRequest request) {
        jmsTemplate.send("SoapRequestQueue", session -> {
            TextMessage message = session.createTextMessage(soap);
            message.setJMSReplyTo(new ActiveMQQueue(queue));
            return message;
        });
    }

    @RequestMapping(path = "/soap", method = RequestMethod.GET)
    public String getSoapResponse(@RequestParam(value = "queue", required = true) String queue,
                                  HttpServletRequest request) {
        TextMessage receivedMessage = (TextMessage) jmsTemplate.receive(queue);
        String response = "Queue is empty";
        if (receivedMessage == null) {
            return response;
        }
        try {
            response = receivedMessage.getText();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return response;
    }

    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestStatus failedToParse(DateTimeParseException e) {
        return new RestStatus(Status.PARSE_FAIL, "DateTime could not be parsed");
    }
}
