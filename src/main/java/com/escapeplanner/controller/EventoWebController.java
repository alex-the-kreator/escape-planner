package com.escapeplanner.controller;

// Importa la entidad Cliente para trabajar con clientes en el formulario
import com.escapeplanner.domain.entity.Cliente;

// Importa la entidad Evento para convertir eventos a request cuando se editan
import com.escapeplanner.domain.entity.Evento;

// Importa la entidad Usuario para mostrar los responsables del evento
import com.escapeplanner.domain.entity.Usuario;

// Importa el enum de estados del evento
import com.escapeplanner.domain.enums.EstadoEvento;

// Importa el enum de sedes disponibles para el evento
import com.escapeplanner.domain.enums.SedeEvento;

// Importa el enum de tipos de evento
import com.escapeplanner.domain.enums.TipoEvento;

// Importa el DTO usado para recibir los datos del formulario
import com.escapeplanner.dto.EventoRequest;

// Importa la excepción de reglas de negocio
import com.escapeplanner.exception.BusinessException;

// Importa la excepción para recursos no encontrados
import com.escapeplanner.exception.ResourceNotFoundException;

// Importa el repositorio de usuarios
import com.escapeplanner.repository.UsuarioRepository;

// Importa el servicio de clientes
import com.escapeplanner.service.ClienteService;

// Importa el servicio de eventos
import com.escapeplanner.service.EventoService;

// Permite validar automáticamente el objeto recibido del formulario
import jakarta.validation.Valid;

// Marca la clase como controlador web de Spring MVC
import org.springframework.stereotype.Controller;

// Permite enviar datos desde el controlador hacia la vista
import org.springframework.ui.Model;

// Guarda los errores de validación del formulario
import org.springframework.validation.BindingResult;

// Mapea peticiones HTTP GET
import org.springframework.web.bind.annotation.GetMapping;

// Recibe datos desde el formulario y los convierte en objeto
import org.springframework.web.bind.annotation.ModelAttribute;

// Captura valores que vienen en la URL
import org.springframework.web.bind.annotation.PathVariable;

// Mapea peticiones HTTP POST
import org.springframework.web.bind.annotation.PostMapping;

// Define una ruta base para todo el controlador
import org.springframework.web.bind.annotation.RequestMapping;

// Permite enviar mensajes temporales después de una redirección
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Importa LocalDate para manejar fechas
import java.time.LocalDate;

// Importa LocalTime para manejar horas
import java.time.LocalTime;

// Importa List para manejar listas de clientes y usuarios
import java.util.List;

/**
 * Controlador web del módulo de eventos.
 *
 * Esta clase maneja las rutas web relacionadas con:
 * - listar eventos
 * - registrar eventos
 * - editar eventos
 * - actualizar eventos
 * - eliminar eventos
 *
 * @author Alex Mártin
 */


@Controller// Indica que esta clase es un controlador de Spring

// Define que todas las rutas de esta clase empiezan con /eventos
@RequestMapping("/eventos")
public class EventoWebController {

    private final EventoService eventoService;// Servicio que contiene la lógica principal de eventos
    private final ClienteService clienteService;// Servicio usado para consultar clientes registrados
    private final UsuarioRepository usuarioRepository;// Repositorio usado para consultar usuarios responsables

    // Constructor donde Spring inyecta automáticamente las dependencias
    public EventoWebController(
            EventoService eventoService,
            ClienteService clienteService,
            UsuarioRepository usuarioRepository
    ) {
        this.eventoService = eventoService;// Se asigna el servicio de eventos recibido
        this.clienteService = clienteService;// Se asigna el servicio de clientes recibido
        this.usuarioRepository = usuarioRepository;// Se asigna el repositorio de usuarios recibido
    }
    // Atiende la ruta GET /eventos
    @GetMapping
    public String listar(Model model) {

        // Agrega al modelo la lista de eventos para mostrarla en la vista
        model.addAttribute("eventos", eventoService.listar());

        // Retorna la vista donde se listan los eventos
        return "eventos/lista";
    }

    // Atiende la ruta GET /eventos/nuevo
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        // Crea un request con valore por defecto para inicializar el formulario
        model.addAttribute("eventoRequest", crearEventoRequestPorDefecto());

        // Prepara datos generales del formulari de creación
        prepararFormularioBase(model, null, "Registrar evento", "Guardar evento", "/eventos/guardar");

        cargarCatalogos(model);// Carga clientes, usuarios, tipos, sedes y estados para los selects

        // Retorna la vista del formulario de eventos
        return "eventos/formulario";
    }

    // Atiende la ruta GET /eventos/{id}/editar
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {

        // Busca el evento por ID y, si existe, prepara el formulrio de edición
        return eventoService.obtenerPorId(id)

                // Si el evento existe, se convierte a EventoRequest para cargarlo en el formulario
                .map(evento -> {

                    // Agrega al modelo los datos del evento convertido a request
                    model.addAttribute("eventoRequest", convertirARequest(evento));

                    // Prepara datos generales del formulario de edición
                    prepararFormularioBase(model, id, "Editar evento", "Actualizar evento", "/eventos/" + id + "/actualizar");

                    // Carga los catálogos necesarios para los selects
                    cargarCatalogos(model);

                    // Retorna la vista del formulario
                    return "eventos/formulario";
                })

                // Si el evento no existe, redirige al listado con mensaje de error
                .orElseGet(() -> {

                    // Agregaun mensaje temporal indicando que no se encontró el evento
                    redirectAttributes.addFlashAttribute("errorMessage", "No se encontró el evento a editar.");

                    // Redirige al listado de eventos
                    return "redirect:/eventos";
                });
    }


    @PostMapping("/guardar")// Atiende la ruta POST /eventos/guardar
    public String guardar(

            // Recibe y valida los datos enviados desde el formulario
            @Valid @ModelAttribute("eventoRequest") EventoRequest eventoRequest,

            BindingResult bindingResult,// Contiene los errores de validación si existen
            Model model,// Modelo para devolver datos a la vista
            RedirectAttributes redirectAttributes  // Permite enviar mensajes temporales después de redireccionar
    ) {

        // Si existen errores de validación, se vuelve al formulario
        if (bindingResult.hasErrors()) {

            // Se vuelve a preparar el formulario de creación
            prepararFormularioBase(model, null, "Registrar evento", "Guardar evento", "/eventos/guardar");
            cargarCatalogos(model);// Se cargan de nuevo los catálogos para que los selects no queden vacíos
            return "eventos/formulario";
        }

        try {
            // Intenta registrar el evento usando el servicio
            eventoService.registrar(eventoRequest);

            // Agrega mensaje de éxito temporal
            redirectAttributes.addFlashAttribute("successMessage", "Evento registrado correctamente.");

            return "redirect:/eventos";

        } catch (BusinessException ex) {

            // Si ocurre un error de negocio, se muesta el mensaje en el formulario
            model.addAttribute("errorMessage", ex.getMessage());

            // Se vuelve a preparar el formulario de creación
            prepararFormularioBase(model, null, "Registrar evento", "Guardar evento", "/eventos/guardar");

            // Se recargan los catálogos
            cargarCatalogos(model);

            // Retorna nuevamente al formulario
            return "eventos/formulario";
        }
    }

    // Atiende la ruta POST /eventos/{id}/actualizar
    @PostMapping("/{id}/actualizar")
    public String actualizar(

            // Captura el ID del evento desde la URL
            @PathVariable Long id,

            // Recibe y valida los datos del formulario
            @Valid @ModelAttribute("eventoRequest") EventoRequest eventoRequest,

            // Contiene los errores de validación
            BindingResult bindingResult,

            // Modelo para enviar datos a la vista
            Model model,

            // Permite enviar mensajes temporales
            RedirectAttributes redirectAttributes
    ) {

        // Si hay errores de validación, se vuelve al formulario de edición
        if (bindingResult.hasErrors()) {

            // Se vuelve a preparar el formulario en modo edición
            prepararFormularioBase(model, id, "Editar evento", "Actualizar evento", "/eventos/" + id + "/actualizar");

            // Se cargan nuevamente los catálogos
            cargarCatalogos(model);

            // Retorna al formulario
            return "eventos/formulario";
        }

        try {
            // Intenta actualizar el evento usando el servicio
            eventoService.actualizar(id, eventoRequest);

            // Agrega mensaje de éxito temporal
            redirectAttributes.addFlashAttribute("successMessage", "Evento actualizado correctamente.");

            // Redirige al listado
            return "redirect:/eventos";

        } catch (BusinessException ex) {

            // Si ocurre un error de negocio, se muestra en el formulario
            model.addAttribute("errorMessage", ex.getMessage());

            // Se vuelve a preparar el formulario en modo edición
            prepararFormularioBase(model, id, "Editar evento", "Actualizar evento", "/eventos/" + id + "/actualizar");

            // Se recargan los catálogos
            cargarCatalogos(model);

            // Retorna al formulario
            return "eventos/formulario";

        } catch (ResourceNotFoundException ex) {

            // Si no se encuentra el evento, se envía mensaje temporal
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());

            // Redirige al listado de eventos
            return "redirect:/eventos";
        }
    }

    // Atiende la ruta POST /eventos/{id}/eliminar
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {

        try {
            // Intenta eliminar el evento usando el ID recibido
            eventoService.eliminar(id);

            // Agrega mensaje de éxito temporal
            redirectAttributes.addFlashAttribute("successMessage", "Evento eliminado correctamente.");

        } catch (ResourceNotFoundException ex) {

            // Si no se encuentra el evento, muestra el error
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }

        // Redirige nuevamente al listado de eventos
        return "redirect:/eventos";
    }

    // Crea un EventoRequest con valores iniciales para el formulario de registro
    private EventoRequest crearEventoRequestPorDefecto() {

        // Retorna un objeto EventoRequest con datos por defecto
        return new EventoRequest(

                // Cliente inicial vacío
                null,

                // Usuario inicial vacío
                null,

                // Tipo de evento por defecto
                TipoEvento.CUMPLEANOS,

                // Fecha actual por defecto
                LocalDate.now(),

                // Hora de inicio por defecto
                LocalTime.of(14, 0),

                // Hora final por defecto
                LocalTime.of(16, 0),

                // Sede por defecto
                SedeEvento.NIZA,

                // Número inicial de personas
                1,

                // Estado inicial del evento
                EstadoEvento.PENDIENTE,

                // Bloqueo desactivado por defecto
                Boolean.FALSE,

                // Detalles logísticos vacíos
                ""
        );
    }

    // Convierte una entidad Evento en un EventoRequest para cargar datos en el formulario
    private EventoRequest convertirARequest(Evento evento) {

        // Retorna un request con los datos actuales del evento
        return new EventoRequest(

                evento.getCliente().getId(),
                evento.getUsuario().getId(),
                evento.getTipo(),
                evento.getFecha(),
                evento.getHoraInicio(),
                evento.getHoraFin(),
                evento.getSede(),
                evento.getNumPersonas(),
                evento.getEstado(),
                evento.getRequiereBloqueo(),
                evento.getDetallesLogisticos()
        );
    }

    // Prepara información básica que necesita el formulario
    private void prepararFormularioBase(Model model, Long eventoId, String titulo, String boton, String accion) {

        // ID del evento, usado principalmente cuando se edita
        model.addAttribute("eventoId", eventoId);

        // Título que se mostrará en el formulario
        model.addAttribute("formTitle", titulo);

        // Texto del botón principal
        model.addAttribute("submitLabel", boton);

        // Ruta a la que enviará el formulario
        model.addAttribute("formAction", accion);
    }

    // Carga los catálogos necesarios para el formulario
    private void cargarCatalogos(Model model) {


        List<Cliente> clientes = clienteService.listar();// Obtiene todos los clientes registrados


        List<Usuario> usuarios = usuarioRepository.findAll();// Obtiene todos los usuarios registrados


        model.addAttribute("clientes", clientes); // Agrega clientes al modelo


        model.addAttribute("usuarios", usuarios);// Agrega usuarios al modelo


        model.addAttribute("tiposEvento", TipoEvento.values());// Agrega todos los tipos de evento al modelo


        model.addAttribute("sedesEvento", SedeEvento.values());// Agrega todas las sedes disponibles al modelo


        model.addAttribute("estadosEvento", EstadoEvento.values()); // Agrega todos los estados de evento al modelo
    }
}