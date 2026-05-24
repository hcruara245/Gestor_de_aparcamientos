package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión con la base de datos MySQL.
 * Sigue el patrón Singleton para mantener una única conexión activa.
 */
public class ConexionBD {

    // Datos de configuración de tu base de datos local
    private static final String URL = "jdbc:mysql://172.22.242.195:3306/parking_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection conexion = null;

    /**
     * Método para obtener la conexión activa a la base de datos.
     * @return Connection objeto de conexión listo para usar.
     */
    /**
     * Método para obtener la conexión activa a la base de datos.
     * Si la conexión se cerró, abre una nueva de forma automática.
     */
    public static Connection getConexion() {
        try {
            // Si la conexión es nula O un método la cerró previamente, abrimos una nueva
            if (conexion == null || conexion.isClosed()) {
                // Cargamos el driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establecemos la conexión remota
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión con 'parking_db' establecida (o restaurada) correctamente.");
            }
        } catch (ClassNotFoundException e) {
            System.err.println("Error: No se encontró el Driver de MySQL en las librerías.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Error de SQL al intentar conectar con la base de datos.");
            e.printStackTrace();
        }
        return conexion;
    }

    /**
     * Método para cerrar la conexión de forma segura al cerrar la aplicación.
     */
    public static void cerrarConexion() {
        if (conexion != null) {
            try {
                conexion.close();
                conexion = null;
                System.out.println("Conexión con la base de datos cerrada de forma segura.");
            } catch (SQLException e) {
                System.err.println("Error al intentar cerrar la base de datos.");
                e.printStackTrace();
            }
        }
    }
}