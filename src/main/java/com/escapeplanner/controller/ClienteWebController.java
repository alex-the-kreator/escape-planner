package com.escapeplanner.controller;

import com.escapeplanner.domain.enums.EstadoCliente;
import com.escapeplanner.dto.ClienteRequest;
import com.escapeplanner.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * Controlador web del módulo de clientes.
 *
 * Esta clase se encarga de manejar las vistas
 * relacionadas con el registro y listado de clientes.
 *
 * El controlador utiliza la capa de servicios existente,
 * permitiendo que el módulo web funcione junto con la API REST.
 *
 * @author Alex Mártín
 */
@Controller
@RequestMapping("/clientes")
public class ClienteWebController {

    //Servicio principal encargado de la lógica relacionada con los clientes
    private final ClienteService clienteService;

    /**
     *
     * Constructor del controlador.
     *
     * Spring Boot inyecta automáticamente
     * la dependencia ClienteService.
     *
     */
    public ClienteWebController(ClienteService clienteService) {

        this.clienteService = clienteService;
    }

    /**
     *
     * Método encargado de mostrar el listado
     * general de clientes.
     *
     * Obtiene todos los clientes desde el servicio
     * y los envía a la vista lista.html.
     *
     */
    @GetMapping
    public String listar(Model model) {

        // Se agrega la lista de clientes al modelo
        model.addAttribute(
                "clientes",
                clienteService.listar()
        );

        // Retorna la vista cliente/lista.html
        return "clientes/lista";
    }

    /**
     *
     * Método encargado de cargar el formulario
     * para registrar un nuevo cliente.
     *
     */
    @GetMapping("/nuevo")
    public String nuevo(Model model) {


        model.addAttribute( //Se crea un objetoClienteRequest vacío con valores iniciales por defecto

                "clienteRequest",

                new ClienteRequest(
                        "",
                        "",
                        "",
                        "",
                        EstadoCliente.ACTIVO
                )
        );

        //Se cargan todos los estados disponibles del enum EstadoCliente

        model.addAttribute(
                "estadosCliente",
                EstadoCliente.values()
        );
        return "clientes/formulario";// Retorna la vista formulario.html
    }

    /**
     *
     * Método encargado de guardar un cliente.
     *
     * Recibe la información enviada desde el formulario.
     *
     * @Valid ejecuta automáticamente las validaciones
     * definidas en ClienteRequest.
     *
     */
    @PostMapping("/guardar")
    public String guardar(

            @Valid
            @ModelAttribute("clienteRequest")
            ClienteRequest clienteRequest,

            BindingResult bindingResult,

            Model model,

            RedirectAttributes redirectAttributes

    ) {

        //Si existen errores de validación, el formulario se vuelve a mostrar
        if (bindingResult.hasErrors()) {

            // Se vuelven a cargar los estado para el select del formulario
            model.addAttribute(
                    "estadosCliente",
                    EstadoCliente.values()
            );

            return "clientes/formulario";// Retorna nuevamente al formulario
        }


        clienteService.registrar(clienteRequest);//Se registra el cliente utilizando la lógica de negocio del servicio

        //Mensaje temporal de éxito después de guardar correctamente
        redirectAttributes.addFlashAttribute(

                "successMessage",

                "Cliente registrado correctamente."
        );


        return "redirect:/clientes";// Redirección al listado de clientes
    }
}
