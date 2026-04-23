package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Documento; //Importo la entidad Documento, que representa la tabla en la base de datos
import com.escapeplanner.dto.DocumentoRequest;// Importo el DTO que se usa para recibir los datos desde el controlador

import java.util.List; // Importo List porque voy a trabajar con listas de documentos

/**
 * Esta interfaz define las operaciones relacionadas con los documentos
 * dentro del sistema. Estos documentos funcionan como soportes
 * asociados a un evento.
 *
 * Aquí se especifica qué se puede hacer, mientras que la lógica
 * se implementa en la clase correspondiente en service.impl.
 *
 * @author Alex Mártin
 */
public interface DocumentoService {

    /**
     * Permite registrar un nuevo documento en el sistem
     * Recibe un DTO con la información necesaria para crear el documento
     */
    Documento registrar(DocumentoRequest request);

    /**
     * Permite obtener todos los documentos asociados a un evento específico
     * Se utiliza el ID del evento para hacer el filtro
     */
    List<Documento> listarPorEvento(Long eventoId);
}
