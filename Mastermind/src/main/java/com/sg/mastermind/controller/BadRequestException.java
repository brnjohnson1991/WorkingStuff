package com.sg.mastermind.controller;

/**
 *
 * @author BRNJO
 */
class BadRequestException extends Exception {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
