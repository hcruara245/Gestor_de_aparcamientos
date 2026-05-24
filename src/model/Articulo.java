package model;

/**
 * Clase Entidad que representa una fila de la tabla Articulos_Almacen.
 */
public class Articulo {
    private int idArticulo;
    private String nombre;
    private int stockActual;
    private int capacidadMaxima;
    private int idProveedor;
    private String nombreProveedor; // Campo extra útil para los JOINs en las vistas

    public Articulo(int idArticulo, String nombre, int stockActual, int capacidadMaxima, String nombreProveedor) {
        this.idArticulo = idArticulo;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.capacidadMaxima = capacidadMaxima;
        this.nombreProveedor = nombreProveedor;
    }

    // Método de negocio intuitivo: calcula el porcentaje de stock restante
    public double getPorcentajeStock() {
        if (capacidadMaxima == 0) return 0;
        return ((double) stockActual / capacidadMaxima) * 100;
    }

    // --- GETTERS Y SETTERS ---
    public int getIdArticulo() { return idArticulo; }
    public String getNombre() { return nombre; }
    public int getStockActual() { return stockActual; }
    public void setStockActual(int stockActual) { this.stockActual = stockActual; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public String getNombreProveedor() { return nombreProveedor; }
}