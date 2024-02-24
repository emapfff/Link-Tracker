package edu.java.clients;

import edu.java.responses.QuestionResponse;

public interface StackOverflowInterface {
    QuestionResponse fetchQuestion(long id);

}
