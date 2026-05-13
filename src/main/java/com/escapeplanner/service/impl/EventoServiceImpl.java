package com.escapeplanner.service.impl;


import com.escapeplanner.domain.entity.Cliente;// Entidades principales utilizadas por el servicio
import com.escapeplanner.domain.entity.Evento;// Cliente y Usuario son necesarios porque cada evento debe quedar asociado a ambos
import com.escapeplanner.domain.entity.Usuario;
import com.escapeplanner.domain.enums.EstadoEvento;// Enum que define los posibles estados del evento, como CONFIRMADO o CANCELADO
import com.escapeplanner.dto.EventoRequest;// DTO que recibe los datos necesarios para registrar o actualizar un evento
import com.escapeplanner.exception.BusinessException;// Excepciones personalizadas utilizadas para controlar errores de negocio y recursos no encontrados
import com.escapeplanner.exception.ResourceNotFoundException;
import com.escapeplanner.repository.ClienteRepository;// Repositorios encargados del acceso a datos de cada entidad
import com.escapeplanner.repository.EventoRepository;
import com.escapeplanner.repository.UsuarioRepository;
import com.escapeplanner.service.EventoService;// Interfaz que esta clase implementa
import org.springframework.stereotype.Service;// Anotaciones de Spring para declarar el servicio y controlar transacciones
import org.springframework.transaction.annotation.Transactional;

// Clases para el manejo de fechas, horas, listas y valores opcionales
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Implementación de la capa de servicio para el módulo de eventos
 *
 * En esta clase se centraliza la lógica principal relacionada con los eventos,
 * como el registro, actualización, consulta y validación de conflictos de horario
 *
 * También se valida que el cliente y el usuario asociados al evento existan
 * antes de guardar o actualizar la información
 *
 * @author Alex Mártin
 */
@Service
@Transactional
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository; //Repositorio encargado de gestionar las operaciones de persistenciarelacionadas con la entidad Evento
    private final ClienteRepository clienteRepository; //Repositorio utilizado para consultar clientes existentes, esto permite validar que el evento siemre quede asociado a un cliente válido
    private final UsuarioRepository usuarioRepository; //Repositorio utilizado para consultar usuarios existentes, esto permite asignarcorrectamente el responsable o usuario relacionado al evento.

    /**
     * Constructor de la clase
     *
     * Se utiliza inyección de dependencias por constructor, ya que es una buena práctica
     * en Spring Boot. De sta forma, Spring entrega automáticamente los repositorios
     * necesarios para que el servicio pueda funcionar
     */
    public EventoServiceImpl(
            EventoRepository eventoRepository,
            ClienteRepository clienteRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.eventoRepository = eventoRepository;
        this.clienteRepository = clienteRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Registra un nuevo evento en el sistema
     *
     * Primero se valida que el cliente y el usuario existan
     * Luego se revisa si el event confirmado genera conflicto de horario
     * Finalmente, se mapean los datos del request hacia la entidad Evento
     * y se guarda en la base de datos
     */
    @Override
    public Evento registrar(EventoRequest request) {
        Cliente cliente = obtenerClienteExistente(request.clienteId());
        Usuario usuario = obtenerUsuarioExistente(request.usuarioId());

        validarConfirmacionSinConflicto(
                request.fecha(),
                request.horaInicio(),
                request.horaFin(),
                request.estado(),
                null
        );

        Evento evento = new Evento();
        mapearDatos(request, evento, cliente, usuario);

        return eventoRepository.save(evento);
    }

    /**
     * Actualiza un evento existente
     *
     * Primero se valida que el evento exista.
     * Después se validan nuevamente el cliente y el usuario asociados
     * También se revisa ue no exista conflicto de horario si el evento queda confirmado
     *
     * En este caso se envía el ID del evento actual para excluirlo de la comparación,
     * evitando que el sistema lo tome como conflicto consigo mismo
     */
    @Override
    public Evento actualizar(Long eventoId, EventoRequest request) {
        Evento eventoExistente = obtenerEventoExistente(eventoId);
        Cliente cliente = obtenerClienteExistente(request.clienteId());
        Usuario usuario = obtenerUsuarioExistente(request.usuarioId());

        validarConfirmacionSinConflicto(
                request.fecha(),
                request.horaInicio(),
                request.horaFin(),
                request.estado(),
                eventoId
        );

        mapearDatos(request, eventoExistente, cliente, usuario);

        return eventoRepository.save(eventoExistente);
    }

    /**
     * Lista todos los eventos registrados
     *
     * Se utiliza una consulta personalizada del repositorio para ordenarlos
     * por fecha ascendente y hora e inicio ascendente
     */
    @Override
    @Transactional(readOnly = true)
    public List<Evento> listar() {
        return eventoRepository.findAllByOrderByFechaAscHoraInicioAsc();
    }

    /**
     * Busca un evento por su ID
     *
     * Se retorna Optional porque puede existir la posibilidad de que
     * no se encuentre ningún evento con el ID recibido
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Evento> obtenerPorId(Long id) {
        return eventoRepository.findById(id);
    }

    /**
     * Verifica si existe un conflicto de horario para una fecha y rango de horas.
     *
     * Esta validación se apoya en el repositorio de eventos
     * Además, se excluyen los eventoscancelados porque estos no deben bloquear
     * la programación de nuevos eventos
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existeConflictoHorario(
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            Long eventoIdExcluir
    ) {
        Long idExcluirSeguro = eventoIdExcluir != null ? eventoIdExcluir : -1L;

        return eventoRepository.existsConflictoHorario(
                fecha,
                horaInicio,
                horaFin,
                idExcluirSeguro,
                EstadoEvento.CANCELADO
        );
    }

    /**
     * Obtiene un evento existente por ID.
     *
     * Este método e usa internamente cuando el evento debe existir obligatoriamente,
     * por ejemplo, al actualizarlo. Si no existe, se lanza una excepción controlada
     */
    private Evento obtenerEventoExistente(Long eventoId) {
        return eventoRepository.findById(eventoId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el evento con id: " + eventoId
                ));
    }

    /**
     * Obtiene un cliente existente por ID.
     *
     * Esta validación garantiza que no se registre un evento asociado
     * a un cliente inexstente
     */
    private Cliente obtenerClienteExistente(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el cliente con id: " + clienteId
                ));
    }

    /**
     * Obtiene un usuario existente por ID.
     *
     * Esta validación garantiza que el evento quede asociado a un usuario válido
     * dentro del sistema.
     */
    private Usuario obtenerUsuarioExistente(Long usuarioId) {
        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el usuario con id: " + usuarioId
                ));
    }

    /**
     * Valida la regla de negocio relacionada con conflictos de horario.
     *
     * La regla definida es:
     * si un evento se quiere registrar o actualizar con estado CONFIRMADO,
     * no debe existir otro evento confirmado en el mismo rango de tiempo.
     */
    private void validarConfirmacionSinConflicto(
            LocalDate fecha,
            LocalTime horaInicio,
            LocalTime horaFin,
            EstadoEvento estado,
            Long eventoIdExcluir
    ) {
        boolean hayConflicto = existeConflictoHorario(
                fecha,
                horaInicio,
                horaFin,
                eventoIdExcluir
        );

        if (hayConflicto && EstadoEvento.CONFIRMADO.equals(estado)) {
            throw new BusinessException(
                    "Ash! No se puede confirmar el evento porque existe conflicto de horario."
            );
        }
    }

    /**
     * Mapea los datos recibidos desde el DTO hacia la entidad Evento.
     *
     * Este método se centraliza para evitar repetir el mismo código
     * tanto en el registro como en la actualización de eventos.
     */
    private void mapearDatos(
            EventoRequest request,
            Evento evento,
            Cliente cliente,
            Usuario usuario
    ) {
        evento.setCliente(cliente);
        evento.setUsuario(usuario);
        evento.setTipo(request.tipo());
        evento.setFecha(request.fecha());
        evento.setHoraInicio(request.horaInicio());
        evento.setHoraFin(request.horaFin());
        evento.setNumPersonas(request.numPersonas());
        evento.setEstado(request.estado());
        evento.setRequiereBloqueo(request.requiereBloqueo());
        evento.setDetallesLogisticos(request.detallesLogisticos());
    }
}