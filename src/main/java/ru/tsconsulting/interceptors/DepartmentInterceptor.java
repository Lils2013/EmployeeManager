package ru.tsconsulting.interceptors;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.DepartmentHistory;
import ru.tsconsulting.repositories.DepartmentHistoryRepository;
import ru.tsconsulting.repositories.DepartmentRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;


@Aspect
@Component
public class DepartmentInterceptor {

    private final DepartmentHistoryRepository departmentHistoryRepository;

    public DepartmentInterceptor(DepartmentHistoryRepository departmentHistoryRepository, DepartmentRepository departmentRepository) {
        this.departmentHistoryRepository = departmentHistoryRepository;
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


	//GetDepartment
    @AfterReturning("execution(* ru.tsconsulting.controllers.DepartmentsController.getDepartment(..))&&args(departmentId,..,request)")
    void afterReturningGetDepartment(Long departmentId, HttpServletRequest request)
    {
       departmentHistoryRepository.save(createRecord(departmentId,request,12L, true));
    }
	@AfterThrowing("execution(* ru.tsconsulting.controllers.DepartmentsController.getDepartment(..))&&args(departmentId,..,request)")
	void afterThrowingGetDepartment(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,12L, false));
	}


	//ChangeHierarchy
	@AfterReturning("execution(* ru.tsconsulting.controllers.DepartmentsController.changeHierarchy(..))&&args(departmentId,..,request)")
	void afterReturningChangeHierarchy(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,5L, true));
	}
	@AfterThrowing("execution(* ru.tsconsulting.controllers.DepartmentsController.changeHierarchy(..))&&args(departmentId,..,request)")
	void afterThrowingChangeHierarchy(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,5L, false));
	}


	//EmployeeByDep
	@AfterReturning("execution(* ru.tsconsulting.controllers.DepartmentsController.employeeByDepartment(..))&&args(departmentId,..,request)")
	void afterReturningEmployeeByDep(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,2L, true));
	}
	@AfterThrowing("execution(* ru.tsconsulting.controllers.DepartmentsController.employeeByDepartment(..))&&args(departmentId,..,request)")
	void afterThrowingEmployeeByDep(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,2L, false));
	}


	//FindSubDeps
	@AfterReturning("execution(* ru.tsconsulting.controllers.DepartmentsController.findSubDeps(..))&&args(departmentId,..,request)")
	void afterReturningFindSubDeps(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,1L, true));
	}

	@AfterThrowing("execution(* ru.tsconsulting.controllers.DepartmentsController.findSubDeps(..))&&args(departmentId,..,request)")
	void afterThrowingFindSubDeps(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,1L, false));
	}


	//CreateDepartment
	@AfterReturning("execution(* ru.tsconsulting.controllers.DepartmentsController.createDepartment(..))&&args(department,..,request)")
	void afterReturningCreateDepartment(Department department, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(department.getId(),request,4L, true));
	}

	@AfterThrowing("execution(* ru.tsconsulting.controllers.DepartmentsController.createDepartment(..))&&args(department,..,request)")
	void afterThrowingCreateDepartment(Department department, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(department.getId(),request,4L, false));
	}


	//DeleteDepartment
	@AfterReturning("execution(* ru.tsconsulting.controllers.DepartmentsController.deleteDepartment(..))&&args(departmentId,..,request)")
	void afterReturningCreateDepartment(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,6L, true));
	}

	@AfterThrowing("execution(* ru.tsconsulting.controllers.DepartmentsController.createDepartment(..))&&args(departmentId,..,request)")
	void afterThrowingCreateDepartment(Long departmentId, HttpServletRequest request)
	{
		departmentHistoryRepository.save(createRecord(departmentId,request,6L, false));
	}

}
