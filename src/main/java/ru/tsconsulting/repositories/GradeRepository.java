package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.GradeEntity;

public interface GradeRepository extends JpaRepository<GradeEntity,Long>{
    GradeEntity findById(Long id);
}
