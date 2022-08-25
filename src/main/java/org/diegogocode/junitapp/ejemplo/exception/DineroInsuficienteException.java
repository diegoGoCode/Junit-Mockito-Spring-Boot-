package org.diegogocode.junitapp.ejemplo.exception;

public class DineroInsuficienteException extends RuntimeException{
    public DineroInsuficienteException(String message) {
        super(message);
    }
}
