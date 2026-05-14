ALTER TABLE eventos
ADD COLUMN IF NOT EXISTS sede VARCHAR(30);

DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM pg_constraint
        WHERE conname = 'chk_eventos_sede'
    ) THEN
        ALTER TABLE eventos
        ADD CONSTRAINT chk_eventos_sede
        CHECK (sede IN ('CEDRITOS', 'NIZA', 'MULTIPLAZA', 'LAGO'));
    END IF;
END $$;

-- Ajusta las sedes existentes antes de activar NOT NULL.
-- Ejemplo:
-- UPDATE eventos SET sede = 'NIZA' WHERE id = 1;

SELECT id, cliente_id, fecha, hora_inicio, hora_fin, sede
FROM eventos
ORDER BY id;
