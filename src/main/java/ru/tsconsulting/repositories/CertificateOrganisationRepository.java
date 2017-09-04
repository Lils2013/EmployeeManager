package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.tsconsulting.entities.Certificate;
import ru.tsconsulting.entities.CertificateOrganisation;

public interface CertificateOrganisationRepository extends JpaRepository<CertificateOrganisation, Long> {
	CertificateOrganisation findById(Long id);
	CertificateOrganisation findByName(String name);
}
