# Escape Planner - Fase de Servicios y API Temporal de Clientes

Autor: Alex Mártin

## 1. Objetivo de esta fase

En esta fase se desarrolló la capa de servicio de los módulos de clientes y eventos, junto con un controlador REST temporal para el módulo de clientes. El objetivo fue dejar el backend en un estado funcional y comprobable antes de avanzar a vistas con Thymeleaf.

Esta fase se enfocó en:

- implementar lógica de negocio básica en `service.impl`
- validar existencia de entidades relacionadas
- controlar errores con excepciones claras
- exponer endpoints REST temporales para probar el mdulo de clientes

## 2. Contexto técnico

El proyecto mantiene el stack definido desde el inicio:

- Java 21
- Spring Boot 3.3.5
- PostgreSQL
- JPA / Hibernate
- Spring Security
- Thymeleaf

La arquitectura sigue siendo monolítica por capas:

- `controller`: recibe solicitudes HTTP
- `service`: define contratos del negocio
- `service.impl`: implementa la lógica
- `repository`: acceso a datos
- `domain.entity`: entidades persistentes
- `dto`: objetos de entrada
- `exception`: manejo centralizado de errores

## 3. Componentes implementados en esta fase

### Servicios implementados

- `ClienteServiceImpl`
- `EventoServiceImpl`

### Excepciones creadas

- `ResourceNotFoundException`
- `BusinessException`

### Controlador REST temporal

- `ClienteController`

### Manejo global de errores

- `GlobalExceptionHandler`

## 4. Desarrollo del módulo de clientes

Se implement\u00F3 `ClienteServiceImpl` usando exclusivamente `ClienteService` y `ClienteRepository`, sin mezclar responsabilidades con otras capas.

### Funcionalidades cubiertas

- registrar cliente
- actualizar cliente
- listar clientes
- buscar clientes por nombre o email
- obtener cliente por id
- eliminar cliente

### Decisiones aplicadas

- inyección por constructor, sin Lombok
- `@Service` en la clase
- `@Transactional` a nivel de servicio
- `@Transactional(readOnly = true)` en consultas
- mapeo centralizado de `ClienteRequest` hacia `Cliente`
- uso de `Optional<Cliente>` en el m\u00E9todo p\u00FAblico `obtenerPorId(...)`
- uso interno de `findById(...).orElseThrow(...)` cuando la existencia del cliente es obligatoria

### Regla aplicada

Si un cliente no existe al actualizar o eliminar, se lanza `ResourceNotFoundException`.

## 5. Desarrollo del m\u00F3dulo de eventos

Se implement\u00F3 `EventoServiceImpl` respetando la interfaz `EventoService` y reutilizando la consulta ya existente en `EventoRepository` para conflicto horario.

### Funcionalidades cubiertas

- registrar evento
- actualizar evento
- listar eventos ordenados por fecha y hora
- obtener evento por id
- validar conflicto horario

### Reglas de negocio aplicadas

- un evento debe estar asociado a un cliente existente
- un evento debe estar asociado a un usuario existente
- la validación de conflicto horario usa `existsConflictoHorario(...)`
- al actualizar, el mismo evento queda excluido de la validación
- si existe conflicto horario y el estado solicitado es `CONFIRMADO`, se lanza `BusinessException`

### Decisiones aplicadas

- `obtenerPorId(...)` mantiene `Optional<Evento>` porque así lo define la interfaz
- para actualizar se usa un método privado con `orElseThrow(...)`
- el mapeo de `EventoRequest` hacia `Evento` se centraliza en un solo método privado
- la lógica de `fechaCreacion` y `fechaActualizacion` se mantiene en la entidad `Evento`

## 6. API REST temporal del módulo de clientes

Para probar el backend antes de construir vistas con Thymeleaf, se implementó `ClienteController` como controlador REST temporal.

### Base path

`/api/clientes`

### Endpoints disponibles

- `POST /api/clientes`
- `GET /api/clientes`
- `GET /api/clientes?buscar=texto`
- `GET /api/clientes/{id}`
- `PUT /api/clientes/{id}`
- `DELETE /api/clientes/{id}`

### Caracteríticas del controlador

- usa `ClienteService`, no el repositorio
- usa `@Valid` para validar `ClienteRequest`
- usa `@RequestBody`, `@PathVariable` y `@RequestParam(required = false)`
- devuelve `ResponseEntity`
- responde `404` si `obtenerPorId(...)` no encuentra el cliente

## 7. Manejo global de errores

Se agregué `GlobalExceptionHandler` para unificar respuestas del backend durante las pruebas REST.

### Casos manejados

- `ResourceNotFoundException` -> HTTP 404
- `BusinessException` -> HTTP 400
- `MethodArgumentNotValidException` -> HTTP 400

### Propósito

Evitar respuestas genéricas del framework y devolver mensajes claros cuando:

- no existe un recurso
- una regla de negocio impide completar la operación
- la validación del request falla

## 8. Resultado alcanzado

Al cerrar esta fase, el proyecto quedó con estas capacidades reales:

- capa de servicio funcional para clientes
- capa de servicio funcional para eventos
- validación de conflicto horario en eventos
- validación de existencia de cliente, usuario y evento
- controlador REST temporal para probar clientes desde herramientas como Postman
- manejo global básico de errores
- compilación correcta del proyecto

## 9. Ejemplos de prueba para clientes

### Crear cliente

`POST /api/clientes`

```json
{
  "nombre": "Cliente Demo",
  "telefono": "3001234567",
  "email": "cliente.demo@correo.com",
  "canalContacto": "WhatsApp",
  "estado": "ACTIVO"
}
```

### Buscar clientes

`GET /api/clientes?buscar=demo`

### Obtener cliente por id

`GET /api/clientes/1`

### Actualizar cliente

`PUT /api/clientes/1`

```json
{
  "nombre": "Cliente Demo Actualizado",
  "telefono": "3009990000",
  "email": "cliente.actualizado@correo.com",
  "canalContacto": "Correo",
  "estado": "ACTIVO"
}
```

### Eliminar cliente

`DELETE /api/clientes/1`

## 10. Lo que no se trabajó todavía

En esta fase no se avanzó a:

- vistas Thymeleaf
- seguridad funcional con Spring Security
- controlador REST de eventos
- módulos de tareas, pagos, documentos y bloqueos
- integración con sistemas externos
- facturación o automatización operativa avanzada

## 11. Conclusión

Esta fase permitió pasar de una base técnica inicial a un backend ya funcional en dos módulos clave del sistema: clientes y eventos. También dejé un punto de prueba real mediante API REST para validar el comportamiento del módulo de clientes antes de construir la interfaz final.

El avance logrado es importante porque ya existe separación clara entre capas, reglas de negocio básicas implementadas y control de errores suficiente para continuar con las siguientes etapas del proyecto sin rehacer la base construida.
