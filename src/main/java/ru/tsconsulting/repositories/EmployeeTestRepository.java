package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.EmployeeTest;

import java.util.List;

/**
 * Created by avtsoy on 24.08.2017.
 */
public interface EmployeeTestRepository extends JpaRepository<EmployeeTest, Long> {

    List<EmployeeTest> findByDepartment_Name(String name);
}
