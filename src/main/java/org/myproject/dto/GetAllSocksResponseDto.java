package org.myproject.dto;

public class GetAllSocksResponseDto {
    private int size;
    private int statusCode;
    private String message;

    public GetAllSocksResponseDto(int statusCode, String message,int size) {
        this.size = size;
        this.statusCode = statusCode;
        this.message = message;
    }

    public GetAllSocksResponseDto(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
