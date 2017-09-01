package ru.tsconsulting.details;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class OrganisationDetails {

	private String name;

	public OrganisationDetails() {
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

		OrganisationDetails that = (OrganisationDetails) o;

		return name != null ? name.equals(that.name) : that.name == null;
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}
