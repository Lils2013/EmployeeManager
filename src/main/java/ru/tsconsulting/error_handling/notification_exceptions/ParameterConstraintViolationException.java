package ru.tsconsulting.error_handling.notification_exceptions;

import java.util.Map;

public class ParameterConstraintViolationException extends RuntimeException {
    Map<String, String> violations;
    public ParameterConstraintViolationException(Map<String, String> violations) {
        this.violations = violations;
    }
    public String getMessage() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("List of constraint violations: ").append(System.getProperty("line.separator"));

        for (String key: violations.keySet()) {
            stringBuilder.append(key).append(System.getProperty("line.separator"));
                        stringBuilder.append("[").append(violations.get(key))
                                .append("]")
                                .append(System.getProperty("line.separator"));

        }
        return stringBuilder.toString();
    }
}
