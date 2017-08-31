package ru.tsconsulting.interceptors;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.DepartmentHistory;
import ru.tsconsulting.repositories.DepartmentHistoryRepository;
import ru.tsconsulting.repositories.DepartmentRepository;

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

    private DepartmentHistory createRecord( Long departmentId, HttpServletRequest request, Long operationId, Boolean status) {
    	DepartmentHistory record = new DepartmentHistory();
        record.setDateTime(LocalDateTime.now());
    	record.setDepartmentId(departmentId);
        record.setIpAddress(request.getRemoteAddr());
    	record.setOperationId(operationId);
    	record.setIsSuccessful(status);
    	return  record;
    }

    @AfterReturning("execution(* ru.tsconsulting.controllers.DepartmentsController.getDepartment(..))&&args(departmentId,..,request)")
    void afterReturningGetDepartment(Long departmentId, HttpServletRequest request)
    {
       departmentHistoryRepository.save(createRecord(departmentId,request,666l, true));
    }
	@AfterThrowing("execution(* ru.tsconsulting.controllers.DepartmentsController.getDepartment(..))&&args(departmentId,..,request)")
	void afterThrowingGetDepartment(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,666l, true));
	}
}
