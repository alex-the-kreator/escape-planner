package com.escapeplanner.exception;

/**
 * Excepcion simple para indicar que un recurso solicitado
 * no existe en la base de datos.
 *
 * @author Alex M\u00E1rtin
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
