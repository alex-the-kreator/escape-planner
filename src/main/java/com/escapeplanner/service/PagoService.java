package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Pago;
import com.escapeplanner.dto.PagoRequest;

import java.util.List;

/**
 * Centraliza el contrato del modulo de pagos asociados a eventos.
 *
 * @author Alex Mártin
 */
public interface PagoService {

    Pago registrar(PagoRequest request);

    List<Pago> listarPorEvento(Long eventoId);
}
