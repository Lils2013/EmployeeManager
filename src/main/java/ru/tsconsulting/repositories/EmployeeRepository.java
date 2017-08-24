package ru.tsconsulting.repositories;

import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.EmployeeEntity;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    List<EmployeeEntity> findByLastname(String name);

    List<EmployeeEntity> findByFirstname(String name);
}
