package com.escapeplanner.exception;

import org.springframework.http.HttpStatus; // Representa los códigos de estado HTTP que se devolverán en las respuestas
import org.springframework.http.ResponseEntity;// Permite construir respuestas HTTP con estado y cuerpo personalizado
import org.springframework.validation.FieldError;// Representa errores específicos de campos cuando falla una validación
import org.springframework.web.bind.MethodArgumentNotValidException;// Excepción que se lanza cuando fallan las validaciones de un DTO con @Vali
import org.springframework.web.bind.annotation.ExceptionHandler;// Permite indicar qué método manejará una excepción específica
import org.springframework.web.bind.annotation.RestControllerAdvice;// Permite manejar excepciones de forma global para todos los controladores REST
import java.time.LocalDateTime;// Permite incluir la fecha y hora en la respuesta de error
import java.util.LinkedHashMap;// Mantiene el orden de inserción de los datos dentro del mapa de respuesta
import java.util.Map;// Estructura usada para construir respuestas de error en formato clave-valor

/**
 * Manejador global de excepciones del backend.
 *
 * Esta clase centraliza las respuestas de error del sistema para evitar
 * repetir manejo de excepciones dentro de cada controlador.
 *
 * De esta manera, cuando ocurre un error controlado, el sistema responde
 * con una estructura  que incluye estado, mensaje, tipo de error
 * y fecha/hora del evento.
 *
 * @author Alex Mártin
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    //Maneja los errores cuando un recurso no existpor ejemplo, cuando se intenta buscar un cliente, usuario o eventO con un ID que no está registrado en la base de datos.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> manejarResourceNotFound(
            ResourceNotFoundException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(construirRespuesta(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    /**
     * Maneja errores relacionados con reglas de negocio.
     *
     * Por ejemplo, cuando se intenta confirmar un evento que tiene conflicto
     * de horario con otro vento ya registrado.
     *
     * Retorna un estado HTTP 400 BAD REQUEST porque la solicitud no cumple
     * con una regla válida del sistema.
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> manejarBusinessException(
            BusinessException ex
    ) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(construirRespuesta(HttpStatus.BAD_REQUEST, ex.getMessage()));
    }

    /**
     * Maneja los errores de validación generados por @Valid.
     *
     * Este método se ejecuta cuando los datos enviados en una solicitud
     * no cumplen las reglas definidas en los DTO, por ejemplo:
     * campos vacíos, tamaños máximos, formatos de email inválidos, etc.
     *
     * Retorna una respuesta cestado HTTP 400 BAD REQUEST e incluye
     * el detalle de los campos que fallaron.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> manejarErroresValidacion(
            MethodArgumentNotValidException ex
    ) {
        Map<String, String> errores = new LinkedHashMap<>();// Mapa donde se almacenan los errores de validación campo por campo

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {// Recorro todos los errores detectados en los campos del DTO
            // Guardo el nombre del campo y el mensaje de error correspondiente
            errores.put(error.getField(), error.getDefaultMessage());
        }
        // Construyo una respuesta base con el estado y mensaje general
        Map<String, Object> respuesta = construirRespuesta(
                HttpStatus.BAD_REQUEST,
                "Error de validacion en la solicitud."
        );

        // Agrego a la respuesta el detalle específico de los errores encontrados
        respuesta.put("errores", errores);

        return ResponseEntity
                .badRequest()
                .body(respuesta);
    }

    /**
     * Construye una respuesta estándar para los errores del sistema.
     *
     * Este método se reutiliza para manener la misma estructura en todas
     * las respuestas de error.
     *
     * La respuesta incluye:
     * - status: código numérico HTTP
     * - error: descripción del estado HTTP
     * - message: mnsaje específico del error
     * - timestamp: fecha y hora en que ocurrió el error
     */
    private Map<String, Object> construirRespuesta(
            HttpStatus status,
            String mensaje
    ) {
        Map<String, Object> respuesta = new LinkedHashMap<>();

        respuesta.put("status", status.value());
        respuesta.put("error", status.getReasonPhrase());
        respuesta.put("message", mensaje);
        respuesta.put("timestamp", LocalDateTime.now());

        return respuesta;
    }
}