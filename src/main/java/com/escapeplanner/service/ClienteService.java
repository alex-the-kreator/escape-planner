package com.escapeplanner.service;

import com.escapeplanner.domain.entity.Cliente;
import com.escapeplanner.dto.ClienteRequest;

import java.util.List;
import java.util.Optional;

/**
 * Define las operaciones base del modulo de clientes.
 * Aqui solo se describe el contrato; la logica quedara en service.impl.
 *
 * @author Alex Mártin
 */
public interface ClienteService {

    Cliente registrar(ClienteRequest request);

    List<Cliente> listar();

    List<Cliente> buscar(String termino);

    Optional<Cliente> obtenerPorId(Long id);
}
