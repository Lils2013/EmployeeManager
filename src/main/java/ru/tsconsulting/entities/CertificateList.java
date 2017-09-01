package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;

@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "CERTIFICATE_LIST", schema = "TEST_B")
public class CertificateList {
	@Id
	@GenericGenerator(name="incrementGenerator1" , strategy="increment")
	@GeneratedValue(generator="incrementGenerator1")
    private long id;

    @ManyToOne
    private  Certificate certificate;




	@ManyToOne
    private Employee employee;


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


}
