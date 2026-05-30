package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    /**
     * Busca un usuario por DNI para comprobar su tipo y si está al día con los pagos.
     */
    public Usuario buscarUsuarioPorDni(String dni) {
        String sql = "SELECT nombre, tipo_usuario, id_parking_asignado, cuota_mensual FROM Clientes WHERE dni = ?";

        try {
            Connection con = ConexionBD.getConexion();
            if (con == null) return null;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, dni);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String tipoUsuario = rs.getString("tipo_usuario");
                        int idParking = rs.getInt("id_parking_asignado");
                        String nombre = rs.getString("nombre");

                        // Si la cuota es 0 o menos, asumimos que el abonado debe dinero
                        double cuota = rs.getDouble("cuota_mensual");
                        boolean pagoAlDia = (cuota > 0 || tipoUsuario.equalsIgnoreCase("normal") || tipoUsuario.equalsIgnoreCase("trabajador"));

                        return new Usuario(dni, nombre, tipoUsuario, idParking, pagoAlDia);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}