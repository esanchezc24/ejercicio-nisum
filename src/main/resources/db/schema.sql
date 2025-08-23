-- Eliminar tablas si ya existen
DROP TABLE IF EXISTS phones;
DROP TABLE IF EXISTS users;

-- Tabla de usuarios
CREATE TABLE users (
   id UUID PRIMARY KEY,
   name VARCHAR(100) NOT NULL,
   email VARCHAR(150) NOT NULL UNIQUE,
   password VARCHAR(255) NOT NULL,
   last_login TIMESTAMP NULL,
   is_active BOOLEAN DEFAULT TRUE,
   token TEXT,
   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
   updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   deleted_at TIMESTAMP NULL
);

-- Tabla de teléfonos
CREATE TABLE phones (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    number VARCHAR(50) NOT NULL,
    city_code VARCHAR(10),
    country_code VARCHAR(10),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);