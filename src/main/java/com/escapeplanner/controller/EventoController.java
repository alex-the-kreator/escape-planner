package com.escapeplanner.controller;

import com.escapeplanner.domain.entity.Evento;// Entidad Evento, que representa un evento registrado dentro del sistema
import com.escapeplanner.dto.EventoRequest;// DTO utilizado ara recibir los datos necesarios al crear o actualizar un evento
import com.escapeplanner.service.EventoService;// Servicio que contiene la lógica de negocio relacionada con los eventos
import jakarta.validation.Valid;// Permite validar automáticamente el contenido del DTO recibido en la petición
import org.springframework.format.annotation.DateTimeFormat;// Permite indicar el formato eperado para fechas y horas recibidas por parámetros
import org.springframework.http.ResponseEntity;// Permite construir respuestas HTTP con estado y cuerpo personalizados

// Anotaciones utilizadas para definir los endpoints del controlador REST
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;// Permite recibir datos enviados en el cuerpo de la petición HTTP
import org.springframework.web.bind.annotation.RequestMapping;// Define la ruta base del controlador
import org.springframework.web.bind.annotation.RequestParam;// Permite recibir parámetrs enviados por la URL
import org.springframework.web.bind.annotation.RestController;// Indica que esta clase funciona como controlador REST
import java.net.URI;// Permite construir la URI del recurso creado
import java.time.LocalDate;// Permite manejar fechas sin hora
import java.time.LocalTime;// Permite manejar horas sin fecha
import java.util.List;// Permite trabajar con listas de eventos
import java.util.LinkedHashMap;// Mapa que conserva el orden en que se agregan los datos a la respuesta
import java.util.Map;// Estructura usad para devolver información en formato clave-valor
import java.util.Optional;// Permite representar que un evento puede existi o no al buscarlo por ID

/**
 * Controlador REST temporal para probar el módulo de eventos
 * antes de construir la interfaz final con Thymeleaf.
 *
 * Esta clase expone endpoints HTTP que permiten registrar, consultar,
 * actualizar y validar conflictos de horario de eventos.
 *
 * Su propósito principal en esta fase es facilitar las pruebas del backend
 * sin depender todavía de una interfaz gráfica definitiva.
 *
 * @author Alex Mártin
 */
@RestController
// Indica que esta clase recibe peticiones REST y devuelve respuestas en formato JSON
@RequestMapping("/api/eventos")
// Define la ruta base para todos los endpoints del controlador
public class EventoController {

    private final EventoService eventoService;// Servicio encargado de aplicar la lógica de negocio del módulo de eventos
    /**
     * Constructor con inyección de dependencias.
     *
     * Spring proporciona automáticamente una instancia de EventoService.
     * Esto permite que el controlador n dependa directamente del repositorio,
     * sino de la capa de servicio, respetando la arquitectura por capas.
     */
    public EventoController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    /**
     * Endpoint para registrar un nuevo evento.
     *
     * Método HTTP: POST
     * Ruta: /api/eventos
     *
     * Recibe un EventoRequest en el cuerpo de la solicitud.
     * La anotación @Valid permite validar los datos enviados según las reglas
     * definidas en el DTO.
     *
     * Si el evento se crea correctamente, retorna:
     * - Código HTTP 201 reated
     * - La ubicación del nuevo recurso creado
     * - El objeto Evento creado
     */
    @PostMapping
    public ResponseEntity<Evento> crear(
            @Valid @RequestBody EventoRequest request
    ) {
        // Se delega la creación del evento a la capa de servicio
        Evento eventoCreado = eventoService.registrar(request);

        // Se construye la respuesta indicando la URI del nuevo evento creado
        return ResponseEntity
                .created(URI.create("/api/eventos/" + eventoCreado.getId()))
                .body(eventoCreado);
    }

    /**
     * Endpoint para listar todos los eventos registrados.
     *
     * Método HTTP: GET
     * Ruta: /api/eventos
     *
     * Retorna la lista completa de eventos con estado HTTP 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Evento>> listar() {
        return ResponseEntity.ok(eventoService.listar()); // Se solicita al servicio la lista de eventos
    }

    /**
     * Endpoint para obtener un evento por su idetificador.
     *
     * Método HTTP: GET
     * Ruta: /api/eventos/{id}
     *
     * El ID se recibe desde la URL mediante @PathVariable.
     *
     * Si el evento existe:
     * - Retorna 200 OK con el evento encontrado.
     *
     * Si el evento no existe:
     * - Retorna 404 Not Found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Evento> obtenerPorId(@PathVariable Long id) {

        Optional<Evento> evento = eventoService.obtenerPorId(id);// Se consulta el evento por ID mediante el servicio
        return evento.map(ResponseEntity::ok)// Si existe, se responde con 200 OK; si no existe, con 404 Not Found
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Endpoint para actualizar un evento existente.
     *
     * Método HTTP: PUT
     * Ruta: /api/eventos/{id}
     *
     * Recibe:
     * - El ID del evento desde la URL
     * - Los nuevos datos del evento n el cuerpo de la solicitud
     *
     * La anotación @Valid valida que el request cumpla las reglas definidas.
     *
     * Si la actualización es exitosa, retorna el evento actualizado
     * con estado HTTP 200 OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Evento> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EventoRequest request
    ) {

        Evento eventoActualizado = eventoService.actualizar(id, request);// Se delega la actualización del evento a la capa de servicio
        return ResponseEntity.ok(eventoActualizado);// Se retorna el evento actualizado
    }

    /**
     * Endpoint para validar si existe conflñcto de horario.
     *
     * Método HTTP: GET
     * Ruta: /api/eventos/conflicto
     *
     * Este endpoint permite probar la regla de negocio relacionada con
     * el cruce de horarios entre eventos.
     *
     * Recibe por parámetros:
     * - fecha
     * - horaInicio
     * - horaFin
     * - eventoIdExcluir, opcional
     *
     * El parámetro eventoIdExcluir se utiliza principalmente en actualizaciones,
     * para evitar que el sistema compare el evento consigo mismo.
     */
    @GetMapping("/conflicto")
    public ResponseEntity<Map<String, Object>> validarConflictoHorario(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime horaInicio,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime horaFin,

            @RequestParam(required = false)
            Long eventoIdExcluir
    ) {
        // Se consulta en la capa de srvicio si existe conflicto de horario
        boolean hayConflicto = eventoService.existeConflictoHorario(
                fecha,
                horaInicio,
                horaFin,
                eventoIdExcluir
        );

        /*
         * Se construye una respuesta detallada para facilitar las pruebas.
         * LinkedHashMap permite conservar el orden en qu se agregan los datos.
         */
        Map<String, Object> respuesta = new LinkedHashMap<>();

        // Se agregan los datos recibidos y el resultado de la validación
        respuesta.put("fecha", fecha);
        respuesta.put("horaInicio", horaInicio);
        respuesta.put("horaFin", horaFin);
        respuesta.put("eventoIdExcluir", eventoIdExcluir);
        respuesta.put("conflicto", hayConflicto);

        return ResponseEntity.ok(respuesta);// Se retorna la respuesta con estado 200 OK
    }
}