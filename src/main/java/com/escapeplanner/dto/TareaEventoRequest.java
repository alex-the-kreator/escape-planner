package com.escapeplanner.dto;

import com.escapeplanner.domain.enums.EstadoTareaEvento;// Importa el enum que representa los posibles estados de una tarea de evento
import jakarta.validation.constraints.NotBlank;// Validación para indicar que un texto no puede estar vacío
import jakarta.validation.constraints.NotNull;// Validación para indicar que un dato no puede ser nulo
import jakarta.validation.constraints.Size;// Validación para limitar el tamaño máximo de un texto
import java.time.LocalDate;// Importa LocalDate para manejar fechas sin hora

// Record usado para transportar los datos necesarios al crear o actualizar una tarea de evento
public record TareaEventoRequest(


        @NotNull Long eventoId,
        @NotBlank @Size(max = 100) String nombre,
        @NotNull EstadoTareaEvento estado,

        // Fecha límite de la tarea, puede ser nula si no se define una fecha específica
        LocalDate fechaLimite

) {
    // El cuerpo queda vacío porque el record genera automáticamente constructor, getters, equals, hashCode y toString
}