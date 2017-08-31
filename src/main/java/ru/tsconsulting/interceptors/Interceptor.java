package ru.tsconsulting.interceptors;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.EmployeeHistory;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.EmployeeHistoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Aspect
@Component
public class Interceptor {

    private final EmployeeHistoryRepository employeeHistoryRepository;
    private final EmployeeRepository employeeRepository;

    public Interceptor(EmployeeHistoryRepository employeeHistoryRepository, EmployeeRepository employeeRepository) {
        this.employeeHistoryRepository = employeeHistoryRepository;
        this.employeeRepository = employeeRepository;
    }

    @Before("execution(* ru.tsconsulting.controllers.*.*(..))&&args(..,request)")
    void getIp(HttpServletRequest request){
        LocalDateTime date = LocalDateTime.now();
        System.err.println("IP-adress: "+request.getRemoteAddr()+", Time: "+date);
    }
    @Before("execution(* ru.tsconsulting.controllers.EmployeesController.getEmployee(..))&&args(employeeId,request)")
    void beforeGetEmployee(Long employeeId, HttpServletRequest request)
    {
        EmployeeHistory record = new EmployeeHistory();
        record.setEmployeeId(employeeId);
        record.setDateTime(LocalDateTime.now());
        record.setIpAddress(request.getRemoteAddr());
        employeeHistoryRepository.save(record);
    }

}
