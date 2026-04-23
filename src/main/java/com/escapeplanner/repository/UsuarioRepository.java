package com.escapeplanner.repository;

import com.escapeplanner.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio de acceso a usuarios.
 * El método por email queda preparado pensando en la futura autenticación
 *
 * @author Alex Mártin
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);
}
