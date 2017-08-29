package ru.tsconsulting.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.Employee;

import javax.annotation.Resource;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Cacheable("employeesOfDepartment")
    List<Employee> findByDepartment_Id(Long id);

    @Cacheable("employee")
    Employee findById (Long id);

    @Override
    @CacheEvict(value="employeesOfDepartment",  key="#result.department.id")
    @CachePut(value="employee", key="#result.id")
    Employee save (Employee employee);

}
