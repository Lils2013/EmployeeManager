package ru.tsconsulting.error_handling;

import org.springframework.ws.soap.SoapFault;
import org.springframework.ws.soap.SoapFaultDetail;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import ru.tsconsulting.error_handling.not_found_exceptions.EntityNotFoundException;

import javax.xml.namespace.QName;

public class DetailSoapFaultDefinitionExceptionResolver extends SoapFaultMappingExceptionResolver {

    private static final QName CODE = new QName("code");
    private static final QName DESCRIPTION = new QName("description");

    @Override
    protected void customizeFault(Object endpoint, Exception ex, SoapFault fault) {
        if (ex instanceof EntityNotFoundException) {
            EntityNotFoundException exception = (EntityNotFoundException) ex;
            SoapFaultDetail detail = fault.addFaultDetail();
            detail.addFaultDetailElement(CODE).addText("ENTITY_NOT_FOUND");
            detail.addFaultDetailElement(DESCRIPTION).addText(exception.getMessage());
        }
    }

}
