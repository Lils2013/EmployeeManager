package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.DepartmentEntity;

import java.util.List;

public interface DepartmentTestRepository extends JpaRepository<DepartmentEntity, Long> {

    List<DepartmentEntity> findByEmployees_Firstname(String name);

    List<DepartmentEntity> findByParentDepartment_Name(String name);

}
