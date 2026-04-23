package com.escapeplanner.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PagoRequest(
        @NotNull Long eventoId,
        @NotNull @Positive BigDecimal monto,
        @NotNull LocalDate fecha,
        @NotBlank @Size(max = 50) String metodoPago,
        @Size(max = 255) String comprobante
) {
}
