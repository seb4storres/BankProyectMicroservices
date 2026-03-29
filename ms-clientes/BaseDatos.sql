-- Script de creación de base de datos para ms-clientes
-- PostgreSQL 15+

-- Crear base de datos si no existe
-- Ejecutar como superuser: CREATE DATABASE ms_clientes;

-- Crear secuencia para ID de clientes
CREATE SEQUENCE IF NOT EXISTS clientes_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- Crear tabla de clientes
-- Nota: Persona es @MappedSuperclass, así que sus campos se incluyen en la tabla de clientes
CREATE TABLE IF NOT EXISTS clientes (
    cliente_id BIGINT PRIMARY KEY DEFAULT nextval('clientes_seq'),
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20) NOT NULL,
    edad INTEGER NOT NULL,
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion VARCHAR(200) NOT NULL,
    telefono VARCHAR(20) NOT NULL,
    contrasena VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Crear índices para mejorar búsquedas
CREATE INDEX IF NOT EXISTS idx_clientes_identificacion ON clientes(identificacion);
CREATE INDEX IF NOT EXISTS idx_clientes_nombre ON clientes(nombre);
CREATE INDEX IF NOT EXISTS idx_clientes_estado ON clientes(estado);

-- Crear tabla de auditoría (opcional pero recomendada para Senior)
CREATE TABLE IF NOT EXISTS cliente_audit_log (
    audit_id BIGSERIAL PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    action VARCHAR(20) NOT NULL, -- CREATE, UPDATE, DELETE
    old_values JSONB,
    new_values JSONB,
    changed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    changed_by VARCHAR(100)
);

CREATE INDEX IF NOT EXISTS idx_cliente_audit_cliente_id ON cliente_audit_log(cliente_id);
CREATE INDEX IF NOT EXISTS idx_cliente_audit_action ON cliente_audit_log(action);
CREATE INDEX IF NOT EXISTS idx_cliente_audit_changed_at ON cliente_audit_log(changed_at);

-- Insertar datos de ejemplo (opcional)
-- Nota: Las contraseñas aquí son solo para demostración; en producción usar hash bcrypt

INSERT INTO clientes (nombre, genero, edad, identificacion, direccion, telefono, contrasena, estado)
VALUES
    ('Jose Lema', 'Masculino', 35, '1234567890', 'Otavalo sn y principal', '098254785', '$2a$10$encrypted_password_1', true),
    ('Marianela Montalvo', 'Femenino', 28, '1234567891', 'Amazonas y NNUU', '097548965', '$2a$10$encrypted_password_2', true),
    ('Juan Osorio', 'Masculino', 42, '1234567892', '13 junio y Equinoccial', '098874587', '$2a$10$encrypted_password_3', true)
ON CONFLICT (identificacion) DO NOTHING;

-- Commit (si está en transacción)
COMMIT;

