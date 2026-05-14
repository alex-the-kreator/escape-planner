package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Evento;
import com.escapeplanner.dto.EventoRequest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Esta interfaz define las operaciones principales del modulo de eventos.
 *
 * @author Alex Mártin
 */
public interface EventoService {

    Evento registrar(EventoRequest request);

    Evento actualizar(Long eventoId, EventoRequest request);

    List<Evento> listar();

    Optional<Evento> obtenerPorId(Long id);

    boolean existeConflictoHorario(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin, Long eventoIdExcluir);

    void eliminar(Long id);
}
