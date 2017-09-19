package ru.tsconsulting.errorHandling.not_found_exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "User not found.")
public class UserNotFoundException extends EntityNotFoundException {
	public UserNotFoundException(String userDetail) {
		super(userDetail);
	}
	@Override
	public String getMessage() {
		return "User [" + getEntityDetail() + "] not found.";
	}
}
