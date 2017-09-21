package ru.tsconsulting.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import ru.tsconsulting.entities.Employee;

import javax.annotation.Resource;
import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Cacheable("employeesOfDepartment")
    List<Employee> findByDepartmentIdAndIsFiredIsFalse(Long id);

    @Cacheable("employeesOfDepartment")
    List<Employee> findByDepartmentIdAndIsFiredIsFalseOrderByIdAsc(Long id);

    @Cacheable("employee")
    Employee findById (Long id);

    @Cacheable("notFiredEmployee")
    Employee findByIdAndIsFiredIsFalse (Long id);

    Employee findByIdAndIsFiredIsTrue (Long id);

    Employee findByUsername (String username);

    List<Employee> findByFirstnameAndLastname(String firstName, String lastName);

    List<Employee> findByFirstname (String firstName);

    List<Employee> findByLastname (String lastName);


    @Override
    @Caching(put={@CachePut(value="employee", key="#result.id"),
            @CachePut(value="notFiredEmployee", key="#result.id", condition="#result.isFired() == false")},
    evict={@CacheEvict(value="employeesOfDepartment", allEntries = true),
            @CacheEvict(value="notFiredEmployee",  key="#result.id", condition="#result.isFired() == true")})
    Employee save (@Param("employee") Employee employee);

    List<Employee> findAllByOrderByIdAsc();

}
