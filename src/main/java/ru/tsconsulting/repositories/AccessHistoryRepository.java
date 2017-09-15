package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.AccessHistory;

public interface AccessHistoryRepository extends JpaRepository<AccessHistory, Long> {
}
