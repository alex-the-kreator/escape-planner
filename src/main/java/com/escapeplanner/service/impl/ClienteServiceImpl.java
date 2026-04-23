package com.escapeplanner.service.impl;

import com.escapeplanner.domain.entity.Cliente; // Entidad que representa al cliente en la BD
import com.escapeplanner.dto.ClienteRequest; // DTO para recibir los datos desde el controller
import com.escapeplanner.exception.ResourceNotFoundException; // Excepción personalizada cuando no se encuentra un cliente
import com.escapeplanner.repository.ClienteRepository; // Repositorio para acceso a datos
import com.escapeplanner.service.ClienteService; // Interfaz del servicio
import org.springframework.stereotype.Service; // Marca la clase como servicio de Spring
import org.springframework.transaction.annotation.Transactional; // Manejo de transacciones

import java.util.List; // Para manejar listas de clientes
import java.util.Optional; // Para manejar posibles valores nulos de forma segura

/**
 * Implementación del servicio de clientes.
 *
 * Aquí se concentra la lógica del módulo, manteniendo la separación de responsabilidades:
 * - El controller solo recibe/retorna datos
 * - El repository accede a la BD
 * - Este servicio aplica la lógica de negocio
 *
 * @author Alex Mártin
 */
@Service // Indica que esta clase es un componente de tipo servicio
@Transactional // Todas las operaciones se ejecutan dentro de una transacción
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;// Repositorio para interactuar con la base de datos

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente registrar(ClienteRequest request) {
        Cliente cliente = new Cliente(); // Se crea una nueva instancia de Cliente

        mapearDatos(request, cliente); // Se copian los datos del DTO al objeto entidad

        return clienteRepository.save(cliente);// Se guarda en la base de datos
    }

    @Override
    public Cliente actualizar(Long clienteId, ClienteRequest request) {
        Cliente clienteExistente = obtenerClienteExistente(clienteId); // Primero se valida que el cliente exista

        mapearDatos(request, clienteExistente);  // Se actualizan sus datos con la información recibida

        return clienteRepository.save(clienteExistente); // Se guardan los cambios
    }

    @Override
    @Transactional(readOnly = true) // Optimización: solo lectura
    public List<Cliente> listar() {
        return clienteRepository.findAll(); // Retorna todos los clientes
    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> buscar(String termino) {// Si no hay término de búsqueda, se listan todos
        if (termino == null || termino.isBlank()) {
            return listar();
        }
        // trim. Es un método de Java que elimina los espacios al inicio y al final del texto y evita errores en la búsqieda
        String terminoNormalizado = termino.trim(); // Se limpia el texto ingresado

        // Búsqueda por nombre o email sin distinguir mayúsculas/minúsculas
        return clienteRepository.findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(
                terminoNormalizado,
                terminoNormalizado
        );
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> obtenerPorId(Long id) {
        // Se mantiene Optional porque el contrato del servicio lo define así
        // (puede que el cliente no exista)
        return clienteRepository.findById(id);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = obtenerClienteExistente(id); // Se valida que el cliente exista antes de eliminarlo

        clienteRepository.delete(cliente);// Se elimina el cliente
    }

    /**
     * Método interno para obtener un cliente obligatoriamente.
     * Si no existe, lanza una excepción controlada.
     */
    private Cliente obtenerClienteExistente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el cliente con id, sorry :( : " + clienteId
                ));
    }

    /**
     * Método auxiliar para mapear los datos del DTO a la entidad.
     * Se reutiliza en registrar y actualizar para evitar duplicar código.
     */
    private void mapearDatos(ClienteRequest request, Cliente cliente) {
        cliente.setNombre(request.nombre());
        cliente.setTelefono(request.telefono());
        cliente.setEmail(request.email());
        cliente.setCanalContacto(request.canalContacto());
        cliente.setEstado(request.estado());
    }
}