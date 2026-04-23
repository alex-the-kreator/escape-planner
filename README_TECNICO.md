# Escape Planner - Notas Tecnicas

Autor: Alex Mártin

## Idea general

Este proyecto se esta construyendo como una aplicacion web interna para
organizar la gestion administrativa de eventos especiales de Escape Room Colombia.
La intencion no es reemplazar otros sistemas de la empresa, sino centralizar
la informacion relevante de clientes, eventos, tareas, pagos, documentos y bloqueos.

## Decision de arquitectura

Se esta usando una arquitectura monolitica por capas con Spring Boot.
Decidi mantener esta estructura porque para el alcance universitario del MVP
es suficiente, es mas entendible al sustentar y evita complejidad innecesaria.

Capas principales:

- `controller`: recibe solicitudes web y conecta con vistas.
- `service`: define contratos de logica del negocio.
- `service.impl`: implementara reglas y validaciones.
- `repository`: acceso a datos con JPA.
- `domain.entity`: entidades mapeadas a la base de datos.
- `dto`: objetos para entrada de datos desde formularios.

## Criterios aplicados hasta ahora

- Se tomo el SQL como fuente de verdad tecnica para nombres de tablas, columnas y relaciones.
- Se tomo la documentacion funcional como base para definir enums y reglas iniciales.
- Se tiparon como enum los campos que ya estaban claramente cerrados en la documentacion.
- Se dejaron validaciones basicas en entidades y DTOs para evitar datos inconsistentes.
- El modulo de eventos se dejo como centro del sistema porque desde ahi se conectan
  tareas, pagos, documentos y bloqueos.

## Cosas pendientes

- Implementar `service.impl`.
- Crear controladores MVC.
- Configurar seguridad funcional con Spring Security.
- Construir vistas con Thymeleaf.
- Agregar mensajes de validacion mas orientados al usuario final.

## Nota personal

Procure que la estructura del codigo quedara clara para poder explicarla en sustentacion,
especialmente la separacion por capas y la relacion entre documentacion, base de datos
y reglas del negocio.
