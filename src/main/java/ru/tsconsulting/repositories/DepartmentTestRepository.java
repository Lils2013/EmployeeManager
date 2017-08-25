package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.DepartmentTest;

import java.util.List;

public interface DepartmentTestRepository extends JpaRepository<DepartmentTest, Long> {

    List<DepartmentTest> findByEmployees_Firstname(String name);

    List<DepartmentTest> findByParentDepartment_Id(Long id);

    List<DepartmentTest> findByParentDepartment_Name(String name);
}
