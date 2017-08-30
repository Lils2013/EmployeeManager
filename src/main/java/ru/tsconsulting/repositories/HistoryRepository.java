package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.History;

public interface HistoryRepository extends JpaRepository<History, Long> {

}
