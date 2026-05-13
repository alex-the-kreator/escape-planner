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

    // La búsqueda del cliente ahora tambien contempla la cedula, porque es uno de los datos mas usados en operacion.
    List<Cliente> findByCedulaContainingIgnoreCaseOrNombreContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String cedula,
            String nombre,
            String email
    );
}
