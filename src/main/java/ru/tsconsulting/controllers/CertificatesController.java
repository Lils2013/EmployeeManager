package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.details.CertificateDetails;
import ru.tsconsulting.details.EmployeeDetails;
import ru.tsconsulting.entities.Certificate;
import ru.tsconsulting.entities.Department;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.errorHandling.*;
import ru.tsconsulting.repositories.CertificateListRepository;
import ru.tsconsulting.repositories.CertificateOrganisationRepository;
import ru.tsconsulting.repositories.CertificatesRepository;
import ru.tsconsulting.repositories.EmployeeRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/certificates")
public class CertificatesController {

	private final CertificatesRepository certificatesRepository;
	private final CertificateOrganisationRepository certificateOrganisationRepository;
	private final CertificateListRepository certificateListRepository;

	@Autowired
	public CertificatesController(CertificatesRepository certificatesRepository, CertificateOrganisationRepository certificateOrganisationRepository, CertificateListRepository certificateListRepository) {
		this.certificatesRepository = certificatesRepository;
		this.certificateOrganisationRepository = certificateOrganisationRepository;
		this.certificateListRepository = certificateListRepository;
	}


	@RequestMapping(method = RequestMethod.POST)
	public Certificate createEmployee(@RequestBody CertificateDetails certificateDetails,
	                               HttpServletRequest request) {
		Certificate certificate = new Certificate(certificateDetails);
		if (certificateDetails.getCertificateOrganisationId() != null) {
			if (certificateOrganisationRepository.findById(certificateDetails.getCertificateOrganisationId()) == null) {
				throw new CertificateOrganisationNotFoundException(certificateDetails.getCertificateOrganisationId());
			} else {
				certificate.setCertificateOrganisation(certificateOrganisationRepository.findById(certificateDetails.getCertificateOrganisationId()));
			}
		}

		Certificate result = certificatesRepository.save(certificate);
		return result;
	}
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public RestError entityNotFound(EntityNotFoundException e) {
		return new RestError(1, e.getMessage());
	}
}
