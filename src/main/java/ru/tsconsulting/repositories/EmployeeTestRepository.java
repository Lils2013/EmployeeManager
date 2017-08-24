package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.EmployeeTest;

import java.util.List;

public interface EmployeeTestRepository extends JpaRepository<EmployeeTest, Long> {

    List<EmployeeTest> findByDepartment_Name(String name);
}
