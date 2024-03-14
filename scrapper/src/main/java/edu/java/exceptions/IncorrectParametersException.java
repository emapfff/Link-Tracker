package edu.java.exceptions;

public class IncorrectParametersException extends RuntimeException {
    public IncorrectParametersException(String exception) {
        super(exception);
    }

    public String getName() {
        return "Incorrect Parameters";
    }
}
