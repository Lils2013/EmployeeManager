package ru.tsconsulting.error_handling.not_found_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Role not found.")
public class RoleNotFoundException extends EntityNotFoundException {
	public RoleNotFoundException(String roleDetail) {
		super(roleDetail);
	}
	@Override
	public String getMessage() {
		return "Role [" + getEntityDetail() + "] not found.";
	}
}
