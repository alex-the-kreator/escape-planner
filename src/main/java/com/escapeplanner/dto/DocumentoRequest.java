package com.escapeplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record DocumentoRequest(
        @NotNull Long eventoId,
        @NotBlank @Size(max = 150) String tipo,
        @NotBlank @Size(max = 150) String nombre,
        @Size(max = 255) String url,
        @NotNull LocalDate fechaRegistro
) {
}
