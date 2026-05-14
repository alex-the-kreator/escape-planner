package com.escapeplanner.service.impl;

import com.escapeplanner.domain.entity.Evento;
import com.escapeplanner.domain.entity.TareaEvento;
import com.escapeplanner.dto.TareaEventoRequest;
import com.escapeplanner.exception.ResourceNotFoundException;
import com.escapeplanner.repository.EventoRepository;
import com.escapeplanner.repository.TareaEventoRepository;
import com.escapeplanner.service.TareaEventoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementación del servicio de tareas del evento.
 *
 * En esta clase se desarrolla la lógica necesaria para registrar,
 * actualizar, consultar y eliminar tareas relacionadas con un evento.
 *
 * También se valida que el evento y la tarea existan antes de realizar
 * cualquier operación importante.
 *
 * @author Alex Mártin
 */
@Service
@Transactional
public class TareaEventoServiceImpl implements TareaEventoService {


    private final TareaEventoRepository tareaEventoRepository;// Repositorio encargado de las operaciones de base de datos para las tareas
    private final EventoRepository eventoRepository;// Repositorio usado para consultar y validar eventos

    public TareaEventoServiceImpl(// Constructor donde Spring inyecta automáticamente los repositorios necesarios
            TareaEventoRepository tareaEventoRepository,
            EventoRepository eventoRepository
    ) {
        this.tareaEventoRepository = tareaEventoRepository;
        this.eventoRepository = eventoRepository;
    }

    // Método para registrar una nueva tarea asociada a un evento
    @Override
    public TareaEvento registrar(TareaEventoRequest request) {


        Evento evento = obtenerEventoExistente(request.eventoId());// Primero se valida que el evento exista
        TareaEvento tareaEvento = new TareaEvento();// Se crea una nueva instancia de TareaEvento

        tareaEvento.setEvento(evento);// Se asigna el evento encontrado a la tarea
        mapearDatos(request, tareaEvento);// Se copian los datos recibidos desde el request hacia la entidad

        return tareaEventoRepository.save(tareaEvento);// Finalmente se guarda la tarea en la base de datos
    }

    // Método para actualizar una tarea existente
    @Override
    public TareaEvento actualizarEstado(Long tareaId, TareaEventoRequest request) {


        TareaEvento tareaExistente = obtenerTareaExistente(tareaId);// Se busca la tarea por su ID y se valida que exista

        Evento evento = obtenerEventoExistente(request.eventoId());// Se valida que el evento recibido también exista

        tareaExistente.setEvento(evento);// Se actualiza el evento asociado a la tarea

        mapearDatos(request, tareaExistente);// Se actualizan los datos principales de la tarea

        return tareaEventoRepository.save(tareaExistente);// Se guarda la tarea ya modificada
    }

    // Método para listar todas las tareas de un evento específico
    @Override
    @Transactional(readOnly = true)
    public List<TareaEvento> listarPorEvento(Long eventoId) {

        obtenerEventoExistente(eventoId);// Se valida que el evento exista antes de listar sus tareas

        // Se retornan las tareas ordenadas por fecha límite y luego por ID
        return tareaEventoRepository.findByEventoIdOrderByFechaLimiteAscIdAsc(eventoId);
    }

    // Método para buscar una tarea por su ID
    @Override
    @Transactional(readOnly = true)
    public Optional<TareaEvento> obtenerPorId(Long id) {

        // Se retorna un Optional porque la tarea puede existir o no
        return tareaEventoRepository.findById(id);
    }

    // Método para eliminar una tarea
    @Override
    public void eliminar(Long id) {

        TareaEvento tareaEvento = obtenerTareaExistente(id);// Primero se busca la tarea para confirmar que exista
        tareaEventoRepository.delete(tareaEvento); // Si existe, se elimina de la base de datos
    }

    // Método privado para obtener un evento existente
    private Evento obtenerEventoExistente(Long eventoId) {


        return eventoRepository.findById(eventoId)// Busca el evento por ID; si no existe, lanza una excepción personalizada
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el evento con id: " + eventoId
                ));
    }

    // Método privado para obtener una tarea existente
    private TareaEvento obtenerTareaExistente(Long tareaId) {

        // Busca la tarea por ID; si no existe, lanza una excepción personalizada
        return tareaEventoRepository.findById(tareaId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró la tarea con id: " + tareaId
                ));
    }

    // Método privado para pasar los datos del request a la entidad TareaEvento
    private void mapearDatos(TareaEventoRequest request, TareaEvento tareaEvento) {

        tareaEvento.setNombre(request.nombre()); // Se asigna el nombre de la tarea
        tareaEvento.setEstado(request.estado());// Se asigna el estado actual de la tarea
        tareaEvento.setFechaLimite(request.fechaLimite());// Se asigna la fecha límite de la tarea
    }
}