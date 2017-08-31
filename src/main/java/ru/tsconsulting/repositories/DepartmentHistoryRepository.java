package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.DepartmentHistory;

public interface DepartmentHistoryRepository extends JpaRepository<DepartmentHistory, Long> {
}
