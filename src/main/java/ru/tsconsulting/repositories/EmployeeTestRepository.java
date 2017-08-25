package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.EmployeeEntity;

import java.util.List;

public interface EmployeeTestRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByDepartment_Name(String name);

}
