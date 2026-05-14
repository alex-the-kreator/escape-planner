package com.escapeplanner.dto;

import com.escapeplanner.domain.enums.EstadoCliente;// Importa el enum que representa los posibles estados de un cliente
import jakarta.validation.constraints.Email;// Validación para verificar que el formato del correo sea correcto
import jakarta.validation.constraints.NotBlank;// Validación para evitar textos vacíos o con solo espacios
import jakarta.validation.constraints.NotNull;// Validación para indicar que un dato no puede ser nulo
import jakarta.validation.constraints.Size;// Validación para limitar la cantidad máxima de caracteres

/**
 *
 * Record utilizado para transportar los datos
 * necesarios al registrar o actualizar un cliente.
 *
 * También incluye validaciones automáticas
 * usando Jakarta Validation.
 *
 */
public record ClienteRequest(

        // Número de cédula del cliente
        // No puede estar vacío y máximo 20 caracteres
        @NotBlank
        @Size(max = 20)
        String cedula,

        // Nombre completo del cliente
        // Obligatorio y máximo 150 caracteres
        @NotBlank
        @Size(max = 150)
        String nombre,

        // Número telefónico del cliente
        // Obligatorio y máximo 30 caracteres
        @NotBlank
        @Size(max = 30)
        String telefono,

        // Correo electrónico del cliente
        // Debe tener formato válido de email
        // Máximo 120 caracteres
        @Email
        @Size(max = 120)
        String email,

        // Canal por el cual llegó el cliente
        // Ejemplo: Instagram, WhatsApp, Facebook, Referido, etc.
        // Máximo 50 caracteres
        @Size(max = 50)
        String canalContacto,

        // Estado actual del cliente
        // No puede ser nulo
        @NotNull
        EstadoCliente estado

) {

}