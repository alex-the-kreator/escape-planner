package com.escapeplanner.dto;

import com.escapeplanner.domain.enums.EstadoTareaEvento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TareaEventoRequest(
        @NotNull Long eventoId,
        @NotBlank @Size(max = 100) String nombre,
        @NotNull EstadoTareaEvento estado,
        LocalDate fechaLimite
) {
}
