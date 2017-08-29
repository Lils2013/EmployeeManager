package ru.tsconsulting.repositories;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;

import java.util.List;
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByParent_Id(Long id);

    @Cacheable("department")
    Department findById(Long id);

    @Override
    @CachePut(value="department", key="#result.id")
    Department save (Department department);

}
