package ru.tsconsulting.interceptors;

import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.EmployeeHistory;
import ru.tsconsulting.repositories.EmployeeHistoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Aspect
@Component
public class EmployeeInterceptor {

    private final EmployeeHistoryRepository employeeHistoryRepository;

    @Autowired
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

    //GET EMPLOYEE

    @AfterThrowing("execution(* ru.tsconsulting.controllers.EmployeesController.getEmployee(..))&&args(employeeId,request)")
    void afterThrowingGetEmployee(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 11l, false);
        employeeHistoryRepository.save(record);
    }
    @AfterReturning("execution(* ru.tsconsulting.controllers.EmployeesController.getEmployee(..))&&args(employeeId,request)")
    void afterReturningGetEmployee(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 11l, true);
        employeeHistoryRepository.save(record);
    }

    //GET EMPLOYEE HISTORY

    @AfterThrowing("execution(* ru.tsconsulting.controllers.EmployeesController.getAudit(..))&&args(employeeId,request)")
    void afterThrowingGetHistory(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 3l, false);
        employeeHistoryRepository.save(record);
    }
    @AfterReturning("execution(* ru.tsconsulting.controllers.EmployeesController.getAudit(..))&&args(employeeId,request)")
    void afterReturningGetHistory(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 3l, true);
        employeeHistoryRepository.save(record);
    }

    //CREATE EMPLOYEE

    @AfterThrowing("execution(* ru.tsconsulting.controllers.EmployeesController.createEmployee(..))&&args(employee,request)")
    void afterThrowingCreateEmployee(Employee employee, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(0l, request, 7l, false);
        employeeHistoryRepository.save(record);
    }
    @AfterReturning(value = "execution(* ru.tsconsulting.controllers.EmployeesController.createEmployee(..))&&args(employee,request)", returning = "result")
    void afterReturningCreateEmployee(Employee employee, HttpServletRequest request, Object result)
    {
        EmployeeHistory record = createRecord(((Employee)result).getId(), request, 7l, true);
        employeeHistoryRepository.save(record);
    }

    //DELETE EMPLOYEE

    @AfterThrowing("execution(* ru.tsconsulting.controllers.EmployeesController.fireEmployee(..))&&args(employeeId,request)")
    void afterThrowingDeleteEmployee(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 10l, false);
        employeeHistoryRepository.save(record);
    }
    @AfterReturning(value = "execution(* ru.tsconsulting.controllers.EmployeesController.fireEmployee(..))&&args(employeeId,request)")
    void afterReturningDeleteEmployee(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 10l, true);
        employeeHistoryRepository.save(record);
    }

    //TRANSFER EMPLOYEE

    @AfterThrowing("execution(* ru.tsconsulting.controllers.EmployeesController.transferEmployee(..))&&args(employeeId,..,request)")
    void afterThrowingTransferEmployee(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 8l, false);
        employeeHistoryRepository.save(record);
    }
    @AfterReturning(value = "execution(* ru.tsconsulting.controllers.EmployeesController.transferEmployee(..))&&args(employeeId,..,request)")
    void afterReturningTransferEmployee(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 8l, true);
        employeeHistoryRepository.save(record);
    }

    //EDIT EMPLOYEE

    @AfterThrowing("execution(* ru.tsconsulting.controllers.EmployeesController.editEmployee(..))&&args(employeeId,..,request)")
    void afterThrowingEditEmployee(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 9l, false);
        employeeHistoryRepository.save(record);
    }
    @AfterReturning(value = "execution(* ru.tsconsulting.controllers.EmployeesController.editEmployee(..))&&args(employeeId,..,request)")
    void afterReturningEditEmployee(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = createRecord(employeeId, request, 9l, true);
        employeeHistoryRepository.save(record);
    }

}
