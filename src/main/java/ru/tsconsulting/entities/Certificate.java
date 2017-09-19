package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.Arrays;


@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "CERTIFICATE")
public class Certificate {
	@Id
    @SequenceGenerator(name = "certificateGenerator", sequenceName = "certificate_sequence",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificateGenerator")
    private Long id;
    private String name;
	private Long serialNumber;

	private byte[] scan;
	@ManyToOne
	private CertificateOrganisation certificateOrganisation;

    public Certificate(){

    }

    public Certificate(CertificateDetails certificateDetails) {
    	setName(certificateDetails.getName());
    	setSerialNumber(certificateDetails.getSerialNumber());
    	setScan(certificateDetails.getScan());
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

		if (id != null ? !id.equals(that.id) : that.id != null) return false;
		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
		if (!Arrays.equals(scan, that.scan)) return false;
		return certificateOrganisation != null ? certificateOrganisation.equals(that.certificateOrganisation) : that.certificateOrganisation == null;
	}

	@Override
	public int hashCode() {
		int result = id != null ? id.hashCode() : 0;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(scan);
		result = 31 * result + (certificateOrganisation != null ? certificateOrganisation.hashCode() : 0);
		return result;
	}

	public static class CertificateDetails {

        @NotNull(message = "Name cannot be null.")
        @Size(min = 1, max = 32, message = "Invalid size of name string: must be between 1 and 32.")
		private String name;

        @Size(min = 1, max = 64, message = "Invalid size of serial number: must be between 1 and 64.")
		private Long serialNumber;

        @ApiModelProperty(value = "File that contains image of the certificate. Use null for now unless otherwise instructed")
		private byte[] scan;

        @ApiModelProperty(value = "id of certificate organisation", example="1",required=true)
		private Long certificateOrganisationId;

		public CertificateDetails(){

		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
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

		public Long getCertificateOrganisationId() {
			return certificateOrganisationId;
		}

		public void setCertificateOrganisationId(Long certificateOrganisationId) {
			this.certificateOrganisationId = certificateOrganisationId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			CertificateDetails that = (CertificateDetails) o;

			if (name != null ? !name.equals(that.name) : that.name != null) return false;
			if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
			if (!Arrays.equals(scan, that.scan)) return false;
			return certificateOrganisationId != null ? certificateOrganisationId.equals(that.certificateOrganisationId) : that.certificateOrganisationId == null;
		}

		@Override
		public int hashCode() {
			int result = name != null ? name.hashCode() : 0;
			result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
			result = 31 * result + Arrays.hashCode(scan);
			result = 31 * result + (certificateOrganisationId != null ? certificateOrganisationId.hashCode() : 0);
			return result;
		}
	}
}
