package edu.java.exceptions;

public class RepeatRegistrationException extends RuntimeException {
    public RepeatRegistrationException(String exception) {
        super(exception);
    }

    public String getName() {
        return "Repeat Registration";
    }
}
