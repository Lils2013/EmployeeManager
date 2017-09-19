package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.Role;
import ru.tsconsulting.entities.RolesList;
import ru.tsconsulting.entities.User;



public interface RolesListRepository extends JpaRepository<RolesList, Long> {
    RolesList findById(Long id);
    RolesList findByUserAndRole(User user, Role role);
}
