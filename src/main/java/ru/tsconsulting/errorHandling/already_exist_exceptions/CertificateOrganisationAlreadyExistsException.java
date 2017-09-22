package ru.tsconsulting.errorHandling.already_exist_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.tsconsulting.errorHandling.not_found_exceptions.EntityNotFoundException;

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
