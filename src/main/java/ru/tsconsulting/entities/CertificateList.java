package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "CERTIFICATE_LIST", schema = "TEST_B")
public class CertificateList {
	@Id
	@GenericGenerator(name="incrementGenerator1" , strategy="increment")
	@GeneratedValue(generator="incrementGenerator1")
    private long id;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate dateAcquired;
    @ManyToOne
    private  Certificate certificate;
	@ManyToOne
	private Employee employee;

	public CertificateList() {
	}

	public CertificateList(CertificateListDetails certificateListDetails) {
		this.dateAcquired = certificateListDetails.getDateAcquired();
	}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	@JsonGetter("certificate_id")
	public Long getCertificateId() {
		if (certificate == null) {
			return null;
		}
		return certificate.getId();
	}

	@JsonGetter("employee_id")
	public Long getEmployeeId() {
		if (certificate == null) {
			return null;
		}
		return certificate.getId();
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

        CertificateList that = (CertificateList) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Certificate getCertificate() {
		return certificate;
	}

	public void setCertificate(Certificate certificate) {
		this.certificate = certificate;
	}

	public static class CertificateListDetails {
		private LocalDate dateAcquired;
		private Long certificateId;
		private Long employeeId;

		public LocalDate getDateAcquired() {
			return dateAcquired;
		}

		public void setDateAcquired(LocalDate dateAcquired) {
			this.dateAcquired = dateAcquired;
		}

		public Long getCertificateId() {
			return certificateId;
		}

		public void setCertificateId(Long certificateId) {
			this.certificateId = certificateId;
		}

		public Long getEmployeeId() {
			return employeeId;
		}

		public void setEmployeeId(Long employeeId) {
			this.employeeId = employeeId;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			CertificateListDetails that = (CertificateListDetails) o;

			if (dateAcquired != null ? !dateAcquired.equals(that.dateAcquired) : that.dateAcquired != null)
				return false;
			if (certificateId != null ? !certificateId.equals(that.certificateId) : that.certificateId != null)
				return false;
			return employeeId != null ? employeeId.equals(that.employeeId) : that.employeeId == null;
		}

		@Override
		public int hashCode() {
			int result = dateAcquired != null ? dateAcquired.hashCode() : 0;
			result = 31 * result + (certificateId != null ? certificateId.hashCode() : 0);
			result = 31 * result + (employeeId != null ? employeeId.hashCode() : 0);
			return result;
		}
	}
}
