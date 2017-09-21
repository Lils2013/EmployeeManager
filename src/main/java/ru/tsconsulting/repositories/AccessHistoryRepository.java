package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.AccessHistory;
import ru.tsconsulting.entities.EmployeeHistory;

import java.time.LocalDateTime;
import java.util.List;

public interface AccessHistoryRepository extends JpaRepository<AccessHistory, Long> {

    List<AccessHistory> findByDateTimeBetweenOrderByIdAsc(LocalDateTime from, LocalDateTime to);

    List<AccessHistory> findByDateTimeAfterOrderByIdAsc(LocalDateTime from);

    List<AccessHistory> findByDateTimeBeforeOrderByIdAsc(LocalDateTime to);

    List<AccessHistory> findAllByOrderByIdAsc();
}
