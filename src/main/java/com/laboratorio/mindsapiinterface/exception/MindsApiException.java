package com.laboratorio.mindsapiinterface.exception;

/**
 *
 * @author Rafael
 * @version 1.2
 * @created 10/07/2024
 * @updated 17/12/2025
 */
public class MindsApiException extends RuntimeException {
    private final Throwable causaOriginal;
    
    public MindsApiException(String message) {
        super(message);
        this.causaOriginal = null;
    }
    
    public MindsApiException(String message, Throwable causaOriginal) {
        super(message);
        this.causaOriginal = causaOriginal;
    }
    
    @Override
    public String getMessage() {
        if (this.causaOriginal != null) {
            return super.getMessage() + " | Causa original: " + this.causaOriginal.getMessage();
        }
        
        return super.getMessage();
    }
}