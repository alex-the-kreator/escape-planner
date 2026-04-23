package com.escapeplanner.repository;

import com.escapeplanner.domain.entity.Evento;
import com.escapeplanner.domain.enums.EstadoEvento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Repositorio principal del módulo de eventos.
 *
 * @author Alex Mártin
 */
public interface EventoRepository extends JpaRepository<Evento, Long> {

    // Este orden facilita mostrar primero la agenda operativa de eventos próximos en la interfaz administrativa
    List<Evento> findAllByOrderByFechaAscHoraInicioAsc();

    // Detecta cruces de horario el mismo dia. La condición horaInicio < horaFinNueva y horaFin > horaInicioNueva
    // es la forma clásica de verificar solapamiento entre rangos
    // TODO: mas adelante se podría complementar con reglas adicionales
    // según tipo de evento o capacidad operativa real de la sede
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
