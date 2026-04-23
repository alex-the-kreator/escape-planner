package com.escapeplanner.domain.entity;

import com.escapeplanner.domain.enums.EstadoEvento;
import com.escapeplanner.domain.enums.TipoEvento;
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad central del sistema.
 * Desde aquí se relacionan los datos principales del evento y su seguimiento.
 *
 * @author Alex Mártin
 */
@Entity
@Table(name = "eventos")
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Todo evento debe pertenecer a un cliente existente según las reglas de integridad referencial del proyecto.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    // El usuario representa al asesor o responsable que gestiona el evento dentro del sistema
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Se usa enum porque en la documentación ya deja cerrados los tipos de evento esperados por el MVP
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private TipoEvento tipo;

    @NotNull
    @Column(nullable = false)
    private LocalDate fecha;

    @NotNull
    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @NotNull
    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    // La cantidad de personas no debe ser cero ni negativa.
    @NotNull
    @Positive
    @Column(name = "num_personas", nullable = false)
    private Integer numPersonas;

    // El estado permite modelar el ciclo de vida operativo del evento
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EstadoEvento estado;

    // Este campo ayuda a marcar si el evento requiere gestion manual e bloqueo en el sistema externo de reservas
    @NotNull
    @Column(name = "requiere_bloqueo", nullable = false)
    private Boolean requiereBloqueo;

    @Size(max = 4000)
    @Column(name = "detalles_logisticos", columnDefinition = "TEXT")
    private String detallesLogisticos;

    @Column(name = "fecha_creacion", nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;

    // Desde el detalle del evento se administran tareas, pagos, documentos y bloqueos. Por eso estas relaciones cuelgan de aquí
    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<TareaEvento> tareas = new ArrayList<>();

    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Pago> pagos = new ArrayList<>();

    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Documento> documentos = new ArrayList<>();

    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Bloqueo> bloqueos = new ArrayList<>();

    public Evento() {
    }

    // Si el registro se guarda por JPA sin traer estas fechas desde fuera aquí se garantiza un valor inicial coherente para auditoria básica
    @PrePersist
    public void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        if (fechaCreacion == null) {
            fechaCreacion = ahora;
        }
        if (fechaActualizacion == null) {
            fechaActualizacion = ahora;
        }
    }

    @PreUpdate
    public void preUpdate() {
        // TODO: si en una siguiente fase se requiere auditoria mas completa,
        // aquí se podría registrar también el usuario que hizo la modificación.
        fechaActualizacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public TipoEvento getTipo() {
        return tipo;
    }

    public void setTipo(TipoEvento tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(LocalTime horaInicio) {
        this.horaInicio = horaInicio;
    }

    public LocalTime getHoraFin() {
        return horaFin;
    }

    public void setHoraFin(LocalTime horaFin) {
        this.horaFin = horaFin;
    }

    public Integer getNumPersonas() {
        return numPersonas;
    }

    public void setNumPersonas(Integer numPersonas) {
        this.numPersonas = numPersonas;
    }

    public EstadoEvento getEstado() {
        return estado;
    }

    public void setEstado(EstadoEvento estado) {
        this.estado = estado;
    }

    public Boolean getRequiereBloqueo() {
        return requiereBloqueo;
    }

    public void setRequiereBloqueo(Boolean requiereBloqueo) {
        this.requiereBloqueo = requiereBloqueo;
    }

    public String getDetallesLogisticos() {
        return detallesLogisticos;
    }

    public void setDetallesLogisticos(String detallesLogisticos) {
        this.detallesLogisticos = detallesLogisticos;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaActualizacion() {
        return fechaActualizacion;
    }

    public void setFechaActualizacion(LocalDateTime fechaActualizacion) {
        this.fechaActualizacion = fechaActualizacion;
    }

    public List<TareaEvento> getTareas() {
        return tareas;
    }

    public void setTareas(List<TareaEvento> tareas) {
        this.tareas = tareas;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public List<Documento> getDocumentos() {
        return documentos;
    }

    public void setDocumentos(List<Documento> documentos) {
        this.documentos = documentos;
    }

    public List<Bloqueo> getBloqueos() {
        return bloqueos;
    }

    public void setBloqueos(List<Bloqueo> bloqueos) {
        this.bloqueos = bloqueos;
    }
}
