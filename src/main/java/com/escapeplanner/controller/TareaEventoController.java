package com.escapeplanner.controller;

import com.escapeplanner.domain.entity.TareaEvento;
import com.escapeplanner.dto.TareaEventoRequest;
import com.escapeplanner.service.TareaEventoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Optional;

/**
 * Controlador REST temporal del módulo de tareas del evento.
 * Se deja enfocado en pruebas backend antes de construir vistas web.
 *
 * @author Alex Mártin
 */
@RestController
@RequestMapping
public class TareaEventoController {

    private final TareaEventoService tareaEventoService;

    public TareaEventoController(TareaEventoService tareaEventoService) {
        this.tareaEventoService = tareaEventoService;
    }

    @GetMapping("/api/eventos/{eventoId}/tareas")
    public ResponseEntity<List<TareaEvento>> listarPorEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(tareaEventoService.listarPorEvento(eventoId));
    }

    @PostMapping("/api/eventos/{eventoId}/tareas")
    public ResponseEntity<TareaEvento> crear(
            @PathVariable Long eventoId,
            @Valid @RequestBody TareaEventoRequest request
    ) {
        TareaEventoRequest requestAjustado = new TareaEventoRequest(
                eventoId,
                request.nombre(),
                request.estado(),
                request.fechaLimite()
        );

        TareaEvento tareaCreada = tareaEventoService.registrar(requestAjustado);

        return ResponseEntity
                .created(URI.create("/api/tareas/" + tareaCreada.getId()))
                .body(tareaCreada);
    }

    @GetMapping("/api/tareas/{id}")
    public ResponseEntity<TareaEvento> obtenerPorId(@PathVariable Long id) {
        Optional<TareaEvento> tareaEvento = tareaEventoService.obtenerPorId(id);
        return tareaEvento.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/api/tareas/{id}")
    public ResponseEntity<TareaEvento> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody TareaEventoRequest request
    ) {
        TareaEvento tareaActualizada = tareaEventoService.actualizarEstado(id, request);
        return ResponseEntity.ok(tareaActualizada);
    }

    @DeleteMapping("/api/tareas/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tareaEventoService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
