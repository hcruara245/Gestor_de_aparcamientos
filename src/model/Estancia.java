package model;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Clase Entidad que representa una fila de la tabla Estancias en memoria de Java.
 */
public class Estancia {
    private int idEstancia;
    private String matricula;
    private int idParking;
    private int idCliente;
    private LocalDate fechaEntrada;
    private LocalTime horaEntrada;
    private LocalDate fechaSalida;
    private LocalTime horaSalida;
    private boolean ticketCompraCC;
    private double totalPagar;

    // Constructor para cuando registramos una ENTRADA nueva (no sabemos salida ni total)
    public Estancia(String matricula, int idParking, int idCliente) {
        this.matricula = matricula;
        this.idParking = idParking;
        this.idCliente = idCliente;
        this.fechaEntrada = LocalDate.now();
        this.horaEntrada = LocalTime.now();
        this.totalPagar = 0.00;
        this.ticketCompraCC = false;
    }

    // Constructor completo por si necesitamos recuperar todos los datos desde la BBDD
    public Estancia(int idEstancia, String matricula, int idParking, int idCliente,
                    LocalDate fechaEntrada, LocalTime horaEntrada, LocalDate fechaSalida,
                    LocalTime horaSalida, boolean ticketCompraCC, double totalPagar) {
        this.idEstancia = idEstancia;
        this.matricula = matricula;
        this.idParking = idParking;
        this.idCliente = idCliente;
        this.fechaEntrada = fechaEntrada;
        this.horaEntrada = horaEntrada;
        this.fechaSalida = fechaSalida;
        this.horaSalida = horaSalida;
        this.ticketCompraCC = ticketCompraCC;
        this.totalPagar = totalPagar;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdEstancia() { return idEstancia; }
    public void setIdEstancia(int idEstancia) { this.idEstancia = idEstancia; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public int getIdParking() { return idParking; }
    public void setIdParking(int idParking) { this.idParking = idParking; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public LocalDate getFechaEntrada() { return fechaEntrada; }
    public void setFechaEntrada(LocalDate fechaEntrada) { this.fechaEntrada = fechaEntrada; }

    public LocalTime getHoraEntrada() { return horaEntrada; }
    public void setHoraEntrada(LocalTime horaEntrada) { this.horaEntrada = horaEntrada; }

    public LocalDate getFechaSalida() { return fechaSalida; }
    public void setFechaSalida(LocalDate fechaSalida) { this.fechaSalida = fechaSalida; }

    public LocalTime getHoraSalida() { return horaSalida; }
    public void setHoraSalida(LocalTime horaSalida) { this.horaSalida = horaSalida; }

    public boolean isTicketCompraCC() { return ticketCompraCC; }
    public void setTicketCompraCC(boolean ticketCompraCC) { this.ticketCompraCC = ticketCompraCC; }

    public double getTotalPagar() { return totalPagar; }
    public void setTotalPagar(double totalPagar) { this.totalPagar = totalPagar; }
}