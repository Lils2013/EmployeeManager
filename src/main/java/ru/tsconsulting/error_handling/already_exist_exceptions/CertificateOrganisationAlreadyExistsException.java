package ru.tsconsulting.error_handling.already_exist_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT, reason = "Certificate organisation already exists.")
public class CertificateOrganisationAlreadyExistsException extends EntityAlreadyExistsException {
    public CertificateOrganisationAlreadyExistsException(String certificateOrganisationDetails) {
        super(certificateOrganisationDetails);
    }

    @Override
    public String getMessage() {
        return "Certificate organisation [" + getEntityDetail() + "] already exists.";
    }
}
