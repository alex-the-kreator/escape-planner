package com.escapeplanner.domain.entity;

import com.escapeplanner.domain.enums.EstadoTareaEvento;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Representa las actividades de seguimiento necesarias para preparar un evento.
 *
 * @author Alex Mártin
 */
@Entity
@Table(name = "tareas_evento")
public class TareaEvento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    // El nombre de la tarea describe una acción concreta del seguimiento,
    // por ejemplo confirmar contrato o pedir la torta al proveedor.
    @NotBlank
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String nombre;

    //En la documentación definimos tres estados cerrados para la tarea.
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoTareaEvento estado;

    @Column(name = "fecha_limite")
    private LocalDate fechaLimite;

    public TareaEvento() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Evento getEvento() {
        return evento;
    }

    public void setEvento(Evento evento) {
        this.evento = evento;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public EstadoTareaEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoTareaEvento estado) {
        this.estado = estado;
    }

    public LocalDate getFechaLimite() {
        return fechaLimite;
    }

    public void setFechaLimite(LocalDate fechaLimite) {
        this.fechaLimite = fechaLimite;
    }
}
