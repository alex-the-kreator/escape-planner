package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Documento;
import com.escapeplanner.dto.DocumentoRequest;

import java.util.List;

/**
 * Permite definir como se registran y consultan soportes documentales.
 *
 * @author Alex Mártin
 */
public interface DocumentoService {

    Documento registrar(DocumentoRequest request);

    List<Documento> listarPorEvento(Long eventoId);
}
