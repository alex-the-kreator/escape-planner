package com.escapeplanner.domain.entity;

import com.escapeplanner.domain.enums.EstadoCliente;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * Entidad que almacena la informacion base del cliente.
 * Un cliente puede estar asociado a varios eventos dentro del sistema.
 *
 * @author Alex Martin
 */
@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // La cedula se maneja como dato funcional del cliente,
    // mientras que el id sigue siendo la clave tecnica interna.
    @NotBlank
    @Size(max = 20)
    @Column(nullable = false, unique = true, length = 20)
    private String cedula;

    @NotBlank
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String nombre;

    @NotBlank
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String telefono;

    @Email
    @Size(max = 120)
    @Column(length = 120)
    private String email;

    @Size(max = 50)
    @Column(name = "canal_contacto", length = 50)
    private String canalContacto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCliente estado;

    @JsonIgnore
    @OneToMany(mappedBy = "cliente", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Evento> eventos = new ArrayList<>();

    public Cliente() {
    }

    public Long getId() {
        return id;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCanalContacto() {
        return canalContacto;
    }

    public void setCanalContacto(String canalContacto) {
        this.canalContacto = canalContacto;
    }

    public EstadoCliente getEstado() {
        return estado;
    }

    public void setEstado(EstadoCliente estado) {
        this.estado = estado;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }
}
