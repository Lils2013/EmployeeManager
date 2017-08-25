package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.PositionEntity;


public interface PositionRepository extends JpaRepository<PositionEntity, Long> {

    PositionEntity findById(Long id);
}