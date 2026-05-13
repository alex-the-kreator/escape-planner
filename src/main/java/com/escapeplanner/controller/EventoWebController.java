package com.escapeplanner.controller;

import com.escapeplanner.domain.entity.Cliente;
import com.escapeplanner.domain.entity.Usuario;
import com.escapeplanner.domain.enums.EstadoEvento;
import com.escapeplanner.domain.enums.TipoEvento;
import com.escapeplanner.dto.EventoRequest;
import com.escapeplanner.exception.BusinessException;
import com.escapeplanner.repository.UsuarioRepository;
import com.escapeplanner.service.ClienteService;
import com.escapeplanner.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 *
 * Controlador web del módulo de eventos.
 *
 * Esta clase se encarga de manejar todas las vistas
 * relacionadas con los eventos desde Thymeleaf.
 *
 * El controlador trabaja junto con:
 * - EventoService
 * - ClienteService
 * - UsuarioRepository
 *
 * La API REST sigue funcionando de manera independiente.
 *
 * @author Alex Mártín
 */
@Controller
@RequestMapping("/eventos")
public class EventoWebController {


    private final EventoService eventoService;//Servicio principal encrgado de la lógica de negocio relacionada con los eventos
    private final ClienteService clienteService; //Servicio encargado de obtener la información de los clientes registrados
    private final UsuarioRepository usuarioRepository;//Repositorio utilizado para consultar los usuario directamente desde la base de datos.

    /**
     *
     * Constructor del controlador.
     *
     * Spring Boot inyecta automáticamente las dependencias.
     *
     */
    public EventoWebController(

            EventoService eventoService,
            ClienteService clienteService,
            UsuarioRepository usuarioRepository

    ) {

        this.eventoService = eventoService;
        this.clienteService = clienteService;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     *
     * Método encargado de mostrar el listado de eventos.
     *
     * Obtiene todos los eventos desde el servicio
     * y los envía a la vista lista.html.
     *
     */
    @GetMapping
    public String listar(Model model) {

        model.addAttribute("eventos", eventoService.listar()); // Se agrega la lista de eventos al modelo
        return "eventos/lista"; // Retorna la vista de listado
    }

    /**
     *
     * Método encargado de cargar el formulario
     * para registrar un nuevo evento.
     *
     */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {

        //Se crea un objeto EventoRequest con dato por defecto para inicializar el formulario.

        model.addAttribute(
                "eventoRequest",
                crearEventoRequestPorDefecto()
        );
        cargarCatalogos(model);// Se cargan clientes, usuarios y catálogos
        return "eventos/formulario";// Retorna la vista formulario.html
    }

    /**
     *
     * Método encargado de guardar un evento.
     *
     * Recibe la información enviada desde el formulario.
     *
     * @Valid valida automáticamente las restricciones
     * definidas en EventoRequest.
     *
     */
    @PostMapping("/guardar")
    public String guardar(

            @Valid
            @ModelAttribute("eventoRequest")
            EventoRequest eventoRequest,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes

    ) {

        if (bindingResult.hasErrors()) {//Si existen errores de validación se vuelve a cargar el formulario.

            cargarCatalogos(model);

            return "eventos/formulario";
        }

        try {

            //Se registra el evento utizand la lógica de negocio del servicio
            eventoService.registrar(eventoRequest);

            //Mensaje temporal de éxito después de guardar correctamente

            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "Evento registrado correctamente."
            );
            return "redirect:/eventos";// Redirección al listado de eventos

        } catch (BusinessException ex) {

            //Si ocurre un error de negocio se muestra el mensaje en pantalla
            model.addAttribute(
                    "errorMessage",
                    ex.getMessage()
            );

            cargarCatalogos(model);// Se vuelven a cargar los catálogos
            return "eventos/formulario";// Retorna nuevamente al formulario
        }
    }

    /**
     *
     * Método privado para crear un EventoRequest
     * con valores iniciales por defecto.
     *
     * Esto permite que el formulario cargue
     * información inicial automáticamente.
     *
     */
    private EventoRequest crearEventoRequestPorDefecto() {

        return new EventoRequest(

                // Cliente ID
                null,

                // Usuario ID
                null,

                // Tipo de evento por defecto
                TipoEvento.CUMPLEANOS,

                // Fecha actual
                LocalDate.now(),

                // Hora de inicio por defecto
                LocalTime.of(14, 0),

                // Hora final por defecto
                LocalTime.of(16, 0),

                // Número inicial de personas
                1,

                // Estado inicial del evento
                EstadoEvento.PENDIENTE,

                // El bloqueo inicia desactivado
                Boolean.FALSE,

                // Detalles logísticos vacíos
                ""
        );
    }

    /**
     *
     * Método privado encargado de cargar
     * toda la información necesaria para
     * los selects y formularios.
     *
     */
    private void cargarCatalogos(Model model) {

        // Obtiene la lista de clientes
        List<Cliente> clientes = clienteService.listar();

        // Obtiene la lista de usuarios
        List<Usuario> usuarios = usuarioRepository.findAll();

        //Se agregan los datos al modelo para ser utilizados en Thymeleaf
        model.addAttribute("clientes", clientes);

        model.addAttribute("usuarios", usuarios);

        //values() obtiene todos los valores del enum TipoEvento
        model.addAttribute(
                "tiposEvento",
                TipoEvento.values()
        );

        //values() obtiene todos losvalores del enum EstadoEvento.

        model.addAttribute(
                "estadosEvento",
                EstadoEvento.values()
        );
    }
}