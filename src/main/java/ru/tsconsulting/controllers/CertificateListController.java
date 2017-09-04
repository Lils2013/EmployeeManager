package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ru.tsconsulting.entities.Certificate;
import ru.tsconsulting.entities.CertificateList;
import ru.tsconsulting.errorHandling.CertificateOrganisationNotFoundException;
import ru.tsconsulting.repositories.CertificateListRepository;
import ru.tsconsulting.repositories.CertificateOrganisationRepository;
import ru.tsconsulting.repositories.CertificateRepository;
import ru.tsconsulting.repositories.EmployeeRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@RestController
@RequestMapping("/certificateList")
public class CertificateListController {
	private CertificateRepository certificateRepository;
	private EmployeeRepository employeeRepository;
	private CertificateListRepository certificateListRepository;

	@Autowired

	public CertificateListController(CertificateRepository certificateRepository, EmployeeRepository employeeRepository,
	                                 CertificateListRepository certificateListRepository) {
		this.certificateRepository = certificateRepository;
		this.employeeRepository = employeeRepository;
		this.certificateListRepository = certificateListRepository;
	}

	@ApiOperation(value = "Add row to certificate list")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful creation of the row"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
	@RequestMapping(method = RequestMethod.POST)
	public CertificateList createCertificate(@RequestBody CertificateList.CertificateListDetails certificateListDetails,
	                                     HttpServletRequest request) {
		CertificateList certificateList = new CertificateList(certificateListDetails);

		if (certificateListDetails.getCertificateId() != null) {
			if (certificateRepository.findById(certificateListDetails.getCertificateId()) != null) {
				certificateList.setCertificate(certificateRepository.findById(certificateListDetails.getCertificateId()));
			}
			else {
//				throw new CertificateNotFoundException(certificateListDetails.getCertificateId());
			}

		}
		else {
			throw new IllegalArgumentException("Incorrect id format(null)");
		}

		if (certificateListDetails.getEmployeeId() != null) {
			if (employeeRepository.findById(certificateListDetails.getEmployeeId()) != null) {
				certificateList.setEmployee(employeeRepository.findById(certificateListDetails.getEmployeeId()));
			}
			else {
//				throw new CertificateNotFoundException(certificateListDetails.getCertificateId());
			}

		}
		else {
			throw new IllegalArgumentException("Incorrect id format(null)");
		}

		certificateList.setDateAcquired(certificateListDetails.getDateAcquired());
		CertificateList result = certificateListRepository.save(certificateList);
		return result;
	}
}
