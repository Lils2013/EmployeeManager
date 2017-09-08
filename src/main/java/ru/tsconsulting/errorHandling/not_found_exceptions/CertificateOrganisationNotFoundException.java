package ru.tsconsulting.errorHandling.not_found_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Certificate organisation not found.")
public class CertificateOrganisationNotFoundException extends EntityNotFoundException {
	public CertificateOrganisationNotFoundException(String certificateDetail) {
		super(certificateDetail);
	}
	@Override
	public String getMessage() {
		return "Certificate organisation [" + getEntityDetail() + "] not found.";
	}
}
