package com.escapeplanner.service;

import com.escapeplanner.domain.entity.TareaEvento;// Importo la entidad TareaEvento, que representa las tareas en la base de datos
import com.escapeplanner.dto.TareaEventoRequest; // Importo el DTO que se usa para recibir los datos desde el controlador
import java.util.List;// Importo List para poder manejar listas de tareas

/**
 * Esta interfaz define las operaciones relacionadas con las tareas que hacen parte del seguimiento de un evento
 * Aquí se establece qué se puede hacer con las tareas, pero la lógica se implementa en otra clase (service.impl)
 *
 * @author Alex Mártin
 */
public interface TareaEventoService {

    // Método para registrar una nueva tarea asociada a un evento
    // Recibe los datos desde un DTO y devuelve la tarea creada
    TareaEvento registrar(TareaEventoRequest request);

    // Método para actualizar el estado de una tarea (por ejemplo: pendiente, en proceso, completada)
    // Se identifica la tarea por su ID y se actualiza con la información del request
    TareaEvento actualizarEstado(Long tareaId, TareaEventoRequest request);

    // Método para obtener todas las tareas asociadas a un evento específico
    // Se usa el ID del evento para filtrar las tareas
    List<TareaEvento> listarPorEvento(Long eventoId);
}
