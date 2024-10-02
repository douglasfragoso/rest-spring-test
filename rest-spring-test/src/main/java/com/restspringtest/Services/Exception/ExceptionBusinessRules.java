package com.restspringtest.Services.Exception;

public class ExceptionBusinessRules extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public ExceptionBusinessRules(String message) {
        super(message);
    }
    
}   