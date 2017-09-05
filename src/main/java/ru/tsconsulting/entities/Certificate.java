package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
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
	private Long testValue;
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
		private String name;
		private Long serialNumber;
		private byte[] scan;
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
