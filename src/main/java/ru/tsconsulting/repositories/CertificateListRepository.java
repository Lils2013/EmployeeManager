package ru.tsconsulting.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import ru.tsconsulting.entities.Certificate;
import ru.tsconsulting.entities.CertificateList;
import ru.tsconsulting.entities.Employee;

public interface CertificateListRepository extends JpaRepository<CertificateList, Long>{
	CertificateList findById(Long id);



}
