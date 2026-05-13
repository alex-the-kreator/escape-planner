# Escape Planner - Fase de Capa Web y Ajustes del Módulo de Eventos

## 1. Descripción de la fase

En esta fase se agregó una primera capa web con Thymeleaf sobre el proyecto **Escape Planner**, manteniendo la API REST que ya estaba funcionando.

La idea principal fue permitir una demostración visual del sistema sin reemplazar los endpoints REST existentes ni cambiar la arquitectura del proyecto.

El sistema sigue siendo un monolito Spring Boot con capas separadas para:

- controladores
- servicios
- persistencia
- vistas

---

## 2. Objetivo de esta fase

El objetivo fue dejar una interfaz web inicial y funcional para:

- mostrar una pantalla de acceso visual
- navegar a un dashboard simple
- listar y registrar clientes desde la web
- listar y registrar eventos desde la web
- conservar operativa la API REST para pruebas con Postman

Adicionalmente, en esta misma fase se corrigieron errores puntuales detectados al probar el módulo web de eventos.

---

# 3. Alcance implementado

## 3.1 Login visual temporal

Se construyó una pantalla inicial accesible desde:

```text
/login
```

Esta vista tiene un objetivo demostrativo y académico.

En esta etapa no se implementó autenticación real con la tabla `usuarios`.

### Se incluyeron:

- logo del sistema cargado desde `static/img/logo.png`
- campos de correo y contraseña
- botón de inicio de sesión
- estilo visual propio del proyecto
- firma visual `by: Alex-the-kreator`

El formulario actualmente redirige al dashboard para no bloquear la demostración de la capa web.

---

## 3.2 Dashboard web

Se implementó una vista principal accesible desde:

```text
/dashboard
```

Su objetivo es servir como punto de entrada a los módulos ya visibles del sistema.

### Incluye:

- nombre del sistema `Escape Planner`
- acceso al módulo de clientes
- acceso al módulo de eventos
- enlaces rápidos a la API REST activa

---

## 3.3 Módulo web de clientes

Se implementó el controlador web:

```text
ClienteWebController
```

### Con estas rutas:

- `GET /clientes`
- `GET /clientes/nuevo`
- `POST /clientes/guardar`

### Vistas creadas:

- `templates/clientes/lista.html`
- `templates/clientes/formulario.html`

Este módulo reutiliza directamente `ClienteService`, lo cual mantiene la lógica centralizada en la capa de servicio y evita duplicar reglas entre la web y la API REST.

---

## 3.4 Módulo web de eventos

Se implementó el controlador web:

```text
EventoWebController
```

### Con estas rutas:

- `GET /eventos`
- `GET /eventos/nuevo`
- `POST /eventos/guardar`

### Vistas creadas:

- `templates/eventos/lista.html`
- `templates/eventos/formulario.html`

Este módulo reutiliza:

- `EventoService`
- `ClienteService`
- `UsuarioRepository`

La vista de eventos aprovecha la lógica ya implementada en `EventoServiceImpl`, especialmente:

- validación de cliente existente
- validación de usuario responsable existente
- validación de conflicto horario
- restricción para no confirmar eventos con cruce de horario

Si ocurre un `BusinessException` al guardar, el mensaje se muestra directamente en la vista.

---

## 3.5 API REST mantenida

Durante esta fase no se eliminaron ni reemplazaron los endpoints REST existentes.

### Endpoints mantenidos:

- `/api/clientes`
- `/api/eventos`
- `/api/eventos/conflicto`

Esto permite que la aplicación tenga dos formas de uso en paralelo:

- interfaz web con Thymeleaf para demostración funcional
- API REST para pruebas técnicas con Postman

---

# 4. Seguridad temporal

Se mantuvo una configuración simple de seguridad en `SecurityConfig`.

### Objetivo:

- permitir pruebas del backend REST
- permitir acceso a la capa web temporal
- evitar bloquear la demostración mientras no exista autenticación real

### En esta fase:

- `/api/**` sigue permitido
- `/login`, `/dashboard`, `/clientes` y `/eventos` siguen accesibles
- no se implementó todavía login real con base de datos

---

# 5. Estilo visual agregado

Se creó una hoja de estilos en:

```text
src/main/resources/static/css/styles.css
```

La intención visual fue dar una identidad más cercana al producto, usando principalmente:

- naranja
- amarillo
- negro
- blanco

### También se incorporó:

- Bootstrap por CDN
- una estructura de layout reutilizable
- tarjetas para el dashboard
- formularios y tablas con apariencia más limpia

---

# 6. Ajustes técnicos realizados en esta fase

Además de la capa web, durante las pruebas se detectaron y corrigieron varios puntos importantes.

---

## 6.1 Corrección del listado web de eventos

La vista `/eventos` presentaba error por carga perezosa de relaciones al renderizar:

- `evento.cliente.nombre`
- `evento.usuario.nombre`

Como el proyecto trabaja con:

```text
spring.jpa.open-in-view=false
```

fue necesario ajustar `EventoRepository` para traer esas relaciones ya cargadas.

### Solución aplicada:

Uso de:

```java
@EntityGraph
```

en consultas clave del repositorio de eventos.

---

## 6.2 Corrección de serialización en Usuario

Para no exponer información sensible y evitar problemas de recursividad JSON, se ajustó la entidad `Usuario`.

### Cambios realizados:

- `password` quedó con `@JsonIgnore`
- la relación inversa `eventos` quedó con `@JsonIgnore`

Esto ayuda tanto a la API REST como a la estabilidad general del proyecto.

---

## 6.3 Búsqueda de cliente en el formulario de eventos

Inicialmente el formulario de eventos usaba una selección tradicional de cliente, lo cual no era práctico para trabajar con muchos registros.

### Se reemplazó por una búsqueda dinámica usando:

- ID
- nombre
- teléfono
- email
- canal de contacto

---

### Problema detectado

El siguiente método de Thymeleaf:

```text
#lists.isLast(clientes, cliente)
```

no existe.

Eso provocaba que la vista no renderizara correctamente.

---

### Solución aplicada

Se modificó el `th:each` usando una variable de estado:

```text
cliente, clienteStat : ${clientes}
```

y posteriormente:

```text
!clienteStat.last
```

Con eso la vista volvió a funcionar correctamente.

---

## 6.4 Autoselección por ID exacto

También se ajustó el JavaScript del formulario de eventos para que, si el usuario escribe un ID exacto como por ejemplo:

```text
6
```

el sistema seleccione automáticamente ese cliente y muestre:

- ID
- nombre
- datos complementarios

Esto mejora la experiencia de uso cuando existen muchos clientes registrados.

---

# 7. Archivos principales involucrados

## Controladores web

```text
src/main/java/com/escapeplanner/controller/WebController.java
src/main/java/com/escapeplanner/controller/ClienteWebController.java
src/main/java/com/escapeplanner/controller/EventoWebController.java
```

---

## Vistas Thymeleaf

```text
src/main/resources/templates/login.html
src/main/resources/templates/dashboard.html
src/main/resources/templates/clientes/lista.html
src/main/resources/templates/clientes/formulario.html
src/main/resources/templates/eventos/lista.html
src/main/resources/templates/eventos/formulario.html
src/main/resources/templates/fragments/layout.html
```

---

## Recursos estáticos

```text
src/main/resources/static/css/styles.css
src/main/resources/static/img/logo.png
```

---

## Ajustes técnicos complementarios

```text
src/main/java/com/escapeplanner/repository/EventoRepository.java
src/main/java/com/escapeplanner/domain/entity/Usuario.java
src/main/java/com/escapeplanner/security/SecurityConfig.java
```

---

# 8. Resultado al cerrar esta fase

Al finalizar esta etapa, el proyecto quedó con:

- login visual temporal
- dashboard funcional
- módulo web de clientes operativo
- módulo web de eventos operativo
- búsqueda de cliente mejorada en el formulario de eventos
- API REST funcionando en paralelo
- seguridad temporal sin bloquear la demostración
- estilo visual más cercano a un producto real

---

# 9. Lo que aún no se implementa

En esta fase todavía no se desarrolló lo siguiente:

- autenticación real con usuarios de base de datos
- autorización por roles `ASESOR` y `ADMINISTRADOR`
- vistas web para tareas, pagos, documentos y bloqueos
- edición web avanzada de clientes y eventos
- dashboard con métricas reales
- manejo avanzado de sesión
- integración con sistemas externos

---

# 10. Conclusión

Esta fase fue importante porque permitió pasar de un backend probado únicamente por Postman a una aplicación también navegable desde el navegador, sin perder la API REST ya construida.

Desde el punto de vista académico, esta etapa demuestra:

- reutilización correcta de la capa de servicio
- convivencia entre controladores REST y controladores web
- uso de Thymeleaf dentro de la arquitectura Spring Boot
- corrección de errores reales detectados durante pruebas funcionales

Con esto, Escape Planner ya no solo funciona a nivel técnico interno, sino que también empieza a verse y usarse como una aplicación completa.