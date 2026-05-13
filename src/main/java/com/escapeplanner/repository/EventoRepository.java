package com.escapeplanner.repository;

import com.escapeplanner.domain.entity.Evento;
import com.escapeplanner.domain.enums.EstadoEvento;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Repositorio principal del modulo de eventos.
 *
 * @author Alex M\u00E1rtin
 */
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // Para la capa web y la API conviene traer cliente y usuario junto al evento,
    // evitando fallos de carga lazy cuando la vista o el JSON necesitan esos datos.
    @EntityGraph(attributePaths = {"cliente", "usuario"})
    List<Evento> findAllByOrderByFechaAscHoraInicioAsc();

    @Override
    @EntityGraph(attributePaths = {"cliente", "usuario"})
    Optional<Evento> findById(Long id);

    // Detecta cruces de horario el mismo dia. La condicion horaInicio < horaFinNueva y horaFin > horaInicioNueva
    // es la forma clasica de verificar solapamiento entre rangos.
    @Query("""
            select count(e) > 0
            from Evento e
            where e.fecha = :fecha
              and e.estado <> :estadoCancelado
              and (:eventoIdExcluir is null or e.id <> :eventoIdExcluir)
              and e.horaInicio < :horaFin
              and e.horaFin > :horaInicio
            """)
    boolean existsConflictoHorario(
            @Param("fecha") LocalDate fecha,
            @Param("horaInicio") LocalTime horaInicio,
            @Param("horaFin") LocalTime horaFin,
            @Param("eventoIdExcluir") Long eventoIdExcluir,
            @Param("estadoCancelado") EstadoEvento estadoCancelado
    );
}
