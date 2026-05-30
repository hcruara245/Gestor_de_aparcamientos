package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelParking extends JFrame {

    private int idParking;
    private String nombreParking;

    // Componentes del Módulo de Accesos
    private JTextField txtMatricula;
    private JButton btnRegistrarEntrada;
    private JButton btnRegistrarSalida;
    private JCheckBox chkTicketCC;
    private JCheckBox chkPerdidaTarjeta;
    private JButton btnEliminarEmpleado;

    // Componentes del Módulo de Almacén y Servicios
    private JList<String> listaAlmacen;
    private DefaultListModel<String> modeloListaAlmacen;
    private JButton btnContratarLavadoExterior; // Nuevo servicio básico
    private JButton btnContratarLavadoPremium;  // El de 5.41€
    private JButton btnContratarAceite;
    private JButton btnRefrescarStock;

    // Componentes del Módulo de Personal
    private JTable tablaPersonal;
    private javax.swing.table.DefaultTableModel modeloTablaPersonal;


    public PanelParking(int idParking, String nombreParking) {
        this.idParking = idParking;
        this.nombreParking = nombreParking;

        setTitle("Gestión Operativa - " + nombreParking);
        setSize(850, 620);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panelBase = new JPanel(new BorderLayout());
        panelBase.setBackground(new Color(240, 244, 248));

        // Encabezado superior
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(30, 41, 59));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(18, 25, 18, 25));

        JLabel lblTitulo = new JLabel("PANEL DE CONTROL: " + nombreParking.toUpperCase(), SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblInfoCadenas = new JLabel("CC Los Panteras | ID: " + idParking, SwingConstants.RIGHT);
        lblInfoCadenas.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblInfoCadenas.setForeground(new Color(148, 163, 184));

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(lblInfoCadenas, BorderLayout.EAST);
        panelBase.add(panelHeader, BorderLayout.NORTH);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pestañas.setBackground(Color.WHITE);

        pestañas.addTab("Control de Accesos", crearPanelAccesos());
        pestañas.addTab("Servicios e Inventario", crearPanelServicios());
        pestañas.addTab("Personal y Turnos", crearPanelPersonal());

        panelBase.add(pestañas, BorderLayout.CENTER);
        add(panelBase);
    }

    private JPanel crearPanelAccesos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblMatricula = new JLabel("Matrícula / DNI:");
        lblMatricula.setFont(new Font("Segoe UI", Font.BOLD, 14));

        txtMatricula = new JTextField(15);
        txtMatricula.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        txtMatricula.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));

        chkTicketCC = new JCheckBox("Presenta ticket de compra CC (2h gratis)");
        chkTicketCC.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        chkTicketCC.setOpaque(false);

        chkPerdidaTarjeta = new JCheckBox("El usuario ha perdido la tarjeta (Multa 24,04€)");
        chkPerdidaTarjeta.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chkPerdidaTarjeta.setForeground(new Color(239, 68, 68));
        chkPerdidaTarjeta.setOpaque(false);

        btnRegistrarEntrada = new JButton("Registrar Entrada");
        btnRegistrarEntrada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrarEntrada.setBackground(new Color(34, 197, 94));
        btnRegistrarEntrada.setForeground(Color.WHITE);
        btnRegistrarEntrada.setFocusPainted(false);
        btnRegistrarEntrada.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnRegistrarSalida = new JButton("Registrar Salida y Cobrar");
        btnRegistrarSalida.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRegistrarSalida.setBackground(new Color(37, 99, 235));
        btnRegistrarSalida.setForeground(Color.WHITE);
        btnRegistrarSalida.setFocusPainted(false);
        btnRegistrarSalida.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblMatricula, gbc);
        gbc.gridx = 1; panel.add(txtMatricula, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 2; panel.add(chkTicketCC, gbc);
        gbc.gridy = 2; panel.add(chkPerdidaTarjeta, gbc);

        gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridx = 0; panel.add(btnRegistrarEntrada, gbc);
        gbc.gridx = 1; panel.add(btnRegistrarSalida, gbc);

        return panel;
    }

    private JPanel crearPanelServicios() {
        JPanel panel = new JPanel(new BorderLayout(25, 25));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        // Paneles de acciones (Izquierda) - Ahora con 3 filas para albergar ambos lavados y el aceite
        JPanel panelAcciones = new JPanel(new GridLayout(3, 1, 0, 15));
        panelAcciones.setOpaque(false);

        TitledBorder bordeServicios = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1), "Servicios Disponibles"
        );
        bordeServicios.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        panelAcciones.setBorder(bordeServicios);

        // Instanciación de los 3 botones oficiales exigidos
        btnContratarLavadoExterior = new JButton("Contratar Lavado Exterior (3.00€)");
        btnContratarLavadoExterior.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnContratarLavadoExterior.setBackground(new Color(248, 250, 252));
        btnContratarLavadoExterior.setFocusPainted(false);
        btnContratarLavadoExterior.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnContratarLavadoPremium = new JButton("Contratar Lavado Completo (5.41€)");
        btnContratarLavadoPremium.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnContratarLavadoPremium.setBackground(new Color(248, 250, 252));
        btnContratarLavadoPremium.setFocusPainted(false);
        btnContratarLavadoPremium.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btnContratarAceite = new JButton("Cambio de Aceite (21.04€)");
        btnContratarAceite.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        btnContratarAceite.setBackground(new Color(248, 250, 252));
        btnContratarAceite.setFocusPainted(false);
        btnContratarAceite.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Los agregamos secuencialmente al layout de la izquierda
        panelAcciones.add(btnContratarLavadoExterior);
        panelAcciones.add(btnContratarLavadoPremium);
        panelAcciones.add(btnContratarAceite);
        panel.add(panelAcciones, BorderLayout.WEST);

        // Monitor de Almacén (Centro/Derecha)
        JPanel panelInventario = new JPanel(new BorderLayout(12, 12));
        panelInventario.setOpaque(false);

        TitledBorder bordeAlmacen = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1), "Monitor de Stock"
        );
        bordeAlmacen.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        panelInventario.setBorder(bordeAlmacen);

        modeloListaAlmacen = new DefaultListModel<>();
        listaAlmacen = new JList<>(modeloListaAlmacen);
        listaAlmacen.setFont(new Font("Consolas", Font.PLAIN, 13));
        listaAlmacen.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane scrollLista = new JScrollPane(listaAlmacen);

        btnRefrescarStock = new JButton("Actualizar Inventario");
        btnRefrescarStock.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRefrescarStock.setBackground(new Color(79, 70, 229));
        btnRefrescarStock.setForeground(Color.WHITE);
        btnRefrescarStock.setFocusPainted(false);
        btnRefrescarStock.setCursor(new Cursor(Cursor.HAND_CURSOR));

        panelInventario.add(scrollLista, BorderLayout.CENTER);
        panelInventario.add(btnRefrescarStock, BorderLayout.SOUTH);

        panel.add(panelInventario, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelPersonal() {
        JPanel panel = new JPanel(new BorderLayout(20, 20));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        JLabel lblTituloSeccion = new JLabel("Cuadrante de Personal Activo en esta Sucursal", SwingConstants.LEFT);
        lblTituloSeccion.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloSeccion.setForeground(new Color(51, 65, 85));
        panel.add(lblTituloSeccion, BorderLayout.NORTH);

        String[] columnas = {"Nombre Completo", "DNI", "Puesto / Rol", "Turno", "Horario Activo"};

        modeloTablaPersonal = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaPersonal = new JTable(modeloTablaPersonal);
        tablaPersonal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaPersonal.setRowHeight(28);
        tablaPersonal.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tablaPersonal.getTableHeader().setReorderingAllowed(false);
        tablaPersonal.setGridColor(new Color(241, 245, 249));

        JScrollPane scrollTabla = new JScrollPane(tablaPersonal);
        panel.add(scrollTabla, BorderLayout.CENTER);

        btnEliminarEmpleado = new JButton("Dar de Baja Empleado Seleccionado");
        btnEliminarEmpleado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEliminarEmpleado.setBackground(new Color(239, 68, 68));
        btnEliminarEmpleado.setForeground(Color.WHITE);
        btnEliminarEmpleado.setFocusPainted(false);
        btnEliminarEmpleado.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelInferior.setOpaque(false);
        panelInferior.add(btnEliminarEmpleado);

        panel.add(panelInferior, BorderLayout.SOUTH);

        return panel;
    }

    // Getters actualizados
    public javax.swing.table.DefaultTableModel getModeloTablaPersonal() { return modeloTablaPersonal; }
    public DefaultListModel<String> getModeloListaAlmacen() { return modeloListaAlmacen; }
    public JTextField getTxtMatricula() { return txtMatricula; }
    public JCheckBox getChkTicketCC() { return chkTicketCC; }
    public JCheckBox getChkPerdidaTarjeta() { return chkPerdidaTarjeta; }
    public JButton getBtnRegistrarEntrada() { return btnRegistrarEntrada; }
    public JButton getBtnRegistrarSalida() { return btnRegistrarSalida; }
    public JButton getBtnContratarLavadoExterior() { return btnContratarLavadoExterior; } // Nuevo
    public JButton getBtnContratarLavadoPremium() { return btnContratarLavadoPremium; }   // Nuevo
    public JButton getBtnContratarAceite() { return btnContratarAceite; }
    public JButton getBtnRefrescarStock() { return btnRefrescarStock; }
    public JButton getBtnEliminarEmpleado() { return btnEliminarEmpleado; }
    public JTable getTablaPersonal() { return tablaPersonal; }
}