package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Bloqueo;
import com.escapeplanner.dto.BloqueoRequest;

import java.util.List;

/**
 * Este módulo define las operaciones relacionadas con los bloqueos dentro del sistema, específicamente los que están asociados a un evento
 * Aquí solo se define qué se puede hacer (no cómo se hace), ya que es una interfaz. La implementación real estará en otra clase
 *
 * @author Alex Mártin
 */
public interface BloqueoService {

    /**
     * Método para registrar un nuevo bloqueo
     * Recibe un objeto con los datos necesarios y devuelve el bloqueo ya creado
     */
    Bloqueo registrar(BloqueoRequest request);

    /**
     * Método para obtener todos los bloqueos asociados a un evento específico
     * Se usa el ID del evento para filtrar los resultados
     */
    List<Bloqueo> listarPorEvento(Long eventoId);
}