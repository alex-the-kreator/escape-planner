package com.escapeplanner.controller;

import com.escapeplanner.domain.entity.Cliente;
import com.escapeplanner.domain.enums.EstadoCliente;
import com.escapeplanner.dto.ClienteRequest;
import com.escapeplanner.exception.ResourceNotFoundException;
import com.escapeplanner.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controlador web del modulo de clientes.
 *
 * @author Alex Mártin
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
        prepararFormularioCreacion(model);
        return "clientes/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return clienteService.obtenerPorId(id)
                .map(cliente -> {
                    prepararFormularioEdicion(model, cliente);
                    return "clientes/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("errorMessage", "No se encontro el cliente a editar.");
                    return "redirect:/clientes";
                });
    }

    @PostMapping("/guardar")
    public String guardar(
            @Valid @ModelAttribute("clienteRequest") ClienteRequest clienteRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            prepararFormularioBase(model, null, "Registrar cliente", "Guardar cliente", "/clientes/guardar");
            return "clientes/formulario";
        }

        clienteService.registrar(clienteRequest);
        redirectAttributes.addFlashAttribute("successMessage", "Cliente registrado correctamente.");
        return "redirect:/clientes";
    }

    @PostMapping("/{id}/actualizar")
    public String actualizar(
            @PathVariable Long id,
            @Valid @ModelAttribute("clienteRequest") ClienteRequest clienteRequest,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            prepararFormularioBase(model, id, "Editar cliente", "Actualizar cliente", "/clientes/" + id + "/actualizar");
            return "clientes/formulario";
        }

        try {
            clienteService.actualizar(id, clienteRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente actualizado correctamente.");
            return "redirect:/clientes";
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
            return "redirect:/clientes";
        }
    }

    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            clienteService.eliminar(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente eliminado correctamente.");
        } catch (ResourceNotFoundException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/clientes";
    }

    private void prepararFormularioCreacion(Model model) {
        model.addAttribute(
                "clienteRequest",
                new ClienteRequest("", "", "", "", "", EstadoCliente.ACTIVO)
        );
        prepararFormularioBase(model, null, "Registrar cliente", "Guardar cliente", "/clientes/guardar");
    }

    private void prepararFormularioEdicion(Model model, Cliente cliente) {
        model.addAttribute(
                "clienteRequest",
                new ClienteRequest(
                        cliente.getCedula(),
                        cliente.getNombre(),
                        cliente.getTelefono(),
                        cliente.getEmail(),
                        cliente.getCanalContacto(),
                        cliente.getEstado()
                )
        );
        prepararFormularioBase(model, cliente.getId(), "Editar cliente", "Actualizar cliente", "/clientes/" + cliente.getId() + "/actualizar");
    }

    private void prepararFormularioBase(Model model, Long clienteId, String titulo, String boton, String accion) {
        model.addAttribute("clienteId", clienteId);
        model.addAttribute("formTitle", titulo);
        model.addAttribute("submitLabel", boton);
        model.addAttribute("formAction", accion);
        model.addAttribute("estadosCliente", EstadoCliente.values());
    }
}
