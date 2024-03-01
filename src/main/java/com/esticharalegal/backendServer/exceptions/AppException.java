package com.esticharalegal.backendServer.exceptions;


import org.springframework.http.HttpStatus;

public class AppException extends Throwable {

    private  HttpStatus status;
    public AppException() {

    }
    public AppException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
