package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.Grade;

public interface GradeRepository extends JpaRepository<Grade,Long>{
    Grade findById(Long id);
}
