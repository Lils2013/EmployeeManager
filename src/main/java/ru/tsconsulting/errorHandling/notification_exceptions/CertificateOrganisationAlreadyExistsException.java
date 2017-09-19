package ru.tsconsulting.errorHandling.notification_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.tsconsulting.errorHandling.not_found_exceptions.EntityNotFoundException;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED, reason = "Certificate organisation already exists.")
public class CertificateOrganisationAlreadyExistsException extends EntityNotFoundException {
    public CertificateOrganisationAlreadyExistsException(String certificateDetails) {
        super(certificateDetails);
    }

    @Override
    public String getMessage() {
        return "Certificate organisation [" + getEntityDetail() + "] already exists.";
    }
}
