package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.Grade;

import java.util.List;

public interface GradeRepository extends JpaRepository<Grade,Long>{
    Grade findById(Long id);

    Grade findByGrade(String name);

    List<Grade> findAllByOrderByIdAsc();
}
