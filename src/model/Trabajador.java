package model;

/**
 * Clase Entidad que representa un empleado de la plantilla "a pelo".
 * Mapea los datos de la tabla Trabajadores de la base de datos remota.
 */
public class Trabajador {

    private int idTrabajador;
    private String nombre;
    private String apellidos;
    private String dni;
    private String puesto;         // Ej: "Mecánico", "Administrativo", "Coordinador"
    private String turnoAsignado;  // Campo extra muy útil para los JOINs (Ej: "Mañana", "Tarde", "Noche")
    private String horarioHoras;   // Campo extra para pintar el intervalo (Ej: "06:00 - 14:00")

    // Constructor completo para cuando recuperamos los datos del personal con sus turnos de la BBDD
    public Trabajador(int idTrabajador, String nombre, String apellidos, String dni, String puesto, String turnoAsignado, String horarioHoras) {
        this.idTrabajador = idTrabajador;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.dni = dni;
        this.puesto = puesto;
        this.turnoAsignado = turnoAsignado;
        this.horarioHoras = horarioHoras;
    }

    /**
     * Método de negocio simple para obtener el nombre completo formateado.
     */
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdTrabajador() { return idTrabajador; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getDni() { return dni; }
    public String getPuesto() { return puesto; }
    public String getTurnoAsignado() { return turnoAsignado; }
    public String getHorarioHoras() { return horarioHoras; }
}