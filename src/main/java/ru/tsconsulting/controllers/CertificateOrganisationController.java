package ru.tsconsulting.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.CertificateOrganisation;
import ru.tsconsulting.errorHandling.EntityNotFoundException;
import ru.tsconsulting.errorHandling.Errors;
import ru.tsconsulting.errorHandling.RestError;
import ru.tsconsulting.repositories.CertificateListRepository;
import ru.tsconsulting.repositories.CertificateOrganisationRepository;
import ru.tsconsulting.repositories.CertificateRepository;


import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/certificateOrganisations")
public class CertificateOrganisationController {

	private final CertificateOrganisationRepository certificateOrganisationRepository;

	@Autowired
	public CertificateOrganisationController(CertificateRepository certificateRepository, CertificateOrganisationRepository certificateOrganisationRepository, CertificateListRepository certificateListRepository) {
		this.certificateOrganisationRepository = certificateOrganisationRepository;
	}


	@RequestMapping(method = RequestMethod.POST)
	public CertificateOrganisation createCertificateOrganisation(@RequestBody CertificateOrganisation.CertificateOrganisationDetails certificateOrganisationDetails,
	                                     HttpServletRequest request) {
		CertificateOrganisation certificateOrganisation = new CertificateOrganisation(certificateOrganisationDetails);

		if (certificateOrganisationDetails.getName() != null) {
			if (certificateOrganisationRepository.findByName(certificateOrganisationDetails.getName()) != null) {
				// throw new CertificateOrganisationAlreadyExistsException(certificateOrganisationDetails.getName());
			}
			else {
				// throw new CertificateOrganisationNameIsNotSpecifiedException(certificateOrganisationDetails.getName());
			}


		}

		CertificateOrganisation result = certificateOrganisationRepository.save(certificateOrganisation);
		return result;
	}
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public RestError entityNotFound(EntityNotFoundException e) {
		return new RestError(Errors.ENTITY_NOT_FOUND, e.getMessage());
	}
}
