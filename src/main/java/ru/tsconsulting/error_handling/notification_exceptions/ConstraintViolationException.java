package ru.tsconsulting.error_handling.notification_exceptions;

import javax.validation.ConstraintViolation;
import java.util.Set;

public class ConstraintViolationException extends RuntimeException {
    Set<ConstraintViolation<Object>> violations;
    public ConstraintViolationException(Set<ConstraintViolation<Object>> violations) {
        this.violations = violations;
    }
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("List of constraint violations: ").append(System.getProperty("line.separator"));

        for (ConstraintViolation<Object> c : violations) {
            stringBuilder.append(c.getMessage()).append(System.getProperty("line.separator"))
                    .append("[").append(c.getInvalidValue())
                    .append("]")
                    .append(System.getProperty("line.separator"));
        }
        return stringBuilder.toString();
    }
}
