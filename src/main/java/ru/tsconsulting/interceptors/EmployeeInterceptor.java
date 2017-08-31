package ru.tsconsulting.interceptors;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
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

    @Around("execution(* ru.tsconsulting.controllers.EmployeesController.getEmployee(..))&&args(employeeId,request)")
    Object beforeGetEmployee(ProceedingJoinPoint proceedingJoinPoint, Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = new EmployeeHistory();
        record.setDateTime(LocalDateTime.now());
        record.setIpAddress(request.getRemoteAddr());
        record.setEmployeeId(employeeId);
        Object output = null;
        try {
            output = proceedingJoinPoint.proceed();
            System.out.println("SUCCESS");
        } catch (Throwable e) {
            System.out.println("FAIL");
        } finally {
            employeeHistoryRepository.save(record);
        }
        return output;
    }
}
