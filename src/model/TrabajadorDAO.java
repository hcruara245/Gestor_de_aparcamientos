package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrabajadorDAO {

    /**
     * Recupera los empleados asignados a un parking concreto.
     */
    public List<Trabajador> obtenerPersonalPorParking(int idParking) {
        List<Trabajador> lista = new ArrayList<>();
        String sql = "SELECT nombre, apellidos, dni, rol FROM empleados WHERE id_parking = ?";

        try {
            Connection con = ConexionBD.getConexion();

            if (con == null) {
                System.err.println("Conexión nula: No se puede cargar la plantilla de personal.");
                return lista;
            }

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, idParking);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String rol = rs.getString("rol");

                        // Asignamos turnos visuales según el rol para rellenar la JTable
                        String turnoVisual = rol.equalsIgnoreCase("guardia") ? "Noche" : "Rotativo";
                        String horarioVisual = rol.equalsIgnoreCase("guardia") ? "22:00 - 06:00" : "08:00 - 16:00";

                        // Instanciamos el modelo con los nuevos parámetros unificados
                        Trabajador t = new Trabajador(
                                rs.getString("dni"),
                                rs.getString("nombre"),
                                rs.getString("apellidos"),
                                rol,
                                turnoVisual,
                                horarioVisual
                        );
                        lista.add(t);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error de SQL al intentar cargar los empleados.");
            e.printStackTrace();
        }

        return lista;
    }

    /**
     * Elimina un empleado de la base de datos por su DNI.
     * @return true si el borrado fue exitoso.
     */
    public boolean eliminarEmpleado(String dni) {
        String sql = "DELETE FROM empleados WHERE dni = ?";

        try (Connection con = ConexionBD.getConexion()) {
            if (con == null) return false;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, dni);
                int filasAfectadas = ps.executeUpdate();
                return filasAfectadas > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error de SQL al intentar eliminar al empleado.");
            e.printStackTrace();
            return false;
        }
    }
}