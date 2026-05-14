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
 * Implementación del servicio de clientes.
 *
 * En esta clase se maneja la lógica principal del módulo de clientes,
 * como registrar, actualizar, listar, buscar y eliminar clientes.
 *
 * También se mantiene la separación de responsabilidades, ya que
 * el controlador no trabaja directamente con el repositorio.
 *
 * @author Alex Mártin
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService {


    private final ClienteRepository clienteRepository;// Repositorio encargado de las operaciones de lientes en la base de datos

    public ClienteServiceImpl(ClienteRepository clienteRepository) {// Constructor donde Spring inyecta automáticamente el repositorio
        this.clienteRepository = clienteRepository;
    }

    // Método encargado de registrar un nuevo cliente
    @Override
    public Cliente registrar(ClienteRequest request) {

        Cliente cliente = new Cliente();
        mapearDatos(request, cliente);// Se copian los datos del request hacia la entidad

        return clienteRepository.save(cliente);// Se guarda el cliente en la base de datos
    }

    // Método encargado de actualizar un cliente existente
    @Override
    public Cliente actualizar(Long clienteId, ClienteRequest request) {

        Cliente clienteExistente = obtenerClienteExistente(clienteId);// Se busca el clente por ID y se valida que exista
        mapearDatos(request, clienteExistente);// Se actualizan los datos del cliente con la información recibida

        return clienteRepository.save(clienteExistente);// Se guarda el cliente actalizado
    }

    // Método para listar todos los clientes registrados
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listar() {

        // Retorna todos los clientes guardados en la base de datos
        return clienteRepository.findAll();
    }

    // Método para buscar clientes por un término escrito por el usuario
    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscar(String termino) {

        if (termino == null || termino.isBlank()) {// Si no se escribenada, se retorna el listado completo
            return listar();
        }

        String terminoNormalizado = termino.trim();// Se limpia el texto para evitar espacios innecesarios

        // Busca coinciencias por cédula, nombre o email
        return clienteRepository.findByCedulaContainingIgnoreCaseOrNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(
                terminoNormalizado,
                terminoNormalizado,
                terminoNormalizado
        );
    }

    // Método para obtener un cliente por su ID
    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorId(Long id) {

        // Se usa Optional porque puede existir o no un cliente con ese ID
        return clienteRepository.findById(id);
    }

    // Método para eliminar un cliente
    @Override
    public void eliminar(Long id) {

        Cliente cliente = obtenerClienteExistente(id);// Primero se alida que el cliente exista
        clienteRepository.delete(cliente);// Si existe, se elimina de la base de datos
    }

    // Método privado para obtener un cliente existente
    private Cliente obtenerClienteExistente(Long clienteId) {

        // Si no encuentra el cliente, lanza una excepción personalizada
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el cliente con id: " + clienteId
                ));
    }

    // Método privado para copiar los datos del request hacia la entidad Cliente
    private void mapearDatos(ClienteRequest request, Cliente cliente) {

        cliente.setCedula(request.cedula());// Número de cédula dl cliente
        cliente.setNombre(request.nombre());// Nombre completo del cliente
        cliente.setTelefono(request.telefono());// Número de teléfono
        cliente.setEmail(request.email());// Correo electrónico
        cliente.setCanalContacto(request.canalContacto());// Canal por el cual se contactó al cliente
        cliente.setEstado(request.estado());// Estado actual de cliente
    }
}