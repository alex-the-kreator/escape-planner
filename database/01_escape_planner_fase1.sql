CREATE TABLE IF NOT EXISTS usuarios (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(120) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    CONSTRAINT chk_usuarios_rol CHECK (rol IN ('ASESOR', 'ADMINISTRADOR'))
);

CREATE TABLE IF NOT EXISTS clientes (
    id BIGSERIAL PRIMARY KEY,
    cedula VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(150) NOT NULL,
    telefono VARCHAR(30) NOT NULL,
    email VARCHAR(120),
    canal_contacto VARCHAR(50),
    estado VARCHAR(20)
);

CREATE TABLE IF NOT EXISTS eventos (
    id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    usuario_id BIGINT NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    fecha DATE NOT NULL,
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    sede VARCHAR(30) NOT NULL,
    num_personas INTEGER NOT NULL,
    estado VARCHAR(30) NOT NULL,
    requiere_bloqueo BOOLEAN NOT NULL DEFAULT FALSE,
    detalles_logisticos TEXT,
    fecha_creacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_eventos_cliente
        FOREIGN KEY (cliente_id) REFERENCES clientes (id),
    CONSTRAINT fk_eventos_usuario
        FOREIGN KEY (usuario_id) REFERENCES usuarios (id),
    CONSTRAINT chk_eventos_sede CHECK (sede IN ('CEDRITOS', 'NIZA', 'MULTIPLAZA', 'LAGO'))
);

CREATE TABLE IF NOT EXISTS tareas_evento (
    id BIGSERIAL PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    fecha_limite DATE,
    CONSTRAINT fk_tareas_evento_evento
        FOREIGN KEY (evento_id) REFERENCES eventos (id)
);

CREATE TABLE IF NOT EXISTS pagos (
    id BIGSERIAL PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    monto NUMERIC(12,2) NOT NULL,
    fecha DATE NOT NULL,
    metodo_pago VARCHAR(50) NOT NULL,
    comprobante VARCHAR(255),
    CONSTRAINT fk_pagos_evento
        FOREIGN KEY (evento_id) REFERENCES eventos (id)
);

CREATE TABLE IF NOT EXISTS documentos (
    id BIGSERIAL PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    tipo VARCHAR(150) NOT NULL,
    nombre VARCHAR(150) NOT NULL,
    url VARCHAR(255),
    fecha_registro DATE NOT NULL,
    CONSTRAINT fk_documentos_evento
        FOREIGN KEY (evento_id) REFERENCES eventos (id)
);

CREATE TABLE IF NOT EXISTS bloqueos (
    id BIGSERIAL PRIMARY KEY,
    evento_id BIGINT NOT NULL,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NOT NULL,
    observacion TEXT,
    CONSTRAINT fk_bloqueos_evento
        FOREIGN KEY (evento_id) REFERENCES eventos (id)
);

INSERT INTO usuarios (nombre, email, password, rol, estado)
VALUES
    ('Usuario Asesor Semilla', 'asesor@escapeplanner.local', '$2a$10$pendiente.definir.hash.segura.asesor', 'ASESOR', 'ACTIVO'),
    ('Usuario Administrador Semilla', 'admin@escapeplanner.local', '$2a$10$pendiente.definir.hash.segura.admin', 'ADMINISTRADOR', 'ACTIVO')
ON CONFLICT (email) DO NOTHING;
