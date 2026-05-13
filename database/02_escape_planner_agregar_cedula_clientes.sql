-- Ajuste para bases ya creadas antes de introducir la cedula del cliente.
-- Este script debe ejecutarse manualmente porque spring.sql.init.mode esta en never.

ALTER TABLE clientes
ADD COLUMN IF NOT EXISTS cedula VARCHAR(20);

CREATE UNIQUE INDEX IF NOT EXISTS uq_clientes_cedula
ON clientes (cedula);
