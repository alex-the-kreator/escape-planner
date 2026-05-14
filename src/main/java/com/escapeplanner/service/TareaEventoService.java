package com.escapeplanner.service;

import com.escapeplanner.domain.entity.TareaEvento;
import com.escapeplanner.dto.TareaEventoRequest;

import java.util.List;
import java.util.Optional;

/**
 *
 * Interfaz de servicio para manejar las tareas asociadas a un evento.
 *
 * Esta interfaz define las operaciones principales que se pueden realizar
 * sobre las tareas de seguimiento dentro del sistema Escape Planner.
 *
 * Al ser una interfaz, aquí solo se declaran los métodos.
 * La lógica real se implementa después en la clase de servicio TareaEventoServiceImpl
 *
 * @author Alex Mártin
 */
public interface TareaEventoService {

    /**
     *
     * Registra una nueva tarea relacionada con un evento.
     *
     * Recibe un objeto TareaEventoRequest con la información necesaria
     * para crear la tarea, como el evento al que pertenece,
     * la descripción, el responsable y el estado
     *
     * @param request datos enviados para crear la tarea
     * @return la tarea creada y guardada en el sistema
     */
    TareaEvento registrar(TareaEventoRequest request);

    //Actualiza el estado o la información de una tarea existente.
    //Se recibe el ID de la tarea para identificar cuál se va a modificar y un request con los nuevos datos enviados desde la petición.

    // @param tareaId identificador de la tarea que se desea actualizar
    // @param request datos nuevos de la tarea
    // @return la tarea actualizada

    TareaEvento actualizarEstado(Long tareaId, TareaEventoRequest request);

    /**
     *
     * Lista todas las tareas asociadas a un evento específico.
     *
     * Este método sirve para consultar el seguimiento de un evento,
     * mostrando las tareas que pertenecen a dicho evento.
     *
     * @param eventoId identificador del evento
     * @return lista de tareas relacionadas con el evento
     */
    List<TareaEvento> listarPorEvento(Long eventoId);

    /**
     *
     * Busca una tarea por su ID.
     *
     * Se usa Optional porque puede existir o no una tarea
     * con el identificador enviado.
     *
     * @param id identificador de la tarea
     * @return Optional con la tarea si existe, o vacío si no se encuentra
     */
    Optional<TareaEvento> obtenerPorId(Long id);

    /**
     *
     * Elimina una tarea del sistema usando su ID.
     *
     * Este método no retorna ningún valor porque su objetivo
     * es realizar la eliminación de la tarea indicada.
     *
     * @param id identificador de la tarea que se desea eliminar
     */
    void eliminar(Long id);
}