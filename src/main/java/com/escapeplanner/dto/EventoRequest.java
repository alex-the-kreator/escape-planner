package com.escapeplanner.dto;

import com.escapeplanner.domain.enums.EstadoEvento;
import com.escapeplanner.domain.enums.TipoEvento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

// Este DTO agrupa los datos mínimos que necesita el backend para registrar o actualizar un evento desde el formulario
/**
 * DTO de entrada para altas y edición de eventos.
 *
 * @author Alex Mártin
 */
public record EventoRequest(
        @NotNull Long clienteId,
        @NotNull Long usuarioId,
        @NotNull TipoEvento tipo,
        @NotNull LocalDate fecha,
        @NotNull LocalTime horaInicio,
        @NotNull LocalTime horaFin,
        @NotNull @Positive Integer numPersonas,
        @NotNull EstadoEvento estado,
        @NotNull Boolean requiereBloqueo,
        @Size(max = 4000) String detallesLogisticos
) {
}
