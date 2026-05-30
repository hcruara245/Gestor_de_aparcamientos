package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArticuloDAO {

    public List<Articulo> obtenerArticulosAlmacen() {
        List<Articulo> lista = new ArrayList<>();
        String sql = "SELECT A.id_articulo, A.nombre, A.stock_actual, A.capacidad_maxima, P.nombre AS proveedor " +
                "FROM Articulos_Almacen A " +
                "INNER JOIN Proveedores P ON A.id_proveedor = P.id_proveedor";

        try {
            Connection con = ConexionBD.getConexion();

            if (con == null) {
                System.err.println("Conexión nula: No se puede acceder al almacén.");
                return lista;
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    Articulo art = new Articulo(
                            rs.getInt("id_articulo"),
                            rs.getString("nombre"),
                            rs.getInt("stock_actual"),
                            rs.getInt("capacidad_maxima"),
                            rs.getString("proveedor")
                    );
                    lista.add(art);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al listar los artículos del almacén.");
            e.printStackTrace();
        }
        return lista;
    }

    /**
     * Resta una unidad de stock si quedan existencias y avisa si cae en nivel critico.
     */
    public boolean usarArticulo(int idArticulo) {
        String sqlBuscar = "SELECT stock_actual, capacidad_maxima FROM Articulos_Almacen WHERE id_articulo = ?";
        String sqlUpdate = "UPDATE Articulos_Almacen SET stock_actual = stock_actual - 1 WHERE id_articulo = ? AND stock_actual > 0";

        try {
            Connection con = ConexionBD.getConexion();
            if (con == null) return false;

            // Intentamos restar la unidad en la base de datos
            try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                psUpdate.setInt(1, idArticulo);
                int filasAfectadas = psUpdate.executeUpdate();

                // Si no se modifica ninguna fila es porque el stock ya estaba a 0
                if (filasAfectadas == 0) {
                    throw new RuntimeException("SIN_STOCK");
                }
            }

            // Consultamos el estado final para verificar si el stock entra en alerta
            try (PreparedStatement psBuscar = con.prepareStatement(sqlBuscar)) {
                psBuscar.setInt(1, idArticulo);
                try (ResultSet rs = psBuscar.executeQuery()) {
                    if (rs.next()) {
                        int stock = rs.getInt("stock_actual");
                        int max = rs.getInt("capacidad_maxima");
                        double porcentaje = ((double) stock / max) * 100;

                        return porcentaje <= 7.0;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Devuelve el ID del articulo filtrando por similitud en el nombre.
     */
    public int buscarIdPorNombre(String nombreArticulo) {
        String sql = "SELECT id_articulo FROM Articulos_Almacen WHERE nombre LIKE ? LIMIT 1";

        try {
            Connection con = ConexionBD.getConexion();
            if (con == null) return -1;

            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, "%" + nombreArticulo + "%");
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("id_articulo");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}