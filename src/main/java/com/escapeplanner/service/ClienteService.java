package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Cliente;
import com.escapeplanner.dto.ClienteRequest;

import java.util.List;
import java.util.Optional;

/**
 * Esta interfaz define las operaciones principales del módulo de clientes.
 * Aquí sol se indica qué acciones se pueden realizar, pero no cómo se hacen.
 * La implementación de estos métodos estará en la clase service.impl
 *
 * @author Alex Mártin
 */
public interface ClienteService {

    /**
     * Registra un nuevo cliente en el sistema
     * Recibe los datos dede un DTO (ClienteRequest)
     */
    Cliente registrar(ClienteRequest request);

    /**
     * Actualiza la información de un cliente existent
     * Se usa el ID para identificarlo y el request con los nuevos datos
     */
    Cliente actualizar(Long clienteId, ClienteRequest request);

    /**
     * Retorna la lista de todos los clientes registrados
     */
    List<Cliente> listar();

    /**
     * Permite buscar clientes por un término (nombre o email)
     */
    List<Cliente> buscar(String termino);

    /**
     * Obtiene un cliente por su ID
     * Se usa Optional porque puede que no exista
     */
    Optional<Cliente> obtenerPorId(Long id);

    /**
     * Elimina un cliente del sistema usando su ID
     */
    void eliminar(Long id);
}
