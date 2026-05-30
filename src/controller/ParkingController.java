package controller;

import model.*;
import view.PanelParking;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * Controlador para la gestión operativa de un parking concreto.
 * Maneja el flujo de vehículos, el inventario y el personal de forma secuencial.
 */
public class ParkingController implements ActionListener {

    private PanelParking vista;
    private EstanciaDAO estanciaDao;
    private ArticuloDAO articuloDao;
    private TrabajadorDAO trabajadorDao;
    private UsuarioDAO usuarioDao; // Cambiado por tu implementación real o UsuarioDAO si aplica
    private int idParking;

    public ParkingController(PanelParking vista, int idParking) {
        this.vista = vista;
        this.idParking = idParking;
        this.estanciaDao = new EstanciaDAO();
        this.articuloDao = new ArticuloDAO();
        this.trabajadorDao = new TrabajadorDAO();
        this.usuarioDao = new UsuarioDAO(); // Asegura la consistencia con tu objeto real

        // Configuración de los listeners de la vista
        this.vista.getBtnRegistrarEntrada().addActionListener(this);
        this.vista.getBtnRegistrarSalida().addActionListener(this);
        this.vista.getBtnRefrescarStock().addActionListener(this);
        this.vista.getBtnContratarLavadoExterior().addActionListener(this); // Escuchador lavado exterior
        this.vista.getBtnContratarLavadoPremium().addActionListener(this);  // Escuchador lavado premium
        this.vista.getBtnContratarAceite().addActionListener(this);

        this.vista.setVisible(true);

        try {
            refrescarPantallaInventario();
            refrescarPantallaPersonal();
        } catch (Exception ex) {
            System.err.println("Error al cargar los datos iniciales de las tablas.");
            ex.printStackTrace();
        }

        this.vista.getBtnEliminarEmpleado().addActionListener(e -> ejecutarBorradoEmpleado());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnRegistrarEntrada()) {
            gestionarEntrada();
        } else if (e.getSource() == vista.getBtnRegistrarSalida()) {
            gestionarSalida();
        } else if (e.getSource() == vista.getBtnRefrescarStock()) {
            refrescarPantallaInventario();
        } else if (e.getSource() == vista.getBtnContratarLavadoExterior()) {
            gestionarContratacionLavado("Exterior (3.00€)"); // Envía bandera de distinción
        } else if (e.getSource() == vista.getBtnContratarLavadoPremium()) {
            gestionarContratacionLavado("Completo (5.41€)");  // Envía bandera de distinción
        } else if (e.getSource() == vista.getBtnContratarAceite()) {
            gestionarContratacionAceite();
        }
    }

    private void gestionarEntrada() {
        String matricula = vista.getTxtMatricula().getText().trim().toUpperCase();

        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, introduce una matrícula válida.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usr = usuarioDao.buscarUsuarioPorDni(matricula);
        String tipoInterfaz = "Usuario Normal";
        int idClienteParaRegistrar = 1;

        if (usr != null) {
            tipoInterfaz = usr.getTipoUsuario().substring(0, 1).toUpperCase() + usr.getTipoUsuario().substring(1).toLowerCase();
            idClienteParaRegistrar = usr.getIdParkingAsignado();

            if (usr.getTipoUsuario().equalsIgnoreCase("abonado")) {
                int abonadosDentro = estanciaDao.contarAbonadosDentro(idParking);
                if (abonadosDentro >= 120) {
                    JOptionPane.showMessageDialog(vista,
                            "ZONA DE ABONADOS COMPLETA (120/120 plazas ocupadas).\n" +
                                    "Debe acceder y pagar como 'Usuario Normal'.",
                            "Límite Alcanzado", JOptionPane.WARNING_MESSAGE);
                    return;
                }
            }
        }

        Estancia nuevaEstancia = new Estancia(matricula, idParking, idClienteParaRegistrar);
        boolean exito = estanciaDao.registrarEntrada(nuevaEstancia);

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Vehículo [" + matricula + "] autorizado como: " + tipoInterfaz);
            vista.getTxtMatricula().setText("");
        } else {
            JOptionPane.showMessageDialog(vista, "Error al conectar con la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void gestionarSalida() {
        String matricula = vista.getTxtMatricula().getText().trim().toUpperCase();
        boolean tieneTicketCC = vista.getChkTicketCC().isSelected();
        boolean perdidaTarjeta = vista.getChkPerdidaTarjeta().isSelected();

        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, introduce la matrícula del coche.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Usuario usr = usuarioDao.buscarUsuarioPorDni(matricula);
        double importeBase = estanciaDao.registrarSalida(matricula, tieneTicketCC);

        if (importeBase == -1) {
            JOptionPane.showMessageDialog(vista, "No se ha encontrado ninguna estancia activa para: " + matricula, "Vehículo No Detectado", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String mensajeTicket = "";
        double totalACobrar = 0.0;

        if (usr != null && (usr.getTipoUsuario().equalsIgnoreCase("abonado") || usr.getTipoUsuario().equalsIgnoreCase("trabajador"))) {
            totalACobrar = 0.0;
            mensajeTicket = "=== CONTROL DE TARJETA ===\n" +
                    "Titular: " + matricula + "\n" +
                    "Tipo Cuenta: " + usr.getTipoUsuario().toUpperCase() + "\n" +
                    "-------------------------\n" +
                    "TOTAL ABONADO EN FACTURA: 0.00 €\n" +
                    "=========================";
        } else {
            if (perdidaTarjeta) {
                totalACobrar = 24.04;
                mensajeTicket = "=== TICKET POR EXTRAVÍO DE TARJETA ===\n" +
                        "Vehículo: " + matricula + "\n" +
                        "Motivo: Pérdida de resguardo de entrada\n" +
                        "-------------------------\n" +
                        "TOTAL MULTA A COBRAR: 24,04 €\n" +
                        "=========================";
            } else {
                double horasTranscurridas = (importeBase == 0) ? 1.5 : importeBase;

                if (tieneTicketCC) {
                    horasTranscurridas = Math.max(0, horasTranscurridas - 2.0);
                }

                if (horasTranscurridas <= 0) {
                    totalACobrar = 0.0;
                } else if (horasTranscurridas <= 0.5) {
                    totalACobrar = 0.90;
                } else if (horasTranscurridas <= 1.0) {
                    totalACobrar = 0.90 + 0.60;
                } else {
                    double tiempoRestante = horasTranscurridas - 1.0;
                    double fraccionesExtra = Math.ceil(tiempoRestante / 0.5);
                    totalACobrar = 1.50 + (fraccionesExtra * 0.90);
                }

                if (horasTranscurridas > 24.0) {
                    totalACobrar += 50.00;
                    mensajeTicket = "=== TICKET CON SANCIÓN (>24h) ===\n";
                } else {
                    mensajeTicket = "=== TICKET DE SALIDA ===\n";
                }

                mensajeTicket += "Vehículo: " + matricula + "\n" +
                        "Tiempo Tarificado: " + String.format("%.2f", horasTranscurridas) + " horas\n" +
                        "Descuento 2h CC: " + (tieneTicketCC ? "Aplicado (Sí)" : "No aportado") + "\n" +
                        "-------------------------\n" +
                        "TOTAL A COBRAR EN CAJA: " + String.format("%.2f", totalACobrar) + " €\n" +
                        "=========================";
            }
        }

        JOptionPane.showMessageDialog(vista, mensajeTicket, "Cierre de Estancia de Vehículo", JOptionPane.INFORMATION_MESSAGE);

        vista.getTxtMatricula().setText("");
        vista.getChkTicketCC().setSelected(false);
        vista.getChkPerdidaTarjeta().setSelected(false);
    }

    private void refrescarPantallaInventario() {
        List<Articulo> articulos = articuloDao.obtenerArticulosAlmacen();

        if (articulos == null || articulos.isEmpty()) {
            System.err.println("Aviso: No se recibieron datos de la base de datos.");
            return;
        }

        DefaultListModel<String> modeloLista = vista.getModeloListaAlmacen();
        modeloLista.clear();

        for (Articulo art : articulos) {
            double porc = art.getPorcentajeStock();
            String alerta = "";

            if (porc <= 7.0) {
                alerta = " [STOCK CRÍTICO < 7%]";
            }

            String lineaTexto = art.getNombre() + ": " + art.getStockActual() + "/" +
                    art.getCapacidadMaxima() + " uds. (Prov: " +
                    art.getNombreProveedor() + ")" + alerta;
            modeloLista.addElement(lineaTexto);
        }
    }

    private void gestionarContratacionLavado(String modalidad) {
        int idJabonReal = articuloDao.buscarIdPorNombre("Jabón");
        if (idJabonReal == -1) {
            idJabonReal = 2;
        }

        try {
            boolean esCritico = articuloDao.usarArticulo(idJabonReal);

            JOptionPane.showMessageDialog(vista, "Servicio de lavado " + modalidad + " registrado con éxito.\nSe ha añadido el importe a la cuenta del taller.", "Servicio Añadido", JOptionPane.INFORMATION_MESSAGE);

            if (esCritico) {
                JOptionPane.showMessageDialog(vista,
                        "ALERTA DE ALMACÉN:\nEl stock de consumibles de lavado ha bajado del 7%.\nPor favor, genera una orden de pedido al proveedor.",
                        "Aviso de Reposición Urgente",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (RuntimeException ex) {
            if (ex.getMessage().equals("SIN_STOCK")) {
                JOptionPane.showMessageDialog(vista,
                        "No se puede contratar el servicio.\nNo queda Jabón disponible en el almacén.\nRepón el inventario antes de continuar.",
                        "Error de Stock",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                ex.printStackTrace();
            }
        }

        refrescarPantallaInventario();
    }

    private void gestionarContratacionAceite() {
        int idAceiteReal = articuloDao.buscarIdPorNombre("Aceite");

        if (idAceiteReal == -1) {
            idAceiteReal = 1;
        }

        try {
            boolean esCritico = articuloDao.usarArticulo(idAceiteReal);

            JOptionPane.showMessageDialog(vista, "Servicio de cambio de aceite registrado con éxito.\nSe ha añadido el importe a la cuenta del taller.", "Servicio Añadido", JOptionPane.INFORMATION_MESSAGE);

            if (esCritico) {
                JOptionPane.showMessageDialog(vista,
                        "ALERTA DE ALMACÉN:\nEl stock de Garrafas de Aceite ha bajado del 7%.\nPor favor, genera una orden de pedido al proveedor.",
                        "Aviso de Reposición Urgente",
                        JOptionPane.WARNING_MESSAGE);
            }

        } catch (RuntimeException ex) {
            if (ex.getMessage().equals("SIN_STOCK")) {
                JOptionPane.showMessageDialog(vista,
                        "No se puede contratar el servicio.\nNo quedan Garrafas de Aceite disponibles en el almacén.\nRepón el inventario antes de continuar.",
                        "Error de Stock",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                ex.printStackTrace();
            }
        }
        refrescarPantallaInventario();
    }

    private void refrescarPantallaPersonal() {
        List<model.Trabajador> listaPersonal = trabajadorDao.obtenerPersonalPorParking(idParking);

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
    }

    private void ejecutarBorradoEmpleado() {
        JTable tabla = this.vista.getTablaPersonal();
        int filaSeleccionada = tabla.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this.vista,
                    "Por favor, selecciona un empleado de la tabla para darle de baja.",
                    "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String dni = (String) tabla.getValueAt(filaSeleccionada, 1);
        String nombre = (String) tabla.getValueAt(filaSeleccionada, 0);

        int confirmar = JOptionPane.showConfirmDialog(this.vista,
                "¿Estás seguro de que deseas eliminar permanentemente a " + nombre + "?",
                "Confirmar Baja", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirmar == JOptionPane.YES_OPTION) {
            TrabajadorDAO empleadoDao = new TrabajadorDAO();
            boolean exito = empleadoDao.eliminarEmpleado(dni);

            if (exito) {
                JOptionPane.showMessageDialog(this.vista, "Empleado eliminado correctamente de la plantilla.");
                refrescarPantallaPersonal();
            } else {
                JOptionPane.showMessageDialog(this.vista, "Error al intentar eliminar el registro.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}