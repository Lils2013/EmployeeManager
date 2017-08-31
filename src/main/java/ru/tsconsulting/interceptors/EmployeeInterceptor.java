package ru.tsconsulting.interceptors;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.EmployeeHistory;
import ru.tsconsulting.repositories.EmployeeHistoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class EmployeeInterceptor {

    private final EmployeeHistoryRepository employeeHistoryRepository;

    public EmployeeInterceptor(EmployeeHistoryRepository employeeHistoryRepository) {
        this.employeeHistoryRepository = employeeHistoryRepository;
    }

    private EmployeeHistory createRecord( Long employeeId, HttpServletRequest request, Long operationId,  Boolean status)
    {
        EmployeeHistory record = new EmployeeHistory();
        record.setDateTime(LocalDateTime.now());
        record.setIpAddress(request.getRemoteAddr());
        record.setEmployeeId(employeeId);
        record.setOperationId(operationId);
        record.setIsSuccessful(status);
        return record;
    }

    @AfterThrowing("execution(* ru.tsconsulting.controllers.EmployeesController.getEmployee(..))&&args(employeeId,request)")
    void getEmployeeThrow(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 11l, false);
        employeeHistoryRepository.save(record);
    }
    @AfterReturning("execution(* ru.tsconsulting.controllers.EmployeesController.getEmployee(..))&&args(employeeId,request)")
    void getEmployeeReturn(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 11l, true);
        employeeHistoryRepository.save(record);
    }

//    @AfterReturning("execution(* ru.tsconsulting.controllers.EmployeesController.getEmployee(..))&&args(employeeId,request)")
//    void employeeCreate(Long employeeId, HttpServletRequest request)
//    {
//        EmployeeHistory record = createRecord(employeeId, request, 11l, true);
//        employeeHistoryRepository.save(record);
//    }
}
