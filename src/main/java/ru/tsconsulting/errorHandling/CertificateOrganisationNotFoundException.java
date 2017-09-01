package ru.tsconsulting.errorHandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Certificate organisation not found.")
public class CertificateOrganisationNotFoundException extends EntityNotFoundException {
	public CertificateOrganisationNotFoundException(long entityId) {
		super(entityId);
	}
	@Override
	public String getMessage() {
		return "Certificate organisation [" + getEntityId() + "] not found.";
	}
}
