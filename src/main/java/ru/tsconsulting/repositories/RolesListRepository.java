package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.RolesList;


public interface RolesListRepository extends JpaRepository<RolesList, Long> {
    RolesList findById(Long id);
}
