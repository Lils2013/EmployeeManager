package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Certificate;
import ru.tsconsulting.entities.CertificateList;
import ru.tsconsulting.entities.CertificateOrganisation;
import ru.tsconsulting.errorHandling.Errors;
import ru.tsconsulting.errorHandling.RestError;
import ru.tsconsulting.errorHandling.not_found_exceptions.CertificateNotFoundException;
import ru.tsconsulting.errorHandling.not_found_exceptions.CertificateOrganisationNotFoundException;
import ru.tsconsulting.errorHandling.not_found_exceptions.EmployeeNotFoundException;
import ru.tsconsulting.errorHandling.not_found_exceptions.EntityNotFoundException;
import ru.tsconsulting.errorHandling.not_specified_exceptions.AttributeNotSpecifiedException;
import ru.tsconsulting.errorHandling.not_specified_exceptions.CertificateOrganisationNameNotSpecifiedException;
import ru.tsconsulting.errorHandling.notification_exceptions.CertificateOrganisationAlreadyExistsException;
import ru.tsconsulting.repositories.CertificateListRepository;
import ru.tsconsulting.repositories.CertificateOrganisationRepository;
import ru.tsconsulting.repositories.CertificateRepository;
import ru.tsconsulting.repositories.EmployeeRepository;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/certificates")
public class CertificatesController {

	private final CertificateRepository certificateRepository;
	private final CertificateOrganisationRepository certificateOrganisationRepository;
	private final EmployeeRepository employeeRepository;
	private final CertificateListRepository certificateListRepository;

	@Autowired
	public CertificatesController(CertificateRepository certificateRepository, CertificateOrganisationRepository certificateOrganisationRepository,
                                  CertificateListRepository certificateListRepository, EmployeeRepository employeeRepository) {
		this.certificateRepository = certificateRepository;
		this.certificateOrganisationRepository = certificateOrganisationRepository;
		this.employeeRepository = employeeRepository;
		this.certificateListRepository = certificateListRepository;
	}

	@ApiOperation(value = "Create certificate")
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
				throw new CertificateOrganisationNotFoundException(certificateDetails.getCertificateOrganisationId().toString());
			}
		}

		Certificate result = certificateRepository.save(certificate);
		return result;
	}

    @ApiOperation(value = "Return certificate")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of certificate"),
            @ApiResponse(code = 404, message = "Certificate with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path = "/{certificateId}", method = RequestMethod.GET)
    public Certificate getCertificate(@PathVariable Long certificateId,
                                HttpServletRequest request) {
        Certificate certificate = certificateRepository.findById(certificateId);

        if (certificate == null) {
            throw new CertificateNotFoundException(certificateId.toString());
        }

        return certificate;
    }

	@ApiOperation(value = "Edit certificate")
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
		if (certificate == null) {
			throw new CertificateNotFoundException(certificateId.toString());
		}

		if(newCertificateOrganisationId != null) {
            CertificateOrganisation certificateOrganisation = certificateOrganisationRepository.findById(newCertificateOrganisationId);
            if (certificateOrganisation==null)
            {
                throw new CertificateOrganisationNotFoundException(newCertificateOrganisationId.toString());
            }
            else {
                certificate.setCertificateOrganisation(certificateOrganisation);
            }
        }

        if(newSerialNumber != null) {
            certificate.setSerialNumber(newSerialNumber);
        }

        if(newScan != null) {
            certificate.setScan(newScan);
        }

        if(newName != null) {
            certificate.setName(newName);
        }

		certificateRepository.save(certificate);

		return certificate;
	}

    @RequestMapping(path="/organisation",method = RequestMethod.POST)
    public CertificateOrganisation createCertificateOrganisation(@RequestBody CertificateOrganisation.CertificateOrganisationDetails certificateOrganisationDetails,
                                                                 HttpServletRequest request) {
        CertificateOrganisation certificateOrganisation = new CertificateOrganisation(certificateOrganisationDetails);

        if (certificateOrganisationDetails.getName() != null) {
            if (certificateOrganisationRepository.findByName(certificateOrganisationDetails.getName()) != null) {
                 throw new CertificateOrganisationAlreadyExistsException(certificateOrganisationDetails.getName());
            }
            else {
                 throw new CertificateOrganisationNameNotSpecifiedException();
            }
        }

        CertificateOrganisation result = certificateOrganisationRepository.save(certificateOrganisation);
        return result;
    }

    @ApiOperation(value = "Add row to certificate list")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful addition of the row"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path="/list", method = RequestMethod.POST)
    public CertificateList addRow(@RequestBody CertificateList.CertificateListDetails certificateListDetails,
                                             HttpServletRequest request) {
        CertificateList certificateList = new CertificateList(certificateListDetails);

        if (certificateListDetails.getCertificateId() != null) {
            if (certificateRepository.findById(certificateListDetails.getCertificateId()) != null) {
                certificateList.setCertificate(certificateRepository.findById(certificateListDetails.getCertificateId()));
            } else {
				throw new CertificateNotFoundException(certificateListDetails.getCertificateId().toString());
            }
        } else {
            throw new IllegalArgumentException("Incorrect id format(null)");
        }

        if (certificateListDetails.getEmployeeId() != null) {
            if (employeeRepository.findById(certificateListDetails.getEmployeeId()) != null) {
                certificateList.setEmployee(employeeRepository.findById(certificateListDetails.getEmployeeId()));
            } else {
                throw new EmployeeNotFoundException(certificateListDetails.getEmployeeId().toString());
            }

        } else {
            throw new IllegalArgumentException("Incorrect id format(null)");
        }

        CertificateList result = certificateListRepository.save(certificateList);
        return result;
    }

        @ExceptionHandler(EntityNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public RestError entityNotFound (EntityNotFoundException e){
            return new RestError(Errors.ENTITY_NOT_FOUND, e.getMessage());
        }

    @ExceptionHandler(AttributeNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestError attributeNotSpecified(AttributeNotSpecifiedException e) {
        return new RestError(Errors.ATTRIBUTE_NOT_SPECIFIED, e.getMessage());
    }

    @ExceptionHandler(CertificateOrganisationAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.ALREADY_REPORTED)
    public RestError alreadyExists(CertificateOrganisationAlreadyExistsException e) {
        return new RestError(Errors.ALREADY_EXISTS, e.getMessage());
    }

}

