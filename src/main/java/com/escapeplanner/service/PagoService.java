package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Pago; // Importo la entidad Pago, que representa los pagos en la base de datos
import com.escapeplanner.dto.PagoRequest;
import java.util.List;// Importo List para manejar colecciones de pagos

/**
 * Esta interfaz define las operaciones del módulo de pagos los cuales esán asociados a eventos dentro del sistema.
 *
 * Aquí se establece el contrato de lo que se puede hacer
 * mientras que la implementación real estará en service.impl
 *
 * @author Alex Mártin
 */
public interface PagoService {

    /**
     * Permite registrar un nuevo pago en el sistema
     * Recibe un DTO con la información necesaria del pago
     */
    Pago registrar(PagoRequest request);

    /**
     * Permite obtener todos los pagos asociados a un evento específico.
     * Se utiliza el ID del evento para filtrar los resultados.
     */
    List<Pago> listarPorEvento(Long eventoId);
}