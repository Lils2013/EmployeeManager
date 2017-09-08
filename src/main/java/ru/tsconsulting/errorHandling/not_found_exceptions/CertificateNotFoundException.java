package ru.tsconsulting.errorHandling.not_found_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Certificate not found.")
public class CertificateNotFoundException extends EntityNotFoundException {
	public CertificateNotFoundException(String certificateDetail) {
		super(certificateDetail);
	}
	@Override
	public String getMessage() {
		return "Certificate [" + getEntityDetail() + "] not found.";
	}
}
