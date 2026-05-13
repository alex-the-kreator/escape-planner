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
 * Controlador web del modulo de clientes.
 *
 * @author Alex Martin
 */
@Controller
@RequestMapping("/clientes")
public class ClienteWebController {

    private final ClienteService clienteService;

    public ClienteWebController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("clientes", clienteService.listar());
        return "clientes/lista";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute(
                "clienteRequest",
                new ClienteRequest(
                        "",
                        "",
                        "",
                        "",
                        "",
                        EstadoCliente.ACTIVO
                )
        );
        model.addAttribute("estadosCliente", EstadoCliente.values());
        return "clientes/formulario";
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("clienteRequest") ClienteRequest clienteRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("estadosCliente", EstadoCliente.values());
            return "clientes/formulario";
        }

        clienteService.registrar(clienteRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Cliente registrado correctamente.");
        return "redirect:/clientes";
    }
}
