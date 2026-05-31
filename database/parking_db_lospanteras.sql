-- =========================================================================
-- SCRIPT DE INICIALIZACIÓN - GESTOR DE APARCAMIENTOS "LOS PANTERAS"
-- =========================================================================

-- BORRADO DE BBDD PARA PRUEBAS (Evita solapamientos si el profesor lo ejecuta varias veces)
DROP DATABASE IF EXISTS parking_db;

-- Creación de la Base de Datos
CREATE DATABASE parking_db;
USE parking_db;

-- =========================================================================
-- CREACIÓN DE ESTRUCTURA DE TABLAS (DDL)
-- =========================================================================

-- 1. GESTIÓN DE APARCAMIENTOS
CREATE TABLE Aparcamientos (
                               id_parking INT NOT NULL AUTO_INCREMENT,
                               nombre VARCHAR(100) NOT NULL,
                               sucursal VARCHAR(100) NOT NULL,
                               capacidad INT NOT NULL DEFAULT 600,
                               PRIMARY KEY (id_parking)
);

-- 2. PERSONAL Y TURNOS
CREATE TABLE Empleados (
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

CREATE TABLE Turnos (
                        id_turno INT NOT NULL AUTO_INCREMENT,
                        fecha DATE NOT NULL,
                        hora_inicio TIME NOT NULL,
                        id_parking INT NOT NULL,
                        PRIMARY KEY (id_turno),
                        FOREIGN KEY (id_parking) REFERENCES Aparcamientos(id_parking)
);

CREATE TABLE Asignacion_Turnos (
                                   id_turno INT NOT NULL,
                                   dni_empleado VARCHAR(9) NOT NULL,
                                   PRIMARY KEY (id_turno, dni_empleado),
                                   FOREIGN KEY (id_turno) REFERENCES Turnos(id_turno),
                                   FOREIGN KEY (dni_empleado) REFERENCES Empleados(dni)
);

-- 3. VEHÍCULOS Y USUARIOS
CREATE TABLE Vehiculos (
                           matricula VARCHAR(15) NOT NULL,
                           marca VARCHAR(50) DEFAULT NULL,
                           modelo VARCHAR(50) DEFAULT NULL,
                           PRIMARY KEY (matricula)
);

CREATE TABLE Clientes (
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

CREATE TABLE Estancias (
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
CREATE TABLE Servicios_Disponibles (
                                       id_servicio INT NOT NULL AUTO_INCREMENT,
                                       nombre_servicio VARCHAR(100) NOT NULL,
                                       precio DECIMAL(5,2) NOT NULL,
                                       PRIMARY KEY (id_servicio)
);

CREATE TABLE Servicios_Contratados (
                                       id_estancia INT NOT NULL,
                                       id_servicio INT NOT NULL,
                                       fecha_servicio DATE NOT NULL,
                                       hora_servicio TIME NOT NULL,
                                       PRIMARY KEY (id_estancia, id_servicio, fecha_servicio, hora_servicio),
                                       FOREIGN KEY (id_estancia) REFERENCES Estancias(id_estancia),
                                       FOREIGN KEY (id_servicio) REFERENCES Servicios_Disponibles(id_servicio)
);

CREATE TABLE Proveedores (
                             id_proveedor INT NOT NULL AUTO_INCREMENT,
                             nombre VARCHAR(100) NOT NULL,
                             direccion VARCHAR(255) NOT NULL,
                             telefono VARCHAR(20) NOT NULL,
                             tiempo_entrega_dias INT NOT NULL,
                             PRIMARY KEY (id_proveedor)
);

CREATE TABLE Articulos_Almacen (
                                   id_articulo INT NOT NULL AUTO_INCREMENT,
                                   nombre VARCHAR(100) NOT NULL,
                                   stock_actual INT NOT NULL,
                                   capacidad_maxima INT NOT NULL,
                                   id_proveedor INT NOT NULL,
                                   PRIMARY KEY (id_articulo),
                                   FOREIGN KEY (id_proveedor) REFERENCES Proveedores(id_proveedor)
);


-- =========================================================================
-- VOLCADO DE DATOS DE PRUEBA (DML)
-- =========================================================================

-- 1. PARKINGS
INSERT INTO Aparcamientos (nombre, sucursal, capacidad) VALUES
                                                            ('Parking Los Panteras - Norte', 'Sucursal A', 600),
                                                            ('Parking Los Panteras - Centro', 'Sucursal B', 600),
                                                            ('Parking Los Panteras - Sur', 'Sucursal C', 600);

-- 2. SERVICIOS Y PROVEEDORES
INSERT INTO Servicios_Disponibles (nombre_servicio, precio) VALUES
                                                                ('Limpieza Exterior', 3.00),
                                                                ('Limpieza Completa (Interior y Exterior)', 5.41),
                                                                ('Cambio de Aceite', 21.04);

INSERT INTO Proveedores (nombre, direccion, telefono, tiempo_entrega_dias) VALUES
                                                                               ('Lubricantes Calatayud', 'Polígono Industrial Las Estaciones, Cl 4', '976112233', 2),
                                                                               ('Suministros Limpieza Panteras', 'Av. de la Almunia 45', '976445566', 3);

INSERT INTO Articulos_Almacen (nombre, stock_actual, capacidad_maxima, id_proveedor) VALUES
                                                                                         ('Garrafa Aceite Motor 5W30', 50, 100, 1),
                                                                                         ('Jabón Carrocerías Concentrado', 5, 100, 2);

-- 3. VEHÍCULOS
INSERT INTO Vehiculos (matricula, marca, modelo) VALUES
                                                     ('1234ABC', 'Seat', 'Ibiza'),
                                                     ('5678DEF', 'Renault', 'Clio'),
                                                     ('9999XYZ', 'Audi', 'A3'),
                                                     ('1111MMM', 'BMW', 'X3'),
                                                     ('1212TTR', 'Ford', 'Focus');

-- 4. EMPLEADOS
INSERT INTO Empleados (dni, nombre, apellidos, rol, jornada_semanal, salario_hora, id_parking) VALUES
                                                                                                   ('12121212T', 'Carlos', 'Guardia Almacen', 'guardia', 40, 5.41, 1),
                                                                                                   ('34343434P', 'Pepe', 'Pérez Restrepo', 'empleado', 40, 5.41, 1),
                                                                                                   ('56565656S', 'Sandra', 'Sánchez Ortiz', 'empleado', 20, 5.41, 1);

-- 5. CLIENTES (SE ASIGNA EL ID EXPLÍCITAMENTE PARA EVITAR FALLOS DE FOREIGN KEY)
INSERT INTO Clientes (id_cliente, tipo_usuario, nombre, dni, tarjeta_abonado, cuota_mensual, id_parking_asignado, n_plaza_asignada) VALUES
                                                                                                                                        (1, 'normal', 'Hugo Cruz Aranda', '12345678A', NULL, NULL, NULL, NULL),
                                                                                                                                        (2, 'normal', 'Ana Martínez Gómez', '87654321B', NULL, NULL, NULL, NULL),
                                                                                                                                        (3, 'abonado', 'Javier López Pérez', '99999999X', 'TARJ-9999', 120.20, 1, 45),
                                                                                                                                        (4, 'abonado', 'Marta Soler Ruiz', '11111111M', 'TARJ-1111', 0.00, 1, 46),
                                                                                                                                        (5, 'trabajador', 'Carlos Guardia Almacen', '12121212T', 'TARJ-WORK-01', NULL, 1, NULL);

-- 6. ESTANCIAS
INSERT INTO Estancias (matricula, id_parking, id_cliente, fecha_entrada, hora_entrada, fecha_salida, hora_salida, ticket_compra_cc, total_pagar) VALUES
                                                                                                                                                     ('1234ABC', 1, 1, CURDATE(), '10:00:00', NULL, NULL, FALSE, 0.00),
                                                                                                                                                     ('5678DEF', 1, 2, DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:00:00', NULL, NULL, FALSE, 0.00),
                                                                                                                                                     ('9999XYZ', 1, 3, CURDATE(), '07:30:00', NULL, NULL, FALSE, 0.00),
                                                                                                                                                     ('9999XYZ', 1, 3, CURDATE(), '01:00:00', NULL, NULL, FALSE, 0.00);

-- Insertar un turno de prueba para que la tabla no esté vacía
INSERT INTO Turnos (fecha, hora_inicio, id_parking) VALUES
                                                        (CURDATE(), '08:00:00', 1),
                                                        (CURDATE(), '16:00:00', 1);

-- Asignar los empleados a esos turnos (usando los DNIs que ya creamos)
INSERT INTO Asignacion_Turnos (id_turno, dni_empleado) VALUES
                                                           (1, '12121212T'), -- Carlos Guardia
                                                           (2, '34343434P'); -- Pepe