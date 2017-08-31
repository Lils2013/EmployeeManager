package ru.tsconsulting.interceptors;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.History;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.HistoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Aspect
@Component
public class Interceptor {

    private final HistoryRepository historyRepository;
    private final EmployeeRepository employeeRepository;

    public Interceptor(HistoryRepository historyRepository, EmployeeRepository employeeRepository) {
        this.historyRepository = historyRepository;
        this.employeeRepository = employeeRepository;
    }

    @Before("execution(* ru.tsconsulting.controllers.*.*(..))&&args(..,request)")
    void getIp(HttpServletRequest request){
        LocalDateTime date = LocalDateTime.now();
        System.err.println("IP-adress: "+request.getRemoteAddr()+", Time: "+date);
    }
    @Before("execution(* ru.tsconsulting.controllers.EmployeesController.getEmployee(employeeId,request))")
    void beforeGetEmployee(Long employeeId, HttpServletRequest request)
    {
        Employee employee = employeeRepository.findById(employeeId);
        Department department = employee.getDepartment();
        History record = new History();
        record.setDateTime(LocalDateTime.now());
        record.setEmployee(employee);
        record.setDepartment(department);
        record.setIpAdress(request.getRemoteAddr());
        historyRepository.save(record);
    }
}
