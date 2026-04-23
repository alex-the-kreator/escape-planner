package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Evento;
import com.escapeplanner.dto.EventoRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Contrato principal del modulo de eventos.
 * Este servicio sera el encargado de aplicar reglas como:
 * validacion de datos, conflicto de horario y actualizacion del evento.
 *
 * @author Alex Mártin
 */
public interface EventoService {

    Evento registrar(EventoRequest request);

    Evento actualizar(Long eventoId, EventoRequest request);

    List<Evento> listar();

    Optional<Evento> obtenerPorId(Long id);

    boolean existeConflictoHorario(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Long eventoIdExcluir);
}
