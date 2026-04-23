package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Bloqueo;
import com.escapeplanner.dto.BloqueoRequest;

import java.util.List;

/**
 * Contrato para el registro y consulta de bloqueos asociados al evento.
 *
 * @author Alex Mártin
 */
public interface BloqueoService {

    Bloqueo registrar(BloqueoRequest request);

    List<Bloqueo> listarPorEvento(Long eventoId);
}
