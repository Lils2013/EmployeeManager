package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.EmployeeHistory;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {

}
