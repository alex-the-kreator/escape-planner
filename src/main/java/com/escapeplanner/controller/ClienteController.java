package com.escapeplanner.controller;


import com.escapeplanner.domain.entity.Cliente;// Entidad Cliente, que representa la información del cliente en el sistema
import com.escapeplanner.dto.ClienteRequest;// DTO usado para recibir los datos enviados al crear o actualizar un cliente
import com.escapeplanner.service.ClienteService;// Servicio que conyiene la lógica de negocio del módul de clientes
import jakarta.validation.Valid; // Permite validar automáticamente el DTO recibido en la petición
import org.springframework.http.HttpStatus;// Representa códigos de estado HTTP, como 204 NO_CONTENT
import org.springframework.http.ResponseEntity; // Permite construir respuestas HTTP completas con estado y cuerpo
// Anotaciones para mapear peticiones HTTP hacia métodos del controlador
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; // Permite recibir datos enviados en el cuerpo de la petición
import org.springframework.web.bind.annotation.RequestMapping;// Define la ruta base del controlador
import org.springframework.web.bind.annotation.RequestParam;// Permite recibir parámetros opcionales desde la URL
import org.springframework.web.bind.annotation.RestController;// Indica que esta clase es un controlador REST/
import java.net.URI; // Permite construir la URI del recurso creado
import java.util.List; // Permite manejar listas de clientes
import java.util.Optional; // Permite manejar la posibilidad de que un cliente no exista

/**
 * Controlador REST temporal para probar el módulo de clientes
 * mientras la capa web con Thymeleaf aún no se construye.
 *
 * Este controlador expne endpoints HTTP para crear, consultar,
 * actualizar y eliminar clientes
 *
 * @author Alex Mártin
 */
@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;// Servicio encargado de aplicar la lógica de negocio de clientes

    //Constructor con inyección de dependencias. Spring proporciona automáticamente la implementación de ClienteService

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    //Crea un nuevo cliente.
    @PostMapping
    public ResponseEntity<Cliente> crear(@Valid @RequestBody ClienteRequest request) {
        Cliente clienteCreado = clienteService.registrar(request);

        return ResponseEntity
                .created(URI.create("/api/clientes/" + clienteCreado.getId()))
                .body(clienteCreado);
    }

    //Lista todos los clientes o reaiza una búsqueda si se envía el parámetro "buscar"
    @GetMapping
    public ResponseEntity<List<Cliente>> listar(@RequestParam(required = false) String buscar) {
        List<Cliente> clientes = (buscar == null || buscar.isBlank())
                ? clienteService.listar()
                : clienteService.buscar(buscar);

        return ResponseEntity.ok(clientes);
    }

    //Obtiene un cliente por su ID
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> obtenerPorId(@PathVariable Long id) {
        Optional<Cliente> cliente = clienteService.obtenerPorId(id);

        return cliente.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //Actualiza un cliente existente
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ClienteRequest request
    ) {
        Cliente clienteActualizado = clienteService.actualizar(id, request);
        return ResponseEntity.ok(clienteActualizado);
    }

    //Elimina un cliente por su ID.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}