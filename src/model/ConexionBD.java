package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Gestiona la conexión con la base de datos MySQL usando el patrón Singleton.
 */
public class ConexionBD {

    private static final String URL = "jdbc:mysql://localhost:3306/parking_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection conexion = null;

    /**
     * Devuelve la conexión activa. Si no existe o se cerró, abre una nueva.
     */
    public static Connection getConexion() {
        try {
            if (conexion == null || conexion.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión con 'parking_db' establecida correctamente.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el Driver de MySQL.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de SQL al conectar con la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }

    /**
     * Cierra la conexión de forma segura al terminar la ejecución.
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión con la base de datos cerrada.");
            } catch (SQLException e) {
                System.err.println("Error al cerrar la base de datos.");
                e.printStackTrace();
            }
        }
    }
}