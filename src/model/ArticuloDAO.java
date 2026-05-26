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

            // --- EL SEGURO DE VIDA ---
            if (con == null) {
                System.err.println("Conexión nula: No se puede obtener el almacén.");
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

    public boolean usarArticulo(int idArticulo) {
        String sqlBuscar = "SELECT stock_actual, capacidad_maxima, nombre FROM Articulos_Almacen WHERE id_articulo = ?";
        String sqlUpdate = "UPDATE Articulos_Almacen SET stock_actual = stock_actual - 1 WHERE id_articulo = ?";
        try {
            Connection con = ConexionBD.getConexion();

            // --- EL SEGURO DE VIDA ---
            if (con == null) return false;

            // 1. Restamos una unidad del stock
            try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                psUpdate.setInt(1, idArticulo);
                psUpdate.executeUpdate();
            }
            // 2. Comprobamos cómo ha quedado el porcentaje
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
     * Busca el ID de un artículo en la base de datos según su nombre.
     */
    public int buscarIdPorNombre(String nombreArticulo) {
        String sql = "SELECT id_article FROM Articulos_Almacen WHERE nombre LIKE ? LIMIT 1";
        // Nota: Revisa si en tu script SQL pusiste id_articulo o id_article. He usado id_articulo por homogeneidad.
        String sqlCorregida = "SELECT id_articulo FROM Articulos_Almacen WHERE nombre LIKE ? LIMIT 1";

        try {
            Connection con = ConexionBD.getConexion();
            if (con == null) return -1;

            try (PreparedStatement ps = con.prepareStatement(sqlCorregida)) {
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
        return -1; // No encontrado
    }
}