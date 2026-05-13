package com.escapeplanner.service.impl;

import com.escapeplanner.domain.entity.Cliente;
import com.escapeplanner.dto.ClienteRequest;
import com.escapeplanner.exception.ResourceNotFoundException;
import com.escapeplanner.repository.ClienteRepository;
import com.escapeplanner.service.ClienteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementacion del servicio de clientes.
 *
 * Aqui se concentra la logica del modulo, manteniendo la separacion de responsabilidades.
 *
 * @author Alex Martin
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente registrar(ClienteRequest request) {
        Cliente cliente = new Cliente();
        mapearDatos(request, cliente);
        return clienteRepository.save(cliente);
    }

    @Override
    public Cliente actualizar(Long clienteId, ClienteRequest request) {
        Cliente clienteExistente = obtenerClienteExistente(clienteId);
        mapearDatos(request, clienteExistente);
        return clienteRepository.save(clienteExistente);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listar() {
        return clienteRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscar(String termino) {
        if (termino == null || termino.isBlank()) {
            return listar();
        }

        String terminoNormalizado = termino.trim();
        return clienteRepository.findByCedulaContainingIgnoreCaseOrNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(
                terminoNormalizado,
                terminoNormalizado,
                terminoNormalizado
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorId(Long id) {
        return clienteRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = obtenerClienteExistente(id);
        clienteRepository.delete(cliente);
    }

    private Cliente obtenerClienteExistente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontro el cliente con id: " + clienteId
                ));
    }

    private void mapearDatos(ClienteRequest request, Cliente cliente) {
        cliente.setCedula(request.cedula());
        cliente.setNombre(request.nombre());
        cliente.setTelefono(request.telefono());
        cliente.setEmail(request.email());
        cliente.setCanalContacto(request.canalContacto());
        cliente.setEstado(request.estado());
    }
}
