-- =========================================================================
-- Script_BD_usuario_MySql.sql
-- Creación de base de datos y usuario de laboratorio para Sistema de Pedidos
-- =========================================================================

CREATE DATABASE IF NOT EXISTS pedidosdb
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

CREATE USER IF NOT EXISTS 'spedidos_app'@'localhost' IDENTIFIED BY 'userapp_2026!';

GRANT ALL PRIVILEGES ON pedidosdb.* TO 'spedidos_app'@'localhost';
FLUSH PRIVILEGES;

USE pedidosdb;

CREATE TABLE IF NOT EXISTS pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente VARCHAR(120) NOT NULL,
    producto_codigo VARCHAR(40) NOT NULL,
    producto_nombre VARCHAR(150) NOT NULL,
    precio_unitario DECIMAL(12, 2) NOT NULL,
    cantidad INT NOT NULL,
    total DECIMAL(12, 2) NOT NULL,
    fecha DATETIME NOT NULL
);