package com.escapeplanner.dto;

import com.escapeplanner.domain.enums.EstadoCliente;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

public record ClienteRequest(
        @NotBlank @Size(max = 150) String nombre,
        @NotBlank @Size(max = 30) String telefono,
        @Email @Size(max = 120) String email,
        @Size(max = 50) String canalContacto,
        @NotNull EstadoCliente estado
) {}