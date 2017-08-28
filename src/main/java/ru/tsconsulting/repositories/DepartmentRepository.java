package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.Department;

import java.util.List;
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    List<Department> findByParent_Id(Long id);

    Department findById(Long id);
}
