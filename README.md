# Escape Planner - Fase 1

## Estructura del proyecto
Autor: Alex Mártin
```text
escape-planner/
├── database/
│   └── 01_escape_planner_fase1.sql
├── src/
│   ├── main/
│   │   ├── java/com/escapeplanner/
│   │   │   ├── EscapePlannerApplication.java
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── domain/
│   │   │   │   ├── entity/
│   │   │   │   └── enums/
│   │   │   ├── dto/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   └── service/
│   │   │       └── impl/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
├── .gitignore
└── pom.xml
```

## application.properties

Archivo ubicado en `src/main/resources/application.properties`.

```properties
spring.application.name=escape-planner
server.port=${SERVER_PORT:8080}
spring.datasource.url=${DB_URL:jdbc:postgresql://localhost:5432/escape_planner}
spring.datasource.username=${DB_USERNAME:postgres}
spring.datasource.password=${DB_PASSWORD:postgres}
spring.datasource.driver-class-name=org.postgresql.Driver
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=false
spring.jpa.open-in-view=false
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
spring.sql.init.mode=never
spring.thymeleaf.cache=false
logging.level.org.springframework.security=INFO
logging.level.org.hibernate.SQL=INFO
```

## Script SQL inicial

Archivo ubicado en `database/01_escape_planner_fase1.sql`.

Incluye:

- Tablas `usuarios`, `clientes`, `eventos`, `tareas_evento`, `pagos`, `documentos` y `bloqueos`.
- Claves primarias `BIGSERIAL`.
- Relaciones foraneas basicas entre usuarios, clientes y eventos.
- Insercion semilla de dos usuarios con roles `ASESOR` y `ADMINISTRADOR`.

## Orden de ejecución

1. Crear la base de datos PostgreSQL llamada `escape_planner`.
2. Ejecutar el script `database/01_escape_planner_fase1.sql`.
3. Configurar las variables de entorno `DB_URL`, `DB_USERNAME` y `DB_PASSWORD` si se requiere un entorno distinto al local por defecto.
4. Abrir el proyecto como proyecto Maven y resolver dependencias.
5. Ejecutar la clase `com.escapeplanner.EscapePlannerApplication`.

## Checklist de validación

- El proyecto abre como aplicación Spring Boot sin errores de estructura.
- `application.properties` apunta a PostgreSQL y `ddl-auto` está en `none`.
- El script SQL crea las 7 tablas solicitadas.
- Existen los usuarios semilla con roles `ASESOR` y `ADMINISTRADOR`.
- No se implementó lógica de negocio.
- No se implementó seguridad funcional.
- No se construyeron vistas completas del sistema.

Configuración inicial del entorno y conexión a base de datos completada.