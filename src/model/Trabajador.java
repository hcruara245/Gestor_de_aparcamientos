package model;

/**
 * Entidad que mapea los datos de un empleado y su horario actual.
 */
public class Trabajador {

    private String dni; // Es la clave primaria real en tu tabla Empleados
    private String nombre;
    private String apellidos;
    private String puesto;
    private String turnoAsignado;  // Campo calculado para los JOINs en la interfaz
    private String horarioHoras;   // Campo calculado para mostrar las horas del turno

    // Constructor para recuperar los datos del personal de la base de datos
    public Trabajador(String dni, String nombre, String apellidos, String puesto, String turnoAsignado, String horarioHoras) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.puesto = puesto;
        this.turnoAsignado = turnoAsignado;
        this.horarioHoras = horarioHoras;
    }

    // Devuelve el nombre y apellidos maquetados en una sola cadena
    public String getNombreCompleto() {
        return this.nombre + " " + this.apellidos;
    }

    // --- GETTERS Y SETTERS ---
    public String getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellidos() { return apellidos; }
    public String getPuesto() { return puesto; }
    public String getTurnoAsignado() { return turnoAsignado; }
    public String getHorarioHoras() { return horarioHoras; }
}