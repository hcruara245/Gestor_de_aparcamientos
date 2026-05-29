package view;

import javax.swing.*;
import java.awt.*;

/**
 * Vista principal de gestión para un aparcamiento específico.
 * Utiliza pestañas para organizar de forma intuitiva los módulos del sistema.
 */
public class PanelParking extends JFrame {

    private int idParking;
    private String nombreParking;

    // Componentes del Módulo 1: Accesos y Estancias
    private JTextField txtMatricula;
    private JComboBox<String> comboTipoUsuario;
    private JButton btnRegistrarEntrada;
    private JButton btnRegistrarSalida;
    private JCheckBox chkTicketCC;
    private JList<String> listaAlmacen;
    private DefaultListModel<String> modeloListaAlmacen;
    private JButton btnContratarLavado;
    private JButton btnRefrescarStock;
    private JTable tablaPersonal;
    private javax.swing.table.DefaultTableModel modeloTablaPersonal;

    public PanelParking(int idParking, String nombreParking) {
        this.idParking = idParking;
        this.nombreParking = nombreParking;

        // Configuración de la ventana
        setTitle("Gestión Operativa - " + nombreParking);
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Solo cierra este panel, no toda la app
        setLocationRelativeTo(null);

        // Contenedor base
        JPanel panelBase = new JPanel(new BorderLayout());
        panelBase.setBackground(new Color(245, 247, 250));

        // 1. ENCABEZADO SUPERIOR INFO
        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(33, 43, 54));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTitulo = new JLabel("PANEL DE CONTROL: " + nombreParking.toUpperCase(), SwingConstants.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblInfoCadenas = new JLabel("CC Los Panteras | ID: " + idParking, SwingConstants.RIGHT);
        lblInfoCadenas.setFont(new Font("Arial", Font.PLAIN, 12));
        lblInfoCadenas.setForeground(new Color(145, 158, 171));

        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelHeader.add(lblInfoCadenas, BorderLayout.EAST);
        panelBase.add(panelHeader, BorderLayout.NORTH);

        // 2. SISTEMA DE PESTAÑAS CENTRAL (JTabbedPane)
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Arial", Font.BOLD, 13));

        // Añadimos las 3 secciones estructuradas
        pestañas.addTab("📥 Control de Accesos", crearPanelAccesos());
        pestañas.addTab("🔧 Servicios e Inventario", crearPanelServicios());
        pestañas.addTab("👥 Personal y Turnos", crearPanelPersonal());

        panelBase.add(pestañas, BorderLayout.CENTER);
        add(panelBase);
    }

    /**
     * Pestaña 1: Registro de Entradas y Salidas de vehículos.
     */
    private JPanel crearPanelAccesos() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Formulario de entrada
        JLabel lblMatricula = new JLabel("Matrícula del Vehículo:");
        lblMatricula.setFont(new Font("Arial", Font.BOLD, 13));
        txtMatricula = new JTextField(12);
        txtMatricula.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lblTipo = new JLabel("Tipo de Cliente:");
        comboTipoUsuario = new JComboBox<>(new String[]{"Usuario Normal", "Abonado", "Trabajador"});

        chkTicketCC = new JCheckBox("Presenta ticket de compra CC (2h gratis)");
        chkTicketCC.setOpaque(false);

        btnRegistrarEntrada = new JButton("Registrar Entrada");
        btnRegistrarSalida = new JButton("Registrar Salida y Cobrar");

        // Estilos rápidos a los botones de acción
        btnRegistrarEntrada.setBackground(new Color(34, 197, 94));
        btnRegistrarEntrada.setForeground(Color.WHITE);
        btnRegistrarSalida.setBackground(new Color(59, 130, 246));
        btnRegistrarSalida.setForeground(Color.WHITE);

        // Posicionar elementos en el Layout de forma intuitiva
        gbc.gridx = 0; gbc.gridy = 0; panel.add(lblMatricula, gbc);
        gbc.gridx = 1; panel.add(txtMatricula, gbc);

        gbc.gridx = 0; gbc.gridy = 1; panel.add(lblTipo, gbc);
        gbc.gridx = 1; panel.add(comboTipoUsuario, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panel.add(chkTicketCC, gbc);

        gbc.gridy = 3; gbc.gridwidth = 1; gbc.gridx = 0; panel.add(btnRegistrarEntrada, gbc);
        gbc.gridx = 1; panel.add(btnRegistrarSalida, gbc);

        return panel;
    }

    /**
     * Pestaña 3: Vista de la plantilla del personal asignado al parking.
     */
    private JPanel crearPanelPersonal() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título de la sección
        JLabel lblTituloSeccion = new JLabel("Cuadrante de Personal Activo en esta Sucursal", SwingConstants.LEFT);
        lblTituloSeccion.setFont(new Font("Arial", Font.BOLD, 14));
        lblTituloSeccion.setForeground(new Color(51, 65, 85));
        panel.add(lblTituloSeccion, BorderLayout.NORTH);

        // Definimos las columnas de la tabla cuadrante
        String[] columnas = {"Nombre Completo", "DNI", "Puesto / Rol", "Turno", "Horario Activo"};

        // Creamos el modelo de la tabla bloqueando la edición directa de las celdas
        modeloTablaPersonal = new javax.swing.table.DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No modificable desde la interfaz directamente
            }
        };

        // Inicializamos la JTable con su modelo
        tablaPersonal = new JTable(modeloTablaPersonal);
        tablaPersonal.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaPersonal.setRowHeight(25); // Espacio generoso para cada fila
        tablaPersonal.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaPersonal.getTableHeader().setReorderingAllowed(false); // Bloquear mover columnas

        // Metemos la tabla dentro de un JScrollPane para que tenga scroll si se llena
        JScrollPane scrollTabla = new JScrollPane(tablaPersonal);
        panel.add(scrollTabla, BorderLayout.CENTER);

        return panel;
    }

    // Getter para que el controlador pueda rellenar los datos de la tabla
    public javax.swing.table.DefaultTableModel getModeloTablaPersonal() { return modeloTablaPersonal; }

    /**
     * Pestaña 2: Contratación de servicios y alertas automáticas de inventario.
     */
    private JPanel crearPanelServicios() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // --- ZONA IZQUIERDA: CONTRATACIÓN DE SERVICIOS ADICIONALES ---
        JPanel panelAcciones = new JPanel(new GridLayout(3, 1, 0, 15));
        panelAcciones.setOpaque(false);
        panelAcciones.setBorder(BorderFactory.createTitledBorder("Servicios Disponibles"));

        btnContratarLavado = new JButton("🧼 Contratar Lavado Premium (15.00€)");
        btnContratarLavado.setFont(new Font("Arial", Font.BOLD, 12));
        btnContratarLavado.setBackground(new Color(243, 244, 246));

        JButton btnContratarAceite = new JButton("🔧 Cambio de Aceite (45.00€)");
        btnContratarAceite.setFont(new Font("Arial", Font.BOLD, 12));
        btnContratarAceite.setBackground(new Color(243, 244, 246));
        btnContratarAceite.setEnabled(false); // De momento desactivado por pruebas

        panelAcciones.add(btnContratarLavado);
        panelAcciones.add(btnContratarAceite);
        panel.add(panelAcciones, BorderLayout.WEST);

        // --- ZONA DERECHA: MONITOR DE INVENTARIO (ALERTAS < 7%) ---
        JPanel panelInventario = new JPanel(new BorderLayout(10, 10));
        panelInventario.setOpaque(false);
        panelInventario.setBorder(BorderFactory.createTitledBorder("Monitor de Stock Remoto"));

        modeloListaAlmacen = new DefaultListModel<>();
        listaAlmacen = new JList<>(modeloListaAlmacen);
        listaAlmacen.setFont(new Font("Consolas", Font.PLAIN, 12)); // Letra monoespaciada tipo terminal
        JScrollPane scrollLista = new JScrollPane(listaAlmacen);

        btnRefrescarStock = new JButton("🔄 Actualizar Inventario");
        btnRefrescarStock.setBackground(new Color(99, 102, 241));
        btnRefrescarStock.setForeground(Color.WHITE);

        panelInventario.add(scrollLista, BorderLayout.CENTER);
        panelInventario.add(btnRefrescarStock, BorderLayout.SOUTH);

        panel.add(panelInventario, BorderLayout.CENTER);

        return panel;
    }

    // Getters necesarios para el controlador
    public JButton getBtnContratarLavado() { return btnContratarLavado; }
    public JButton getBtnRefrescarStock() { return btnRefrescarStock; }
    public DefaultListModel<String> getModeloListaAlmacen() { return modeloListaAlmacen; }

    public JTextField getTxtMatricula() { return txtMatricula; }
    public JComboBox<String> getComboTipoUsuario() { return comboTipoUsuario; }
    public JButton getBtnRegistrarEntrada() { return btnRegistrarEntrada; }
    public JButton getBtnRegistrarSalida() { return btnRegistrarSalida; }
    public JCheckBox getChkTicketCC() { return chkTicketCC; }
}