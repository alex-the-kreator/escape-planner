# Escape Planner - Notas Técnicas

Autor: Alex Mártin

## Idea general

Este proyecto se está construyendo como una aplicación web interna para
organizar la gestion administrativa de eventos especiales de Escape Room Colombia.
La intención no es reemplazar otros sistemas de la empresa, sino centralizar
la información relevante de clientes, eventos, tareas, pagos, documentos y bloqueos.

## Decisión de arquitectura

Se está usando una arquitectura monolítica por capas con Spring Boot.
Decidí mantener esta estructura porque para el alcance universitario del MVP
es suficiente, es más entendible al sustentar y evita complejidad innecesaria.

Capas principales:

- `controller`: recibe solicitudes web y conecta con vistas.
- `service`: define contratos de lógica del negocio.
- `service.impl`: implementará reglas y validaciones.
- `repository`: acceso a datos con JPA.
- `domain.entity`: entidades mapeadas a la base de datos.
- `dto`: objetos para entrada de datos desde formularios.

## Criterios aplicados hasta ahora

- Se tomó el SQL como fuente de verdad técnica para nombres de tablas, columnas y relaciones.
- Se tomó la documentación funcional como base para definir enums y reglas iniciales.
- Se tiparon como enum los campos que ya estaban claramente cerrados en la documentación.
- Se dejaron validaciones básicas en entidades y DTOs para evitar datos inconsistentes.
- El módulo de eventos se dejó como centro del sistema porque desde ahi se conectan
  tareas, pagos, documentos y bloqueos.

## Cosas pendientes

- Implementar `service.impl`.
- Crear controladores MVC.
- Configurar seguridad funcional con Spring Security.
- Construir vistas con Thymeleaf.
- Agregar mensajes de validación más orientados al usuario final.

## Nota personal

Procure que la estructura del código quedara clara para poder explicarla en sustentación,
especialmente la separación por capas y la relación entre documentación, base de datos
y reglas del negocio.
