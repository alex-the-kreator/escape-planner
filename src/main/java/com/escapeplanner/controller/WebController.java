package com.escapeplanner.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * Controlador principal del módulo web.
 *
 * Esta clase se encarga de manejar las rutas relacionadas
 * con la pantalla de inicio, login y dashboard.
 * @author Alex Mártín
 */
@Controller
public class WebController {

    /**
     *
     * Ruta principal del sistema.
     *
     * Cuando el usuario entra a la raíz del proyecto (/),
     * automáticamente es redireccionado a la pantalla de login.
     *
     */
    @GetMapping("/")
    public String inicio() {

        // Redirección hacia la vista login
        return "redirect:/login";
    }

    /**
     *
     * Método que carga la vista del login.
     *
     * Este método únicamente muestra la página login.html.
     *
     */
    @GetMapping("/login")
    public String login() {

        // Retorna la vista login
        return "login";
    }

    /**
     *
     * Método que procesa el formulario del login.
     *
     * Recibe:
     * - correo del usuario
     * - contraseña
     *
     * RedirectAttributes permite enviar mensajes temporales
     * después de una redirección.
     *
     */
    @PostMapping("/login")
    public String procesarLogin(

            @RequestParam String correo,// Captura el correo enviado desde el formulario
            @RequestParam String password,// Captura la contraseña enviada desde el formulario
            RedirectAttributes redirectAttributes// Permite enviar mensajes temporales a otra vista

    ) {

        redirectAttributes.addFlashAttribute(//Se agrega un mensaje temporal de éxito. Este mensaje se mostrará en el dashboard.
                "successMessage",

                "Ingreso temporal exitoso. " +
                        "La autenticación real se implementará en una fase posterior."
        );

        return "redirect:/dashboard";// Redirección hacia el dashboard principal
    }

    /**
     *
     * Método encargado de mostrar el dashboard principal.
     *
     * El dashboard funciona como menú principal
     * del sistema Escape Planner.
     *
     */
    @GetMapping("/dashboard")
    public String dashboard() {


        return "dashboard";// Retorna la vista dashboard.html
    }
}