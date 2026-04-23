package com.escapeplanner.repository;

import com.escapeplanner.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para consultas y persistencia de clientes.
 *
 * @author Alex Mártin
 */
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Esta búsqueda simple es util para una futura pantalla de selección de cliente.
    List<Cliente> findByNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(String nombre, String email);
}
