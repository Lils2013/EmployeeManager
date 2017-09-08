package ru.tsconsulting.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.envers.Audited;

import javax.persistence.*;


@Entity
@Audited
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Table(name = "CERTIFICATEORGANISATION")
public class CertificateOrganisation {
    @Id
    @SequenceGenerator(name = "certificateOrganisationGenerator", sequenceName = "certorg_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificateOrganisationGenerator")
    private Long id;
    private String name;

	public CertificateOrganisation() {
	}

	public CertificateOrganisation(CertificateOrganisationDetails certificateOrganisationDetails) {
		this.setName(certificateOrganisationDetails.getName());
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CertificateOrganisation that = (CertificateOrganisation) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

	public static class CertificateOrganisationDetails {

		private String name;

		public CertificateOrganisationDetails() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			CertificateOrganisationDetails that = (CertificateOrganisationDetails) o;

			return name != null ? name.equals(that.name) : that.name == null;
		}

		@Override
		public int hashCode() {
			return name != null ? name.hashCode() : 0;
		}
	}
}
