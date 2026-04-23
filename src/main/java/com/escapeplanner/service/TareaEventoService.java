package com.escapeplanner.service;

import com.escapeplanner.domain.entity.TareaEvento;
import com.escapeplanner.dto.TareaEventoRequest;

import java.util.List;

/**
 * Organiza las operaciones del seguimiento operativo de cada evento.
 *
 * @author Alex Mártin
 */
public interface TareaEventoService {

    TareaEvento registrar(TareaEventoRequest request);

    TareaEvento actualizarEstado(Long tareaId, TareaEventoRequest request);

    List<TareaEvento> listarPorEvento(Long eventoId);
}
