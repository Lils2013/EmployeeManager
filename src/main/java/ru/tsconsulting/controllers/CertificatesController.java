package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.*;
import ru.tsconsulting.errorHandling.*;
import ru.tsconsulting.repositories.CertificateListRepository;
import ru.tsconsulting.repositories.CertificateOrganisationRepository;
import ru.tsconsulting.repositories.CertificateRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/certificates")
public class CertificatesController {

	private final CertificateRepository certificateRepository;
	private final CertificateOrganisationRepository certificateOrganisationRepository;

	@Autowired
	public CertificatesController(CertificateRepository certificateRepository, CertificateOrganisationRepository certificateOrganisationRepository, CertificateListRepository certificateListRepository) {
		this.certificateRepository = certificateRepository;
		this.certificateOrganisationRepository = certificateOrganisationRepository;
	}

	@ApiOperation(value = "Create certificate using CertificateDetails")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful creation of certificate"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
	@RequestMapping(method = RequestMethod.POST)
	public Certificate createCertificate(@RequestBody Certificate.CertificateDetails certificateDetails,
	                                     HttpServletRequest request) {
		Certificate certificate = new Certificate(certificateDetails);

		if (certificateDetails.getCertificateOrganisationId() != null) {
			if (certificateOrganisationRepository.findById(certificateDetails.getCertificateOrganisationId()) != null) {
				certificate.setCertificateOrganisation(certificateOrganisationRepository.findById(certificateDetails.getCertificateOrganisationId()));
			}
			else {
				throw new CertificateOrganisationNotFoundException(certificateDetails.getCertificateOrganisationId());
			}


		}

		Certificate result = certificateRepository.save(certificate);
		return result;
	}

	@ApiOperation(value = "Edit certificate by id")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful edition of certificate"),
			@ApiResponse(code = 404, message = "Certificate with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
	@RequestMapping(path="/{certificateId}/edit",method = RequestMethod.POST)
	public Certificate editCertificate(@PathVariable Long certificateId,
	                             @RequestParam(value = "newName", required=false) String newName,
	                             @RequestParam(value = "newSerialNumber", required=false) Long newSerialNumber,
	                             @RequestParam(value = "newScan", required=false) byte[] newScan,
	                             @RequestParam(value = "newCertificateOrganisationId", required=false) Long newCertificateOrganisationId,
	                             HttpServletRequest request) {
		Certificate certificate = certificateRepository.findById(certificateId);
		if (certificate==null)
		{
			//throw new CertificateNotFoundException(certificateId);
		}
		CertificateOrganisation certificateOrganisation = certificateOrganisationRepository.findById(newCertificateOrganisationId);
		if (certificateOrganisation==null)
		{
			throw new CertificateOrganisationNotFoundException(newCertificateOrganisationId);
		}


		certificate.setName(newName);
		certificate.setSerialNumber(newSerialNumber);
		certificate.setScan(newScan);
		certificate.setCertificateOrganisation(certificateOrganisation);

		certificateRepository.save(certificate);
		return certificate;
	}

	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public RestError entityNotFound(EntityNotFoundException e) {
		return new RestError(1, e.getMessage());
	}
}
