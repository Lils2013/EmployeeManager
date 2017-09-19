package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.Role;
import ru.tsconsulting.entities.User;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(Long id);
    Role findByName(String name);
}
