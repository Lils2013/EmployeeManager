package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.DepartmentHistory;
import ru.tsconsulting.entities.EmployeeHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface DepartmentHistoryRepository extends JpaRepository<DepartmentHistory, Long> {

    List<DepartmentHistory> findByDepartmentIdAndDateTimeBetween(Long departmentId, LocalDateTime from, LocalDateTime to);

    List<DepartmentHistory> findByDepartmentIdAndDateTimeAfter(Long departmentId, LocalDateTime from);

    List<DepartmentHistory> findByDepartmentIdAndDateTimeBefore(Long departmentId, LocalDateTime to);

    List<DepartmentHistory> findByDepartmentId(Long departmentId);
}
