package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase DAO encargada de las operaciones CRUD de la plantilla de personal.
 * Extrae los empleados y sus turnos asignados filtrando por el parking actual.
 */
public class TrabajadorDAO {

    /**
     * Recupera los trabajadores activos y sus horarios asignados para un parking específico.
     * @param idParking ID del parking seleccionado en el menú principal.
     * @return Lista de objetos entidad Trabajador "a pelo".
     */
    public List<Trabajador> obtenerPersonalPorParking(int idParking) {
        List<Trabajador> lista = new ArrayList<>();

        // Consulta SQL con INNER JOINs para cruzar empleados, asignaciones y sus horarios de turnos
        String sql = "SELECT t.id_trabajador, t.nombre, t.apellidos, t.dni, t.puesto, tu.nombre_turno, tu.horario " +
                "FROM Trabajadores t " +
                "INNER JOIN Asignacion_Turnos a ON t.id_trabajador = a.id_trabajador " +
                "INNER JOIN Turnos tu ON a.id_turno = tu.id_turno " +
                "WHERE a.id_parking = ?";

        try {
            Connection con = ConexionBD.getConexion();

            // El seguro de vida por si la BBDD se cae o está apagada
            if (con == null) {
                System.err.println("Conexión nula: No se puede cargar la plantilla de personal.");
                return lista;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idParking); // Filtramos dinámicamente por la sucursal actual

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        // Mapeamos cada fila a nuestro molde POJO "Trabajador"
                        Trabajador t = new Trabajador(
                                rs.getInt("id_trabajador"),
                                rs.getString("nombre"),
                                rs.getString("apellidos"),
                                rs.getString("dni"),
                                rs.getString("puesto"),
                                rs.getString("nombre_turno"), // Viene de la tabla Turnos
                                rs.getString("horario")       // Viene de la tabla Turnos (Ej: "06:00 - 14:00")
                        );
                        lista.add(t);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de SQL al intentar cargar los turnos del personal.");
            e.printStackTrace();
        }

        return lista;
    }
}