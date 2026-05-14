package com.escapeplanner.repository;

import com.escapeplanner.domain.entity.Documento;// Importa la entidad Documento
import org.springframework.data.jpa.repository.JpaRepository;// JpaRepository proporciona automáticamente operaciones CRUD

/**
 *
 * Repositorio encargado del acceso a datos
 * para la entidad Documento.
 *
 * Al extender JpaRepository, Spring Data JPA
 * genera automáticamente métodos como:
 *
 * - save()
 * - findById()
 * - findAll()
 * - delete()
 * - existsById()
 *
 * En este repositorio todavía no se agregan
 * consultas personalizadas, pero pueden
 * añadirse más adelante según las necesidades
 * del proyecto.
 *
 */
public interface DocumentoRepository extends JpaRepository<Documento, Long> {

    /*
        Documento:entidad que administrará el repositorio
        Long: tipo de dato de la llave primaria (ID)
     */

}