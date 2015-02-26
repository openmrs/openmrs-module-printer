package org.openmrs.module.printer;

public class UnableToPrintException extends Exception {

    private static final long serialVersionUID = 1L;

    public UnableToPrintException() {
        super();
    }

    public UnableToPrintException(String message) {
        super(message);
    }

    public UnableToPrintException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
