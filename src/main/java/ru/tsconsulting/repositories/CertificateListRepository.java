package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.tsconsulting.entities.Certificate;
import ru.tsconsulting.entities.CertificateList;
import ru.tsconsulting.entities.Employee;

import java.util.List;

public interface CertificateListRepository extends JpaRepository<CertificateList, Long>{
	CertificateList findById(Long id);

	List<CertificateList> findByEmployee(Employee employee);

	List<CertificateList> findByCertificate(Certificate certificate);

	List<CertificateList> findByEmployeeAndCertificate(Employee employee, Certificate certificate);
}
