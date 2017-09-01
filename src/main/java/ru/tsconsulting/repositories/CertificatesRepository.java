package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.tsconsulting.entities.Certificate;

public interface CertificatesRepository extends JpaRepository<Certificate, Long> {
	Certificate findById(Long id);

	Certificate save (@Param("certificate") Certificate certificate);

}
