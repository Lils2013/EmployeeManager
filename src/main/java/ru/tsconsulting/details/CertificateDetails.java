package ru.tsconsulting.details;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.Arrays;

public class CertificateDetails {

	private String name;
	private Long serialNumber;
	private byte[] scan;
	private Long certificateOrganisationId;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate dateAcquired;

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

	public LocalDate getDateAcquired() {
		return dateAcquired;
	}

	public void setDateAcquired(LocalDate dateAcquired) {
		this.dateAcquired = dateAcquired;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		CertificateDetails that = (CertificateDetails) o;

		if (name != null ? !name.equals(that.name) : that.name != null) return false;
		if (serialNumber != null ? !serialNumber.equals(that.serialNumber) : that.serialNumber != null) return false;
		if (!Arrays.equals(scan, that.scan)) return false;
		if (certificateOrganisationId != null ? !certificateOrganisationId.equals(that.certificateOrganisationId) : that.certificateOrganisationId != null)
			return false;
		return dateAcquired != null ? dateAcquired.equals(that.dateAcquired) : that.dateAcquired == null;
	}

	@Override
	public int hashCode() {
		int result = name != null ? name.hashCode() : 0;
		result = 31 * result + (serialNumber != null ? serialNumber.hashCode() : 0);
		result = 31 * result + Arrays.hashCode(scan);
		result = 31 * result + (certificateOrganisationId != null ? certificateOrganisationId.hashCode() : 0);
		result = 31 * result + (dateAcquired != null ? dateAcquired.hashCode() : 0);
		return result;
	}
}
