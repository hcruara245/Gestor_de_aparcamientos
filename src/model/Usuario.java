package model;

/**
 * Clase auxiliar para verificar los permisos de acceso en la barrera.
 */
public class Usuario {
    private String dni;
    private String nombre;
    private String tipoUsuario; // normal, abonado o trabajador
    private int idParkingAsignado;
    private boolean pagoMensualAlDia; // Controla si tiene recibos pendientes

    public Usuario(String dni, String nombre, String tipoUsuario, int idParkingAsignado, boolean pagoMensualAlDia) {
        this.dni = dni;
        this.nombre = nombre;
        this.tipoUsuario = tipoUsuario;
        this.idParkingAsignado = idParkingAsignado;
        this.pagoMensualAlDia = pagoMensualAlDia;
    }

    // --- GETTERS ---
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getTipoUsuario() { return tipoUsuario; }
    public int getIdParkingAsignado() { return idParkingAsignado; }
    public boolean isPagoMensualAlDia() { return pagoMensualAlDia; }
}