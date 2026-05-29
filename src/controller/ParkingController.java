package controller;

import model.*;
import view.PanelParking;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador para la gestión operativa de un parking concreto.
 * Maneja tanto el flujo de vehículos como el inventario y el personal de forma optimizada.
 */
public class ParkingController implements ActionListener {

    private PanelParking vista;
    private EstanciaDAO estanciaDao;
    private ArticuloDAO articuloDao;
    private TrabajadorDAO trabajadorDao;
    private int idParking;

    public ParkingController(PanelParking vista, int idParking) {
        this.vista = vista;
        this.idParking = idParking;
        this.estanciaDao = new EstanciaDAO();
        this.articuloDao = new ArticuloDAO();
        this.trabajadorDao = new TrabajadorDAO();

        // --- CONEXIÓN DE LISTENERS ---
        this.vista.getBtnRegistrarEntrada().addActionListener(this);
        this.vista.getBtnRegistrarSalida().addActionListener(this);
        this.vista.getBtnRefrescarStock().addActionListener(this);
        this.vista.getBtnContratarLavado().addActionListener(this);

        // Abrimos la interfaz al milisegundo de forma instantánea
        this.vista.setVisible(true);

        // >>> OPTIMIZACIÓN DE RED: Hilos secundarios para cargar MySQL sin congelar la app <<<
        new Thread(() -> {
            try {
                refrescarPantallaInventario();
                refrescarPantallaPersonal();
            } catch (Exception ex) {
                System.err.println("Aviso: Fallo en la precarga de datos remotos.");
                ex.printStackTrace();
            }
        }).start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Pestaña 1: Accesos
        if (e.getSource() == vista.getBtnRegistrarEntrada()) {
            gestionarEntrada();
        } else if (e.getSource() == vista.getBtnRegistrarSalida()) {
            gestionarSalida();
        }
        // Pestaña 2: Inventario y Servicios
        else if (e.getSource() == vista.getBtnRefrescarStock()) {
            new Thread(this::refrescarPantallaInventario).start();
        } else if (e.getSource() == vista.getBtnContratarLavado()) {
            gestionarContratacionLavado();
        }
    }

    // ==========================================
    //       LÓGICA DEL MÓDULO DE ACCESOS
    // ==========================================

    private void gestionarEntrada() {
        String matricula = vista.getTxtMatricula().getText().trim().toUpperCase();
        String tipoUsuario = (String) vista.getComboTipoUsuario().getSelectedItem();

        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, introduce una matrícula válida.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Aplicamos POJO: Creamos el objeto molde "a pelo"
        Estancia nuevaEstancia = new Estancia(matricula, idParking, 1);
        boolean exito = estanciaDao.registrarEntrada(nuevaEstancia);

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Vehículo [" + matricula + "] registrado con éxito como " + tipoUsuario + ".");
            vista.getTxtMatricula().setText("");
        } else {
            JOptionPane.showMessageDialog(vista, "Hubo un error al conectar con el servidor para registrar la entrada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gestionarSalida() {
        String matricula = vista.getTxtMatricula().getText().trim().toUpperCase();
        boolean tieneTicketCC = vista.getChkTicketCC().isSelected();

        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, introduce la matrícula del coche que va a salir.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double importe = estanciaDao.registrarSalida(matricula, tieneTicketCC);

        if (importe == -1) {
            JOptionPane.showMessageDialog(vista, "No se ha encontrado ninguna estancia activa para la matrícula: " + matricula, "Vehículo No Detectado", JOptionPane.ERROR_MESSAGE);
        } else {
            String mensajeTicket = "=== TICKET DE SALIDA ===\n" +
                    "Vehículo: " + matricula + "\n" +
                    "Descuento 2h CC: " + (tieneTicketCC ? "Aplicado (Sí)" : "No aportado") + "\n" +
                    "-------------------------\n" +
                    "TOTAL A COBRAR: " + String.format("%.2f", importe) + " €\n" +
                    "=========================";

            JOptionPane.showMessageDialog(vista, mensajeTicket, "Cierre de Estancia", JOptionPane.INFORMATION_MESSAGE);
            vista.getTxtMatricula().setText("");
            vista.getChkTicketCC().setSelected(false);
        }
    }

    // ==========================================
    //      LÓGICA DEL MÓDULO DE INVENTARIO
    // ==========================================

    /**
     * Recupera los objetos Articulo del DAO y actualiza la JList sin parpadeos.
     */
    private void refrescarPantallaInventario() {
        List<Articulo> articulos = articuloDao.obtenerArticulosAlmacen();

        if (articulos == null || articulos.isEmpty()) {
            System.err.println("Aviso: No se recibieron datos del servidor, manteniendo vista previa.");
            return;
        }

        javax.swing.SwingUtilities.invokeLater(() -> {
            DefaultListModel<String> modeloLista = vista.getModeloListaAlmacen();
            modeloLista.clear();

            for (Articulo art : articulos) {
                double porc = art.getPorcentajeStock();
                String alerta = (porc <= 7.0) ? " ⚠️ [STOCK CRÍTICO < 7%]" : "";

                String lineaTexto = String.format("%s: %d/%d uds. (Prov: %s)%s",
                        art.getNombre(), art.getStockActual(), art.getCapacidadMaxima(), art.getNombreProveedor(), alerta);

                modeloLista.addElement(lineaTexto);
            }
        });
    }

    /**
     * Simula la contratación de un lavado buscando de forma dinámica el artículo correcto en la BBDD.
     */
    private void gestionarContratacionLavado() {
        int idJabonReal = articuloDao.buscarIdPorNombre("Jabón");

        if (idJabonReal == -1) {
            idJabonReal = 2;
        }

        boolean esCritico = articuloDao.usarArticulo(idJabonReal);

        JOptionPane.showMessageDialog(vista, "🧼 Lavado Premium registrado con éxito.\nSe ha añadido 15.00€ a la cuenta del taller.", "Servicio Añadido", JOptionPane.INFORMATION_MESSAGE);

        if (esCritico) {
            JOptionPane.showMessageDialog(vista,
                    "🚨 ¡ALERTA DE ALMACÉN!\nEl stock de consumibles de lavado ha bajado del 7%.\nPor favor, genera una orden de pedido al proveedor.",
                    "Aviso de Reposición Urgente",
                    JOptionPane.WARNING_MESSAGE);
        }

        new Thread(this::refrescarPantallaInventario).start();
    }

    // ==========================================
    //       LÓGICA DEL MÓDULO DE PERSONAL
    // ==========================================

    /**
     * Recupera los empleados asignados a este parking y rellena la JTable de forma limpia.
     */
    private void refrescarPantallaPersonal() {
        List<model.Trabajador> listaPersonal = trabajadorDao.obtenerPersonalPorParking(idParking);

        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.table.DefaultTableModel modeloTabla = vista.getModeloTablaPersonal();
            modeloTabla.setRowCount(0);

            for (model.Trabajador t : listaPersonal) {
                Object[] fila = new Object[]{
                        t.getNombreCompleto(),
                        t.getDni(),
                        t.getPuesto(),
                        t.getTurnoAsignado(),
                        t.getHorarioHoras()
                };
                modeloTabla.addRow(fila);
            }
        });
    }
}