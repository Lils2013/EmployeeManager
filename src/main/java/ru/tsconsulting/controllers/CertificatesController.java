package ru.tsconsulting.controllers;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.tsconsulting.entities.Certificate;
import ru.tsconsulting.entities.CertificateList;
import ru.tsconsulting.entities.CertificateOrganisation;
import ru.tsconsulting.entities.Employee;
import ru.tsconsulting.error_handling.Status;
import ru.tsconsulting.error_handling.RestStatus;
import ru.tsconsulting.error_handling.already_exist_exceptions.EntityAlreadyExistsException;
import ru.tsconsulting.error_handling.not_found_exceptions.CertificateNotFoundException;
import ru.tsconsulting.error_handling.not_found_exceptions.CertificateOrganisationNotFoundException;
import ru.tsconsulting.error_handling.not_found_exceptions.EmployeeNotFoundException;
import ru.tsconsulting.error_handling.not_found_exceptions.EntityNotFoundException;
import ru.tsconsulting.error_handling.not_specified_exceptions.AttributeNotSpecifiedException;
import ru.tsconsulting.error_handling.not_specified_exceptions.CertificateNotSpecifiedException;
import ru.tsconsulting.error_handling.not_specified_exceptions.CertificateOrganisationNameNotSpecifiedException;
import ru.tsconsulting.error_handling.not_specified_exceptions.EmployeeNotSpecifiedException;
import ru.tsconsulting.error_handling.already_exist_exceptions.CertificateOrganisationAlreadyExistsException;
import ru.tsconsulting.repositories.CertificateListRepository;
import ru.tsconsulting.repositories.CertificateOrganisationRepository;
import ru.tsconsulting.repositories.CertificateRepository;
import ru.tsconsulting.repositories.EmployeeRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
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
	public Certificate createCertificate(@ApiParam(value = "CertificateOrganisationId, serialNumber - whole numbers in " +
            "the range of (0) to (1,0E19); Name - string with max length = 255 symbols; Scan - file with max size = " +
            "255 bytes.")@RequestBody Certificate.CertificateDetails certificateDetails,
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
    public Certificate getCertificate(
            @ApiParam(value = "Id of certificate, a whole number in the range of " +
                    "(0) to (1,0E19)", required = true) @PathVariable Long certificateId,
                                HttpServletRequest request) {
        Certificate certificate = certificateRepository.findById(certificateId);

        if (certificate == null) {
            throw new CertificateNotFoundException(certificateId.toString());
        }

        return certificate;
    }

    @ApiOperation(value = "Return all certificates")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of certificates"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(method = RequestMethod.GET)
    public List<Certificate> getAllCertificates(HttpServletRequest request) {
        return certificateRepository.findAll();
    }

	@ApiOperation(value = "Edit certificate")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "Successful edition of certificate"),
			@ApiResponse(code = 404, message = "Certificate with given id does not exist"),
			@ApiResponse(code = 500, message = "Internal server error")}
	)
	@RequestMapping(path="/{certificateId}/edit",method = RequestMethod.POST)
	public Certificate editCertificate(@ApiParam(value = "Id of certificate, a whole number in the range of (0) " +
            "to (1,0E19)", required = true) @PathVariable Long certificateId,
            @ApiParam(value = "New certificate name, string with max length = 255 symbols")
            @RequestParam(value = "newName", required=false) String newName,
            @ApiParam(value = "New serial number, a whole number in the range of (0) to (1,0E19)")
	        @RequestParam(value = "newSerialNumber", required=false) Long newSerialNumber,
            @ApiParam(value = "New scan, max size = 255 bytes")
	        @RequestParam(value = "newScan", required=false) byte[] newScan,
            @ApiParam(value = "New organization id, a whole number in the range of (0) to (1,0E19)")
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

    @ApiOperation(value = "Create certificate organisation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful creation of certificate organisation"),
            @ApiResponse(code = 404, message = "Certificate organisation with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path="/organisations",method = RequestMethod.POST)
    public CertificateOrganisation createCertificateOrganisation(@RequestBody CertificateOrganisation.CertificateOrganisationDetails certificateOrganisationDetails,
                                                                 HttpServletRequest request) {
        CertificateOrganisation certificateOrganisation = new CertificateOrganisation(certificateOrganisationDetails);

        if (certificateOrganisationDetails.getName() != null) {
            if (certificateOrganisationRepository.findByName(certificateOrganisationDetails.getName()) != null) {
                 throw new CertificateOrganisationAlreadyExistsException(certificateOrganisationDetails.getName());
            }
        } else {
            throw new CertificateOrganisationNameNotSpecifiedException();
        }

        CertificateOrganisation result = certificateOrganisationRepository.save(certificateOrganisation);
        return result;
    }

    @ApiOperation(value = "Return certificate organisation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of certificate organisation"),
            @ApiResponse(code = 404, message = "Certificate organisation with given id does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path="/organisations/{certificateOrganisationId}", method = RequestMethod.GET)
    public CertificateOrganisation getCertificateOrganisation(@ApiParam(value = "Certificate organization id, a whole " +
            "number in the range of (0) to (1,0E19)")@PathVariable Long certificateOrganisationId,
                                      HttpServletRequest request) {
        CertificateOrganisation certificateOrganisation = certificateOrganisationRepository.findById(certificateOrganisationId);

        if (certificateOrganisation == null) {
            throw new CertificateOrganisationNotFoundException(certificateOrganisationId.toString());
        }
        return certificateOrganisation;
    }

    @ApiOperation(value = "Return all certificate organisations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of certificate organisations"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path="/organisations", method = RequestMethod.GET)
    public List<CertificateOrganisation> getAllCertificateOrganisations(HttpServletRequest request) {
        return certificateOrganisationRepository.findAll();
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
            throw new EmployeeNotSpecifiedException();
        }

        if (certificateListDetails.getEmployeeId() != null) {
            if (employeeRepository.findById(certificateListDetails.getEmployeeId()) != null) {
                certificateList.setEmployee(employeeRepository.findById(certificateListDetails.getEmployeeId()));
            } else {
                throw new EmployeeNotFoundException(certificateListDetails.getEmployeeId().toString());
            }

        } else {
            throw new CertificateNotSpecifiedException();
        }

        CertificateList result = certificateListRepository.save(certificateList);
        return result;
    }

    @ApiOperation(value = "Find rows by employee id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval the row"),
            @ApiResponse(code = 500, message = "Internal server error")}
    )
    @RequestMapping(path="/list/find", method = RequestMethod.GET)
    public List<CertificateList> findByEmployeeOrCertificate( @ApiParam(value = "Id of an employee, a whole number " +
            "in the range of (0) to (1,0E19)")@RequestParam(required = false) Long employeeId,
            @ApiParam(value = "Certificate id, a whole number in the range of (0) to (1,0E19)")
            @RequestParam(required = false) Long certificateId,
                                               HttpServletRequest request) {
	    Employee employee;
	    Certificate certificate;
        List<CertificateList> certificateList;

	    if(employeeId != null && certificateId != null) {
	        employee = employeeRepository.findById(employeeId);
	        certificate = certificateRepository.findById(certificateId);
            certificateList = certificateListRepository.findByEmployeeAndCertificate(employee, certificate);
        }
        else if(employeeId != null) {
	        employee = employeeRepository.findById(employeeId);
	        certificateList = certificateListRepository.findByEmployee(employee);
        }
        else if(certificateId != null) {
	        certificate = certificateRepository.findById(certificateId);
	        certificateList = certificateListRepository.findByCertificate(certificate);
        }
        else {
	        throw new EmployeeNotSpecifiedException();
        }

        return certificateList;
    }



        @ExceptionHandler(EntityNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public RestStatus entityNotFound (EntityNotFoundException e){
            return new RestStatus(Status.ENTITY_NOT_FOUND, e.getMessage());
        }

    @ExceptionHandler(AttributeNotSpecifiedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public RestStatus attributeNotSpecified(AttributeNotSpecifiedException e) {
        return new RestStatus(Status.ATTRIBUTE_NOT_SPECIFIED, e.getMessage());
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public RestStatus alreadyExists(EntityAlreadyExistsException e) {
        return new RestStatus(Status.ALREADY_EXISTS, e.getMessage());
    }
}

