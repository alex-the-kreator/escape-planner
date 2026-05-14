package com.escapeplanner.service.impl;

import com.escapeplanner.domain.entity.Cliente;
import com.escapeplanner.domain.entity.Evento;
import com.escapeplanner.domain.entity.Usuario;
import com.escapeplanner.domain.enums.EstadoEvento;
import com.escapeplanner.dto.EventoRequest;
import com.escapeplanner.exception.BusinessException;
import com.escapeplanner.exception.ResourceNotFoundException;
import com.escapeplanner.repository.ClienteRepository;
import com.escapeplanner.repository.EventoRepository;
import com.escapeplanner.repository.UsuarioRepository;
import com.escapeplanner.service.EventoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de la capa de servicio para el módulo de eventos.
 *
 * Esta clase contiene toda la lógica de negocio relacionada
 * con el manejo de eventos dentro del sistema Escape Planner.
 *
 * Aquí se realizan operaciones como:
 * - registrar eventos
 * - actualizar eventos
 * - validar conflictos horarios
 * - consultar eventos
 * - eliminar eventos
 *
 * @author Alex Mártin
 */
@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;// Repositorio encargado de las operaciones de eventos en base de datos
    private final ClienteRepository clienteRepository;// Repositorio utilizado para consultar clientes
    private final UsuarioRepository usuarioRepository;// Repositorio utilizado para consultar usuarios responsables

    // Constructor donde Spring inyecta automáticamente los repositorios
    public EventoServiceImpl(
            EventoRepository eventoRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.eventoRepository = eventoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Método encargado de registrar un nuevo evento
    @Override
    public Evento registrar(EventoRequest request) {

        Cliente cliente = obtenerClienteExistente(request.clienteId());// Se valida que el cliente exista
        Usuario usuario = obtenerUsuarioExistente(request.usuarioId());// Se valida que el usuario responsable exista


        validarConfirmacionSinConflicto(// Se valida que no exista conflicto horario
                request.fecha(),
                request.horaInicio(),
                request.horaFin(),
                request.estado(),
                null
        );

        Evento evento = new Evento();// Se crea una nueva instancia del evento
        mapearDatos(request, evento, cliente, usuario);// Se copian los datos del request hacia la entidad
        return eventoRepository.save(evento);// Se guarda el evento en base de datos
    }

    // Método encargado de actualizar un evento existente
    @Override
    public Evento actualizar(Long eventoId, EventoRequest request) {


        Evento eventoExistente = obtenerEventoExistente(eventoId); // Se valida que el evento exista
        Cliente cliente = obtenerClienteExistente(request.clienteId());// Se valida que el cliente exista
        Usuario usuario = obtenerUsuarioExistente(request.usuarioId());// Se valida que el usuario exista

        // Se valida nuevamente el conflicto horario
        validarConfirmacionSinConflicto(
                request.fecha(),
                request.horaInicio(),
                request.horaFin(),
                request.estado(),
                eventoId
        );

        mapearDatos(request, eventoExistente, cliente, usuario);// Se actualizan los datos del evento
        return eventoRepository.save(eventoExistente);// Se guardan los cambios
    }

    // Método para listar todos los eventos registrados
    @Override
    @Transactional(readOnly = true)
    public List<Evento> listar() {

        // Retorna los eventos ordenados por fecha y hora
        return eventoRepository.findAllByOrderByFechaAscHoraInicioAsc();
    }

    // Método para buscar un evento por ID
    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> obtenerPorId(Long id) {

        return eventoRepository.findById(id); // Se retorna Optional porque el evento puede existir o no
    }

    // Método para verificar si existe conflicto de horarios
    @Override
    @Transactional(readOnly = true)
    public boolean existeConflictoHorario(

            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            Long eventoIdExcluir

    ) {

        /*
            Si el ID es null se usa un valor seguro (-1)
            para evitar errores en la consulta.
         */
        Long idExcluirSeguro = eventoIdExcluir != null
                ? eventoIdExcluir
                : -1L;

        // Consulta si existe cruce de horarios
        return eventoRepository.existsConflictoHorario(
                fecha,
                horaInicio,
                horaFin,
                idExcluirSeguro,
                EstadoEvento.CANCELADO
        );
    }

    // Método para eliminar un evento
    @Override
    public void eliminar(Long id) {

        Evento evento = obtenerEventoExistente(id);// Se valida que el evento exista
        eventoRepository.delete(evento);// Se elimina el evento encontrado
    }

    // Método privado para obtener un evento existente
    private Evento obtenerEventoExistente(Long eventoId) {

        return eventoRepository.findById(eventoId)

                .orElseThrow(() -> new ResourceNotFoundException(

                        "No se encontró el evento con id: " + eventoId
                ));
    }

    // Método privado para obtener un cliente existente
    private Cliente obtenerClienteExistente(Long clienteId) {

        return clienteRepository.findById(clienteId)

                .orElseThrow(() -> new ResourceNotFoundException(

                        "No se encontró el cliente con id: " + clienteId
                ));
    }

    // Método privado para obtener un usuario existente
    private Usuario obtenerUsuarioExistente(Long usuarioId) {

        return usuarioRepository.findById(usuarioId)

                .orElseThrow(() -> new ResourceNotFoundException(

                        "No se encontró el usuario con id: " + usuarioId
                ));
    }

    // Método privado para validar conflictos antes de confirmar un evento
    private void validarConfirmacionSinConflicto(

            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            EstadoEvento estado,
            Long eventoIdExcluir

    ) {

        // Se consulta si existe cruce de horarios
        boolean hayConflicto = existeConflictoHorario(
                fecha,
                horaInicio,
                horaFin,
                eventoIdExcluir
        );

        /*
            Si existe conflicto y el evento
            intenta quedar confirmado,
            se lanza una excepción de negocio.
         */
        if (hayConflicto && EstadoEvento.CONFIRMADO.equals(estado)) {

            throw new BusinessException(

                    "Ash! No se puede confirmar el evento porque se cruzan los horarios."
            );
        }
    }

    // Método privado para copiar datos del request hacia la entidad Evento
    private void mapearDatos(

            EventoRequest request,
            Evento evento,
            Cliente cliente,
            Usuario usuario

    ) {
        evento.setCliente(cliente);// Cliente asociado al evento
        evento.setUsuario(usuario);// Usuario responsable del evento
        evento.setTipo(request.tipo());// Tipo de evento
        evento.setFecha(request.fecha());// Fecha del evento
        evento.setHoraInicio(request.horaInicio());// Hora inicial
        evento.setHoraFin(request.horaFin());// Hora final
        evento.setSede(request.sede());// Sede donde se realizará
        evento.setNumPersonas(request.numPersonas());// Número de personas
        evento.setEstado(request.estado());// Estado actual del evento
        evento.setRequiereBloqueo(request.requiereBloqueo());// Define si requiere bloqueo
        evento.setDetallesLogisticos(request.detallesLogisticos());// Información logística adicional
    }
}