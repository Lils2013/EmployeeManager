package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;
import ru.tsconsulting.details.CertificateDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Arrays;


@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "CERTIFICATE", schema = "TEST_B")
public class Certificate {
	@Id
	@GenericGenerator(name="incrementGenerator1" , strategy="increment")
	@GeneratedValue(generator="incrementGenerator1")
	private Long id;
	private String name;
	private Long serialNumber;
	private byte[] scan;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate dateAquired;

	public LocalDate getDateAquired() {
		return dateAquired;
	}

	public void setDateAquired(LocalDate dateAquired) {
		this.dateAquired = dateAquired;
	}

	@ManyToOne
	private CertificateOrganisation certificateOrganisation;


    public Certificate(){

    }

    public Certificate(CertificateDetails certificateDetails) {
    	setName(certificateDetails.getName());
    	setSerialNumber(certificateDetails.getSerialNumber());
    	setScan(certificateDetails.getScan());
    	setDateAquired(certificateDetails.getDateAcquired());
    }



	@JsonGetter("certificateorganisation_id")
	public Long getCertificateOrganisationId() {
		if (certificateOrganisation == null) {
			return null;
		}
		return certificateOrganisation.getId();
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

	public void setCertificateOrganisation(CertificateOrganisation certificateOrganisation) {
		this.certificateOrganisation = certificateOrganisation;
	}


    public Long getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(Long serialNumber) {
        this.serialNumber = serialNumber;
    }


    public byte[] getScan() {
        return scan;
    }

    public void setScan(byte[] scan) {
        this.scan = scan;
    }





    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Certificate that = (Certificate) o;

        if (id != that.id) return false;
        if (serialNumber != that.serialNumber) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (!Arrays.equals(scan, that.scan)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (int) (serialNumber ^ (serialNumber >>> 32));
        result = 31 * result + Arrays.hashCode(scan);
        return result;
    }
}
