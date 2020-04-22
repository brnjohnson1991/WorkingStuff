package com.sg.mastermind.dao;

/**
 *
 * @author BRNJO
 */
public class GameEmptyException extends Exception {

    public GameEmptyException(String message) {
        super(message);
    }

    public GameEmptyException(String message, Throwable cause) {
        super(message, cause);
    }

    
}
