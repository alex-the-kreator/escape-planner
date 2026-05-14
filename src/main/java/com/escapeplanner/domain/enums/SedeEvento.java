package com.escapeplanner.domain.enums;

/**
 * Sedes activas de Escape Room Colombia para eventos
 *
 * @author Alex Mártin
 */
public enum SedeEvento {
    CEDRITOS("Cedritos"),
    NIZA("Niza"),
    MULTIPLAZA("Multiplaza"),
    LAGO("Lago");

    private final String nombreVisible;

    SedeEvento(String nombreVisible) {
        this.nombreVisible = nombreVisible;
    }

    public String getNombreVisible() {
        return nombreVisible;
    }
}
