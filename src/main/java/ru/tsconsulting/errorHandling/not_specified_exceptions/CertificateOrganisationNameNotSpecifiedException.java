package ru.tsconsulting.errorHandling.not_specified_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Certificate organisation name is not specified not specified.")
public class CertificateOrganisationNameNotSpecifiedException extends AttributeNotSpecifiedException {

    public String getMessage() {
        return "Certificate organisation name is not specified.";
    }
}