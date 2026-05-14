package com.escapeplanner.exception;

/**
 * Excepción simple para indicar que un recurso solicitado
 * no existe en la base de datos.
 *
 * @author Alex Mártin
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
