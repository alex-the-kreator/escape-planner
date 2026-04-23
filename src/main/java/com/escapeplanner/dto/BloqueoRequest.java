package com.escapeplanner.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record BloqueoRequest(
        @NotNull Long eventoId,
        @NotNull LocalDateTime fechaInicio,
        @NotNull LocalDateTime fechaFin,
        @Size(max = 4000) String observacion
) {
}
