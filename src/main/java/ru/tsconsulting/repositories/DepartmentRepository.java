package ru.tsconsulting.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;

import java.util.List;
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Cacheable("subDepartments")
    List<Department> findByParentIdAndIsDismissedIsFalse(Long id);

    @Cacheable("department")
    Department findById(Long id);

    @Cacheable("notDismissedDepartment")
    Department findByIdAndIsDismissedIsFalse(Long id);

    Department findByName(String name);

    @Override
    @Caching(put={@CachePut(value="department", key="#result.id"),
            @CachePut(value="notDismissedDepartment", key="#result.id", condition="#result.isDismissed() == false")},
            evict={@CacheEvict(value="subDepartments", allEntries = true),
                    @CacheEvict(value="notDismissedDepartment", key="#result.id", condition="#result.isDismissed() == true")})
    Department save (Department department);

}
