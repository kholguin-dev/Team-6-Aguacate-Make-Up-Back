
-- AGUACATE MAKE UP
-- ARCHIVO 1: CREACION DE BASE DE DATOS Y TABLAS
-- Compatible con MySQL 8.0.16 o superior.
-- Ejecutar primero este archivo y luego 02_datos_muestra.sql.
-- No incluye pagos, pedidos ni checkout.
-- La aplicación actual trabaja con localStorage; para conectar MySQL se requiere backend/API.

CREATE DATABASE IF NOT EXISTS aguacate_makeup
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE aguacate_makeup;
-- 1. ROLES
CREATE TABLE IF NOT EXISTS roles (
  id_rol SMALLINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(30) NOT NULL,
  descripcion VARCHAR(150) NULL,
  PRIMARY KEY (id_rol),
  UNIQUE KEY uq_roles_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 2. USUARIOS
CREATE TABLE IF NOT EXISTS usuarios (
  id_usuario BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  rol_id SMALLINT UNSIGNED NOT NULL,
  nombres VARCHAR(100) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  correo VARCHAR(150) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  acepta_terminos TINYINT NOT NULL DEFAULT 0,
  recibe_novedades TINYINT NOT NULL DEFAULT 0,
  activo TINYINT NOT NULL DEFAULT 1,
  verificado TINYINT NOT NULL DEFAULT 0,
  fecha_registro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_usuario),
  UNIQUE KEY uq_usuarios_correo (correo),
  UNIQUE KEY uq_usuarios_telefono (telefono),
  KEY idx_usuarios_rol (rol_id),
  CONSTRAINT fk_usuarios_rol
    FOREIGN KEY (rol_id) REFERENCES roles (id_rol)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 3. CATEGORIAS
CREATE TABLE IF NOT EXISTS categorias (
  id_categoria BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  descripcion TEXT NULL,
  activo TINYINT NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_categoria),
  UNIQUE KEY uq_categorias_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 4. SUBCATEGORIAS
CREATE TABLE IF NOT EXISTS subcategorias (
  id_subcategoria BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  categoria_id BIGINT UNSIGNED NOT NULL,
  nombre VARCHAR(100) NOT NULL,
  activo TINYINT NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_subcategoria),
  UNIQUE KEY uq_subcategoria_categoria_nombre (categoria_id, nombre),
  KEY idx_subcategorias_categoria (categoria_id),
  CONSTRAINT fk_subcategoria_categoria
    FOREIGN KEY (categoria_id) REFERENCES categorias (id_categoria)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 5. PRODUCTOS
CREATE TABLE IF NOT EXISTS productos (
  id_producto BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  codigo_producto VARCHAR(20) NOT NULL,
  sku VARCHAR(45) NOT NULL,
  subcategoria_id BIGINT UNSIGNED NOT NULL,
  nombre VARCHAR(200) NOT NULL,
  marca VARCHAR(100) NOT NULL,
  descripcion TEXT NULL,
  precio_base DECIMAL(12,2) NOT NULL,
  precio_promocional DECIMAL(12,2) NULL,
  stock INT UNSIGNED NOT NULL DEFAULT 0,
  tamano VARCHAR(50) NULL,
  estado ENUM('Borrador','Publicado') NOT NULL DEFAULT 'Borrador',
  destacado TINYINT NOT NULL DEFAULT 0,
  novedades TINYINT NOT NULL DEFAULT 0,
  activo TINYINT NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_producto),
  UNIQUE KEY uq_producto_codigo (codigo_producto),
  UNIQUE KEY uq_producto_sku (sku),
  KEY idx_productos_subcategoria (subcategoria_id),
  CONSTRAINT chk_producto_precio_base CHECK (precio_base >= 0),
  CONSTRAINT chk_producto_precio_promocional CHECK (
    precio_promocional IS NULL OR
    (precio_promocional >= 0 AND precio_promocional <= precio_base)
  ),
  CONSTRAINT fk_producto_subcategoria
    FOREIGN KEY (subcategoria_id) REFERENCES subcategorias (id_subcategoria)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 6. IMAGENES_PRODUCTO
-- Se permiten hasta 5 imágenes por producto mediante orden 1..5.
-- La aplicación debe garantizar que cada producto tenga al menos una imagen
-- y exactamente una imagen principal; estas reglas requieren validación de aplicación
-- o triggers/transacciones adicionales.
CREATE TABLE IF NOT EXISTS imagenes_producto (
  id_imagen BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  producto_id BIGINT UNSIGNED NOT NULL,
  url VARCHAR(500) NOT NULL,
  texto_alternativo VARCHAR(255) NOT NULL,
  es_principal TINYINT NOT NULL DEFAULT 0,
  orden TINYINT UNSIGNED NOT NULL DEFAULT 1,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_imagen),
  UNIQUE KEY uq_imagen_orden (producto_id, orden),
  KEY idx_imagenes_producto (producto_id),
  CONSTRAINT chk_imagen_orden CHECK (orden BETWEEN 1 AND 5),
  CONSTRAINT chk_imagen_principal CHECK (es_principal IN (0,1)),
  CONSTRAINT fk_imagen_producto
    FOREIGN KEY (producto_id) REFERENCES productos (id_producto)
    ON UPDATE CASCADE ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 7. CARRITOS
-- Invitado: usuario_id NULL y token_invitado informado.
-- Registrado: usuario_id informado y token_invitado NULL.
-- UNIQUE(usuario_id) permite un solo carrito por usuario registrado.
CREATE TABLE IF NOT EXISTS carritos (
  id_carrito BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  usuario_id BIGINT UNSIGNED NULL,
  token_invitado CHAR(36) NULL,
  fecha_creacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  fecha_actualizacion DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (id_carrito),
  UNIQUE KEY uq_carrito_usuario (usuario_id),
  UNIQUE KEY uq_carrito_invitado (token_invitado),
  CONSTRAINT fk_carrito_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 8. DETALLE_CARRITO
CREATE TABLE IF NOT EXISTS detalle_carrito (
  id_detalle BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  carrito_id BIGINT UNSIGNED NOT NULL,
  producto_id BIGINT UNSIGNED NOT NULL,
  cantidad INT UNSIGNED NOT NULL,
  fecha_agregado DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_detalle),
  UNIQUE KEY uq_producto_carrito (carrito_id, producto_id),
  KEY idx_detalle_producto (producto_id),
  CONSTRAINT chk_detalle_cantidad CHECK (cantidad > 0),
  CONSTRAINT fk_detalle_carrito
    FOREIGN KEY (carrito_id) REFERENCES carritos (id_carrito)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_detalle_producto
    FOREIGN KEY (producto_id) REFERENCES productos (id_producto)
    ON UPDATE CASCADE ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 9. MENSAJES
-- usuario_id NULL permite solicitudes de visitantes sin cuenta.
CREATE TABLE IF NOT EXISTS mensajes (
  id_mensaje BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  usuario_id BIGINT UNSIGNED NULL,
  nombre_completo VARCHAR(150) NOT NULL,
  correo VARCHAR(150) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  mensaje VARCHAR(500) NOT NULL,
  acepta_politica_datos TINYINT NOT NULL,
  fecha_solicitud DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id_mensaje),
  KEY idx_mensajes_usuario (usuario_id),
  CONSTRAINT chk_mensaje_acepta_politica CHECK (acepta_politica_datos IN (0,1)),
  CONSTRAINT fk_mensaje_usuario
    FOREIGN KEY (usuario_id) REFERENCES usuarios (id_usuario)
    ON UPDATE CASCADE ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Verificación de tablas creadas
SHOW TABLES;