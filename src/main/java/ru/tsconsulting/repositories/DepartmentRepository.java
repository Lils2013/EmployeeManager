package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.DepartmentEntity;

import java.util.List;
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

    List<DepartmentEntity> findByName(String name);

    List<DepartmentEntity> findByParent_Id(Long id);
}
