package edu.java.exceptions;

public class AbsentChatException extends RuntimeException {
    public AbsentChatException(String exception) {
        super(exception);
    }

    public String getName() {
        return "Absent Chat";
    }
}
