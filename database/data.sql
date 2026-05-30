USE parking_db;

-- =========================================================================
-- 1. VEHÍCULOS DE PRUEBA
-- =========================================================================
INSERT INTO Vehiculos (matricula, marca, modelo) VALUES
                                                     ('1234ABC', 'Seat', 'Ibiza'),      -- Coche de Usuario Normal A
                                                     ('5678DEF', 'Renault', 'Clio'),    -- Coche de Usuario Normal B (Sancionado)
                                                     ('9999XYZ', 'Audi', 'A3'),         -- Coche de Abonado Activo
                                                     ('1111MMM', 'BMW', 'X3'),          -- Coche de Abonado Moroso (Para probar bloqueo)
                                                     ('1212TTR', 'Ford', 'Focus');      -- Coche de Trabajador del Parking

-- =========================================================================
-- 2. CLIENTES CON ENUMS REALES
-- =========================================================================
-- Caso 1: Un cliente común y corriente
INSERT INTO Clientes (tipo_usuario, nombre, dni, tarjeta_abonado, cuota_mensual, id_parking_asignado, n_plaza_asignada) VALUES
                                                                                                                            ('normal', 'Hugo Cruz Aranda', '12345678A', NULL, NULL, NULL, NULL),
                                                                                                                            ('normal', 'Ana Martínez Gómez', '87654321B', NULL, NULL, NULL, NULL);

-- Caso 2: Abonado al día con plaza reservada (21,04€ es la cuota o lo que estipules)
INSERT INTO Clientes (tipo_usuario, nombre, dni, tarjeta_abonado, cuota_mensual, id_parking_asignado, n_plaza_asignada) VALUES
    ('abonado', 'Javier López Pérez', '99999999X', 'TARJ-9999', 120.20, 1, 45);

-- Caso 3: Abonado que simularemos que NO ha pagado
INSERT INTO Clientes (tipo_usuario, nombre, dni, tarjeta_abonado, cuota_mensual, id_parking_asignado, n_plaza_asignada) VALUES
    ('abonado', 'Marta Soler Ruiz', '11111111M', 'TARJ-1111', 0.00, 1, 46); -- Cuota 0.00 simula impago

-- Caso 4: Un empleado registrado como cliente para entrar con su tarjeta por barrera
INSERT INTO Clientes (tipo_usuario, nombre, dni, tarjeta_abonado, cuota_mensual, id_parking_asignado, n_plaza_asignada) VALUES
    ('trabajador', 'Carlos Guardia Almacen', '12121212T', 'TARJ-WORK-01', NULL, 1, NULL);


-- =========================================================================
-- 3. EMPLEADOS DE LA SUCURSAL (Para la Pestaña 3 del cuadrante)
-- =========================================================================
INSERT INTO Empleados (dni, nombre, apellidos, rol, jornada_semanal, salario_hora, id_parking) VALUES
                                                                                                   ('12121212T', 'Carlos', 'Guardia Almacen', 'guardia', 40, 5.41, 1),
                                                                                                   ('34343434P', 'Pepe', 'Pérez Restrepo', 'empleado', 40, 5.41, 1),
                                                                                                   ('56565656S', 'Sandra', 'Sánchez Ortiz', 'empleado', 20, 5.41, 1);


-- =========================================================================
-- 4. ESTANCIAS ACTIVAS E HISTÓRICAS (Para testear la Salida)
-- =========================================================================
-- Estancia A: Usuario normal dentro del parking que lleva poco tiempo
INSERT INTO Estancias (matricula, id_parking, id_cliente, fecha_entrada, hora_entrada, fecha_salida, hora_salida, ticket_compra_cc, total_pagar) VALUES
    ('1234ABC', 1, 1, CURDATE(), '10:00:00', NULL, NULL, FALSE, 0.00);

-- Estancia B: Usuario normal que lleva MÁS de 24 horas dentro
INSERT INTO Estancias (matricula, id_parking, id_cliente, fecha_entrada, hora_entrada, fecha_salida, hora_salida, ticket_compra_cc, total_pagar) VALUES
    ('5678DEF', 1, 2, DATE_SUB(CURDATE(), INTERVAL 2 DAY), '08:00:00', NULL, NULL, FALSE, 0.00);

-- Estancia C: Abonado que está dentro del parking actualmente
INSERT INTO Estancias (matricula, id_parking, id_cliente, fecha_entrada, hora_entrada, fecha_salida, hora_salida, ticket_compra_cc, total_pagar) VALUES
    ('9999XYZ', 1, 3, CURDATE(), '07:30:00', NULL, NULL, FALSE, 0.00);


-- =========================================================================
-- 5. SIMULACIÓN DEL 20% COMPLETO
-- =========================================================================
INSERT INTO Estancias (matricula, id_parking, id_cliente, fecha_entrada, hora_entrada, fecha_salida, hora_salida, ticket_compra_cc, total_pagar) VALUES
    ('9999XYZ', 1, 3, CURDATE(), '01:00:00', NULL, NULL, FALSE, 0.00);