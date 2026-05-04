package com.escapeplanner.exception;

/**
 * Excepción simple para reglas de negocio que impiden completar
 * una operación aunque los datos técnicos existan.
 *
 * @author Alex Mártin
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
