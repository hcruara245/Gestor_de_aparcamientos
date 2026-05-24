package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Duration;

/**
 * Clase DAO encargada de las operaciones CRUD en la tabla Estancias.
 * Sigue el patrón MVC interactuando directamente con objetos de la entidad Estancia.
 */
public class EstanciaDAO {

    /**
     * Registra la entrada de un vehículo usando el objeto entidad Estancia.
     * @param estancia Objeto entidad "a pelo" con los datos del registro.
     * @return true si la inserción en el servidor fue correcta.
     */
    public boolean registrarEntrada(Estancia estancia) {
        String sql = "INSERT INTO Estancias (matricula, id_parking, id_cliente, fecha_entrada, hora_entrada, total_pagar) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            Connection con = ConexionBD.getConexion();

            // 1. Aseguramos que el vehículo existe en el servidor remoto
            asegurarVehiculo(con, estancia.getMatricula());

            // 2. Buscamos un cliente válido de base o lo creamos
            int idClienteValido = asegurarClienteBase(con, "normal");
            estancia.setIdCliente(idClienteValido); // Seteamos el ID recuperado al objeto entidad

            // 3. Ejecutamos la inserción de la estancia
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, estancia.getMatricula());
                ps.setInt(2, estancia.getIdParking());
                ps.setInt(3, estancia.getIdCliente());
                ps.setDate(4, java.sql.Date.valueOf(estancia.getFechaEntrada()));
                ps.setTime(5, java.sql.Time.valueOf(estancia.getHoraEntrada()));
                ps.setDouble(6, estancia.getTotalPagar());

                int filasAfectadas = ps.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error al registrar entrada en la tabla Estancias.");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Busca la estancia activa de una matrícula, calcula el precio y registra la salida.
     * @param matricula Matrícula del vehículo que abandona el parking.
     * @param tieneTicketCC true si presenta el descuento de 2h del Centro Comercial.
     * @return El total a pagar en euros, o -1 si hubo algún error o no se encontró el coche.
     */
    public double registrarSalida(String matricula, boolean tieneTicketCC) {
        String sqlBuscar = "SELECT id_estancia, fecha_entrada, hora_entrada FROM Estancias WHERE matricula = ? AND fecha_salida IS NULL LIMIT 1";
        String sqlActualizar = "UPDATE Estancias SET fecha_salida = ?, hora_salida = ?, ticket_compra_cc = ?, total_pagar = ? WHERE id_estancia = ?";

        try {
            Connection con = ConexionBD.getConexion();

            try (PreparedStatement psBuscar = con.prepareStatement(sqlBuscar)) {
                psBuscar.setString(1, matricula);

                try (ResultSet rs = psBuscar.executeQuery()) {
                    if (!rs.next()) {
                        return -1; // No hay ninguna estancia abierta para esa matrícula
                    }

                    int idEstancia = rs.getInt("id_estancia");
                    LocalDate fechaEntrada = rs.getDate("fecha_entrada").toLocalDate();
                    LocalTime horaEntrada = rs.getTime("hora_entrada").toLocalTime();

                    // Datos de la salida (momento actual)
                    LocalDate fechaSalida = LocalDate.now();
                    LocalTime horaSalida = LocalTime.now();

                    // 1. Calcular el tiempo total transcurrido en minutos
                    java.time.LocalDateTime entradaCompleta = java.time.LocalDateTime.of(fechaEntrada, horaEntrada);
                    java.time.LocalDateTime salidaCompleta = java.time.LocalDateTime.of(fechaSalida, horaSalida);
                    long minutosTotales = Duration.between(entradaCompleta, salidaCompleta).toMinutes();

                    // Simulación de paso del tiempo para entornos de desarrollo si da 0 min
                    if (minutosTotales <= 0) {
                        minutosTotales = 150; // 2 horas y media por defecto
                    }

                    // 2. Aplicar tarifas y beneficios del Centro Comercial
                    long minutesFacturables = minutosTotales;
                    if (tieneTicketCC) {
                        minutesFacturables = Math.max(0, minutosTotales - 120); // 2 horas gratis (120 minutos)
                    }

                    // Tarifa estándar de 0.05€ el minuto
                    double tarifaPorMinuto = 0.05;
                    double totalPagar = minutesFacturables * tarifaPorMinuto;

                    // Redondear a dos decimales de forma limpia
                    totalPagar = Math.round(totalPagar * 100.0) / 100.0;

                    // 3. Actualizar la base de datos con los datos de salida reales
                    try (PreparedStatement psActualizar = con.prepareStatement(sqlActualizar)) {
                        psActualizar.setDate(1, java.sql.Date.valueOf(fechaSalida));
                        psActualizar.setTime(2, java.sql.Time.valueOf(horaSalida));
                        psActualizar.setBoolean(3, tieneTicketCC);
                        psActualizar.setDouble(4, totalPagar);
                        psActualizar.setInt(5, idEstancia);

                        psActualizar.executeUpdate();
                    }

                    return totalPagar; // Devolvemos el precio final calculado
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al procesar la salida en la base de datos.");
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Asegura la existencia del vehículo en la tabla Vehiculos.
     */
    private void asegurarVehiculo(Connection con, String matricula) throws SQLException {
        String sql = "INSERT IGNORE INTO Vehiculos (matricula, marca, modelo) VALUES (?, 'Genérico', 'Prueba')";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, matricula);
            ps.executeUpdate();
        }
    }

    /**
     * Busca un cliente base en la base de datos. Si no existe ninguno para el tipo de usuario,
     * lo inserta al vuelo y devuelve su ID autogenerado.
     */
    private int asegurarClienteBase(Connection con, String tipoUsuario) throws SQLException {
        String tipoEnum = "normal";
        if (tipoUsuario.toLowerCase().contains("abonado")) tipoEnum = "abonado";
        if (tipoUsuario.toLowerCase().contains("trabajador")) tipoEnum = "trabajador";

        String sqlBuscar = "SELECT id_cliente FROM Clientes WHERE tipo_usuario = ? LIMIT 1";
        try (PreparedStatement psBuscar = con.prepareStatement(sqlBuscar)) {
            psBuscar.setString(1, tipoEnum);
            try (ResultSet rs = psBuscar.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_cliente");
                }
            }
        }

        String sqlInsertar = "INSERT INTO Clientes (tipo_usuario, nombre, dni) VALUES (?, 'Cliente Genérico', '00000000T')";
        try (PreparedStatement psInsertar = con.prepareStatement(sqlInsertar, Statement.RETURN_GENERATED_KEYS)) {
            psInsertar.setString(1, tipoEnum);
            psInsertar.executeUpdate();

            try (ResultSet generatedKeys = psInsertar.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return 1;
    }
}