package org.myproject.exception;

public class AdvancedRuntimeException extends RuntimeException {
    private final int code;
    private final String error;

    public AdvancedRuntimeException(int code, String error, String message) {
        super(message);
        this.code = code;
        this.error = error;
    }

    public int getCode() {
        return code;
    }

    public String getError() {
        return error;
    }
}
