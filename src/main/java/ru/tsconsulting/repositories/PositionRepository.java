package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.Position;

import java.util.List;


public interface PositionRepository extends JpaRepository<Position, Long> {

    Position findById(Long id);

    List<Position> findAllByOrderByIdAsc();
}