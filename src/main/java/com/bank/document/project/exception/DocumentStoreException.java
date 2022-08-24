package com.bank.document.project.exception;

public class DocumentStoreException extends RuntimeException{

    public DocumentStoreException(String message){
        super(message);
    }

    public DocumentStoreException(String message, Throwable cause){
        super(message, cause);
    }
}
