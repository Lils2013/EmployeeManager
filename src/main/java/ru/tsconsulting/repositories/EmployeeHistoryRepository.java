package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.EmployeeHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface EmployeeHistoryRepository extends JpaRepository<EmployeeHistory, Long> {

    List<EmployeeHistory> findByEmployeeIdAndDateTimeBetween(Long employeeId, LocalDateTime from, LocalDateTime to);

    List<EmployeeHistory> findByEmployeeIdAndDateTimeAfter(Long employeeId, LocalDateTime from);

    List<EmployeeHistory> findByEmployeeIdAndDateTimeBefore(Long employeeId, LocalDateTime to);

    List<EmployeeHistory> findByEmployeeId(Long employeeId);
}
