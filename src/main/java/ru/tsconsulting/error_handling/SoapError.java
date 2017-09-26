package ru.tsconsulting.error_handling;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ServiceFault", propOrder = {
        "code",
        "description"
})
public class SoapError {
    private String code;
    private String description;
    public SoapError() {
    }
    public SoapError(String code, String description) {
        this.code = code;
        this.description = description;
    }
    public String getDescription() {
        return description;
    }

    public String getErrors() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
