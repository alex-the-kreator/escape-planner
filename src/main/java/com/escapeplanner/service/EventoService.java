package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Evento;// Importo la entidad Evento, que representa los datos del evento en la base de datos
import com.escapeplanner.dto.EventoRequest;// Importo el DTO que se usa para recibir los datos desde el controlador
import java.time.LocalDate;// Importo LocalDate para manejar fechas (día, mes, año)
import java.time.LocalTime;// Importo LocalTime para manejar horas (hora de inicio y fin)
import java.util.List;// Importo List para trabajar con listas de eventos
import java.util.Optional;// Importo Optional porque puede que un evento no exista al buscarlo por ID

/**
 * Esta interfaz define las operaciones principales del módulo de eventos.
 * Aquí se establece qué se puede hacer, como registrar, actualizar y consultar eventos.
 *
 * También se contemplan reglas importantes del sistema como:
 * validación de datos y verificación de conflictos de horario.
 *
 * La implementación de esta lógica se realiza en la clase service.impl.
 *
 * @author Alex Mártin
 */
public interface EventoService {

    /**
     * Registra un nuevo evento en el sistema
     * Recibe los datos desde un DTO
     */
    Evento registrar(EventoRequest request);

    /**
     * Actualiza la información de un evento existente
     * Se identifica por su ID
     */
    Evento actualizar(Long eventoId, EventoRequest request);

    /**
     * Devuelve la lista de todos los eventos registrados
     */
    List<Evento> listar();

    /**
     * Busca un evento por su ID
     * Se usa Optional porque puede que no exista
     */
    Optional<Evento> obtenerPorId(Long id);

    /**
     * Verifica si existe un conflicto de horario para un evento
     * Se valida usando la fecha y el rango de horas
     * El parámetro eventoIdExcluir se usa para ignorar un evento específico
     * (por ejemplo, cuando se está actualizando y no se quiere comparar consigo mismo)
     */
    boolean existeConflictoHorario(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Long eventoIdExcluir);
}
