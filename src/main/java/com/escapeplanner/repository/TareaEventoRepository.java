package com.escapeplanner.repository;

import com.escapeplanner.domain.entity.TareaEvento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 *
 * Repositorio encargado de las operaciones de acceso
 * a datos para la entidad TareaEvento.
 *
 * JpaRepository proporciona automáticamente métodos como:
 * - save()
 * - findById()
 * - findAll()
 * - delete()
 *
 * Además, aquí se define una consulta personalizada
 * utilizando el nombre del método.
 *
 * @author Alex Mártin
 */
public interface TareaEventoRepository extends JpaRepository<TareaEvento, Long> {

    /**
     *
     * Método para listar todas las tareas asociadas
     * a un evento específico.
     *
     * Los resultados se ordenan:
     * 1. Por fecha límite ascendente
     * 2. Por ID ascendente
     *
     * Spring Data JPA genera automáticamente
     * la consulta a partir del nombre del método.
     *
     * @param eventoId ID del evento
     * @return lista de tareas asociadas al evento
     */
    List<TareaEvento> findByEventoIdOrderByFechaLimiteAscIdAsc(Long eventoId);
}