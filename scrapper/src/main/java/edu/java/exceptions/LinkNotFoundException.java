package edu.java.exceptions;

public class LinkNotFoundException extends RuntimeException {
    public LinkNotFoundException(String exception) {
        super(exception);
    }

    public String getName() {
        return "Link Not Found";
    }
}
