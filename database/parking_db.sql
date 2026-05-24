-- Creación de la Base de Datos
CREATE DATABASE IF NOT EXISTS parking_db;
USE parking_db;

-- 1. GESTIÓN DE APARCAMIENTOS
CREATE TABLE IF NOT EXISTS Aparcamientos (
  id_parking INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  sucursal VARCHAR(100) NOT NULL,
  capacidad INT NOT NULL DEFAULT 600,
  PRIMARY KEY (id_parking)
);

-- 2. PERSONAL Y TURNOS
CREATE TABLE IF NOT EXISTS Empleados (
  dni VARCHAR(9) NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  apellidos VARCHAR(100) NOT NULL,
  rol ENUM('guardia', 'empleado') NOT NULL,
  jornada_semanal INT NOT NULL DEFAULT 40,
  salario_hora DECIMAL(5,2) NOT NULL DEFAULT 5.41,
  id_parking INT NOT NULL,
  PRIMARY KEY (dni),
  FOREIGN KEY (id_parking) REFERENCES Aparcamientos(id_parking)
);

CREATE TABLE IF NOT EXISTS Turnos (
  id_turno INT NOT NULL AUTO_INCREMENT,
  fecha DATE NOT NULL,
  hora_inicio TIME NOT NULL,
  id_parking INT NOT NULL,
  PRIMARY KEY (id_turno),
  FOREIGN KEY (id_parking) REFERENCES Aparcamientos(id_parking)
);

CREATE TABLE IF NOT EXISTS Asignacion_Turnos (
  id_turno INT NOT NULL,
  dni_empleado VARCHAR(9) NOT NULL,
  PRIMARY KEY (id_turno, dni_empleado),
  FOREIGN KEY (id_turno) REFERENCES Turnos(id_turno),
  FOREIGN KEY (dni_empleado) REFERENCES Empleados(dni)
);

-- 3. VEHÍCULOS Y USUARIOS
CREATE TABLE IF NOT EXISTS Vehiculos (
  matricula VARCHAR(15) NOT NULL,
  marca VARCHAR(50) DEFAULT NULL,
  modelo VARCHAR(50) DEFAULT NULL,
  PRIMARY KEY (matricula)
);

CREATE TABLE IF NOT EXISTS Clientes (
  id_cliente INT NOT NULL AUTO_INCREMENT,
  tipo_usuario ENUM('normal', 'abonado', 'trabajador') NOT NULL,
  nombre VARCHAR(50) NOT NULL,
  dni VARCHAR(9) NOT NULL,
  tarjeta_abonado VARCHAR(50) UNIQUE DEFAULT NULL,
  cuota_mensual DECIMAL(6,2) DEFAULT NULL,
  id_parking_asignado INT DEFAULT NULL,
  n_plaza_asignada INT DEFAULT NULL,
  PRIMARY KEY (id_cliente),
  FOREIGN KEY (id_parking_asignado) REFERENCES Aparcamientos(id_parking)
);

CREATE TABLE IF NOT EXISTS Estancias (
  id_estancia INT NOT NULL AUTO_INCREMENT,
  matricula VARCHAR(15) NOT NULL,
  id_parking INT NOT NULL,
  id_cliente INT NOT NULL,
  fecha_entrada DATE NOT NULL,
  hora_entrada TIME NOT NULL,
  fecha_salida DATE DEFAULT NULL,
  hora_salida TIME DEFAULT NULL,
  ticket_compra_cc BOOLEAN NOT NULL DEFAULT FALSE,
  total_pagar DECIMAL(6,2) NOT NULL DEFAULT 0.00,
  PRIMARY KEY (id_estancia),
  FOREIGN KEY (matricula) REFERENCES Vehiculos(matricula),
  FOREIGN KEY (id_parking) REFERENCES Aparcamientos(id_parking),
  FOREIGN KEY (id_cliente) REFERENCES Clientes(id_cliente)
);

-- 4. SERVICIOS E INVENTARIO
CREATE TABLE IF NOT EXISTS Servicios_Disponibles (
  id_servicio INT NOT NULL AUTO_INCREMENT,
  nombre_servicio VARCHAR(100) NOT NULL,
  precio DECIMAL(5,2) NOT NULL,
  PRIMARY KEY (id_servicio)
);

CREATE TABLE IF NOT EXISTS Servicios_Contratados (
  id_estancia INT NOT NULL,
  id_servicio INT NOT NULL,
  fecha_servicio DATE NOT NULL,
  hora_servicio TIME NOT NULL,
  PRIMARY KEY (id_estancia, id_servicio, fecha_servicio, hora_servicio),
  FOREIGN KEY (id_estancia) REFERENCES Estancias(id_estancia),
  FOREIGN KEY (id_servicio) REFERENCES Servicios_Disponibles(id_servicio)
);

CREATE TABLE IF NOT EXISTS Proveedores (
  id_proveedor INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  direccion VARCHAR(255) NOT NULL,
  telefono VARCHAR(20) NOT NULL,
  tiempo_entrega_dias INT NOT NULL,
  PRIMARY KEY (id_proveedor)
);

CREATE TABLE IF NOT EXISTS Articulos_Almacen (
  id_articulo INT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(100) NOT NULL,
  stock_actual INT NOT NULL,
  capacidad_maxima INT NOT NULL,
  id_proveedor INT NOT NULL,
  PRIMARY KEY (id_articulo),
  FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor)
);

-- ==========================================
-- VOLCADO DE DATOS DE PRUEBA REALES
-- ==========================================

-- Insertar los 3 Parkings oficiales
INSERT INTO Aparcamientos (nombre, sucursal, capacidad) VALUES 
('Parking Los Panteras - Norte', 'Sucursal A', 600),
('Parking Los Panteras - Centro', 'Sucursal B', 600),
('Parking Los Panteras - Sur', 'Sucursal C', 600);

-- Insertar los Servicios con los precios reales exigidos
INSERT INTO Servicios_Disponibles (nombre_servicio, precio) VALUES 
('Limpieza Exterior', 3.00),
('Limpieza Completa (Interior y Exterior)', 5.41),
('Cambio de Aceite', 21.04);

-- Insertar Proveedores reales con teléfono incluido
INSERT INTO Proveedores (nombre, direccion, telefono, tiempo_entrega_dias) VALUES
('Lubricantes Calatayud', 'Polígono Industrial Las Estaciones, Cl 4', '976112233', 2),
('Suministros Limpieza Panteras', 'Av. de la Almunia 45', '976445566', 3);

-- Insertar Artículos de almacén para pruebas de stock
INSERT INTO Articulos_Almacen (nombre, stock_actual, capacidad_maxima, id_proveedor) VALUES
('Garrafa Aceite Motor 5W30', 50, 100, 1),
('Jabón Carrocerías Concentrado', 5, 100, 2);