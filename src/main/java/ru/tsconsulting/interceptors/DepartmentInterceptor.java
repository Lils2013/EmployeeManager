package ru.tsconsulting.interceptors;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.DepartmentHistory;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.entities.EmployeeHistory;
import ru.tsconsulting.repositories.DepartmentHistoryRepository;
import ru.tsconsulting.repositories.DepartmentRepository;
import ru.tsconsulting.repositories.EmployeeRepository;
import ru.tsconsulting.repositories.EmployeeHistoryRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Aspect
@Component
public class DepartmentInterceptor {

    private final DepartmentHistoryRepository departmentHistoryRepository;
    private final DepartmentRepository departmentRepository;

    public DepartmentInterceptor(DepartmentHistoryRepository departmentHistoryRepository, DepartmentRepository departmentRepository) {
        this.departmentHistoryRepository = departmentHistoryRepository;
        this.departmentRepository = departmentRepository;
    }

    @Before("execution(* ru.tsconsulting.controllers.*.*(..))&&args(..,request)")
    void getIp(HttpServletRequest request){
        LocalDateTime date = LocalDateTime.now();
        System.err.println("IP-address: "+request.getRemoteAddr()+", Time: "+date);
    }

    private DepartmentHistory createRecord(LocalDateTime time, Long departmentId, String ipAddress, Long operationId) {
    	DepartmentHistory record = new DepartmentHistory();
    	record.setDateTime(time);
    	record.setDepartmentId(departmentId);
    	record.setIpAddress(ipAddress);
    	record.setOperationId(operationId);
    	return  record;
    }

    @Before("execution(* ru.tsconsulting.controllers.DepartmentsController.getDepartment(..))&&args(departmentId,..,request)")
    void beforeGetDepartment(Long departmentId, HttpServletRequest request)
    {
       departmentHistoryRepository.save(createRecord(LocalDateTime.now(), departmentId, request.getRemoteAddr(), 2L));
    }

	@Before("execution(* ru.tsconsulting.controllers.DepartmentsController.changeHierarchy(..))&&args(departmentId,..,request)")
	void beforeChangeHierarchy(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(LocalDateTime.now(), departmentId, request.getRemoteAddr(), 3L));
	}



}
