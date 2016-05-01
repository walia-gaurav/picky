package org.cmu.picky.model;

/**
 * Represents an error. At the moment only contains a message.
 */
public class Error {

    private String message = "";

    public Error() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
