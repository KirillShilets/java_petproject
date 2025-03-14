package org.myproject.dto;

public class CustomErrorResponseDto {
    private int code;
    private String error;
    private String message;

    public CustomErrorResponseDto(int code, String error, String message) {
        this.code = code;
        this.error = error;
        this.message = message;
    }

    public CustomErrorResponseDto() {}

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
