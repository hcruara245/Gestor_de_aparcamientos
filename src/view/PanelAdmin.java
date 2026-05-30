package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class PanelAdmin extends JFrame {

    private JTextField txtDniCliente, txtNombreCliente, txtTarjeta, txtCuota, txtPlaza;
    private JComboBox<String> comboTipoCliente, comboParkingCliente;
    private JButton btnRegistrarCliente;

    private JTextField txtDniEmp, txtNombreEmp, txtApellidosEmp, txtSalario;
    private JComboBox<String> comboRolEmp, comboParkingEmp;
    private JButton btnRegistrarEmpleado;

    public PanelAdmin() {
        setTitle("Módulo de Administración Central - Los Panteras");
        setSize(620, 550); // Un pelín más de alto para que no se amontone abajo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Contenedor principal con el fondo gris azulado de la app
        JPanel panelBase = new JPanel(new BorderLayout());
        panelBase.setBackground(new Color(240, 244, 248));

        // Cabecera oscura corporativa
        JPanel panelHeader = new JPanel(new GridLayout(1, 1));
        panelHeader.setBackground(new Color(30, 41, 59));
        panelHeader.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel lblTituloVentana = new JLabel("ALTA DE NUEVOS REGISTROS", SwingConstants.LEFT);
        lblTituloVentana.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTituloVentana.setForeground(Color.WHITE);
        panelHeader.add(lblTituloVentana);
        panelBase.add(panelHeader, BorderLayout.NORTH);

        // Panel de pestañas
        JTabbedPane pestañas = new JTabbedPane();
        pestañas.setFont(new Font("Segoe UI", Font.BOLD, 13));
        pestañas.addTab("Registrar Cliente", crearPanelClientes());
        pestañas.addTab("Registrar Empleado", crearPanelEmpleados());

        // Manejo de los campos según la selección del combo de usuarios
        comboTipoCliente.addActionListener(e -> {
            String tipo = (String) comboTipoCliente.getSelectedItem();

            if (tipo.equals("normal")) {
                txtCuota.setText("0.00");
                txtCuota.setEnabled(false);
                txtPlaza.setText("");
                txtPlaza.setEnabled(false);
            } else if (tipo.equals("abonado")) {
                txtCuota.setText("120.20");
                txtCuota.setEnabled(false);
                txtPlaza.setEnabled(true);
            } else if (tipo.equals("trabajador")) {
                txtCuota.setText("0.00");
                txtCuota.setEnabled(false);
                txtPlaza.setText("");
                txtPlaza.setEnabled(false);
            }
        });

        // Estado por defecto al cargar (empieza en usuario normal)
        txtCuota.setText("0.00");
        txtCuota.setEnabled(false);
        txtPlaza.setEnabled(false);

        panelBase.add(pestañas, BorderLayout.CENTER);
        add(panelBase);
    }

    private JPanel crearPanelClientes() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabels = new Font("Segoe UI", Font.BOLD, 13);

        txtDniCliente = new JTextField(15);
        txtNombreCliente = new JTextField(15);
        txtTarjeta = new JTextField(15);
        txtCuota = new JTextField("120.20");
        txtPlaza = new JTextField(15);

        comboTipoCliente = new JComboBox<>(new String[]{"normal", "abonado", "trabajador"});
        comboParkingCliente = new JComboBox<>(new String[]{"1", "2", "3"});

        btnRegistrarCliente = new JButton("Guardar Cliente en la Base de Datos");
        btnRegistrarCliente.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegistrarCliente.setBackground(new Color(34, 197, 94)); // Mismo verde que el panel de accesos
        btnRegistrarCliente.setForeground(Color.WHITE);
        btnRegistrarCliente.setFocusPainted(false);
        btnRegistrarCliente.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Maquetación del formulario
        colocarComp(panel, new JLabel("DNI / Matrícula:"), fontLabels, gbc, 0, 0);
        colocarComp(panel, txtDniCliente, gbc, 1, 0);
        colocarComp(panel, new JLabel("Nombre Completo:"), fontLabels, gbc, 0, 1);
        colocarComp(panel, txtNombreCliente, gbc, 1, 1);
        colocarComp(panel, new JLabel("Tipo Usuario:"), fontLabels, gbc, 0, 2);
        colocarComp(panel, comboTipoCliente, gbc, 1, 2);
        colocarComp(panel, new JLabel("ID Parking Asignado:"), fontLabels, gbc, 0, 3);
        colocarComp(panel, comboParkingCliente, gbc, 1, 3);
        colocarComp(panel, new JLabel("Cuota Mensual (€):"), fontLabels, gbc, 0, 4);
        colocarComp(panel, txtCuota, gbc, 1, 4);
        colocarComp(panel, new JLabel("Nº Plaza Reservada:"), fontLabels, gbc, 0, 5);
        colocarComp(panel, txtPlaza, gbc, 1, 5);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(btnRegistrarCliente, gbc);

        return panel;
    }

    private JPanel crearPanelEmpleados() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font fontLabels = new Font("Segoe UI", Font.BOLD, 13);

        txtDniEmp = new JTextField(15);
        txtNombreEmp = new JTextField(15);
        txtApellidosEmp = new JTextField(15);
        txtSalario = new JTextField("5.41");

        comboRolEmp = new JComboBox<>(new String[]{"guardia", "empleado"});
        comboParkingEmp = new JComboBox<>(new String[]{"1", "2", "3"});

        btnRegistrarEmpleado = new JButton("Confirmar Alta de Empleado");
        btnRegistrarEmpleado.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnRegistrarEmpleado.setBackground(new Color(37, 99, 235)); // Mismo azul que el panel de accesos
        btnRegistrarEmpleado.setForeground(Color.WHITE);
        btnRegistrarEmpleado.setFocusPainted(false);
        btnRegistrarEmpleado.setCursor(new Cursor(Cursor.HAND_CURSOR));

        colocarComp(panel, new JLabel("DNI Empleado:"), fontLabels, gbc, 0, 0);
        colocarComp(panel, txtDniEmp, gbc, 1, 0);
        colocarComp(panel, new JLabel("Nombre:"), fontLabels, gbc, 0, 1);
        colocarComp(panel, txtNombreEmp, gbc, 1, 1);
        colocarComp(panel, new JLabel("Apellidos:"), fontLabels, gbc, 0, 2);
        colocarComp(panel, txtApellidosEmp, gbc, 1, 2);
        colocarComp(panel, new JLabel("Rol / Puesto:"), fontLabels, gbc, 0, 3);
        colocarComp(panel, comboRolEmp, gbc, 1, 3);
        colocarComp(panel, new JLabel("Salario Hora (€):"), fontLabels, gbc, 0, 4);
        colocarComp(panel, txtSalario, gbc, 1, 4);
        colocarComp(panel, new JLabel("Asignar a Sucursal:"), fontLabels, gbc, 0, 5);
        colocarComp(panel, comboParkingEmp, gbc, 1, 5);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 10, 10);
        panel.add(btnRegistrarEmpleado, gbc);

        return panel;
    }

    private void colocarComp(JPanel p, Component c, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x; gbc.gridy = y;
        p.add(c, gbc);
    }

    private void colocarComp(JPanel p, JLabel label, Font f, GridBagConstraints gbc, int x, int y) {
        label.setFont(f);
        gbc.gridx = x; gbc.gridy = y;
        p.add(label, gbc);
    }

    public void conectarListeners(ActionListener al) {
        btnRegistrarCliente.addActionListener(al);
        btnRegistrarEmpleado.addActionListener(al);
    }

    public JButton getBtnRegistrarCliente() { return btnRegistrarCliente; }
    public JButton getBtnRegistrarEmpleado() { return btnRegistrarEmpleado; }

    public String getDniCliente() { return txtDniCliente.getText().trim(); }
    public String getNombreCliente() { return txtNombreCliente.getText().trim(); }
    public String getTipoCliente() { return (String) comboTipoCliente.getSelectedItem(); }
    public int getParkingCliente() { return Integer.parseInt((String) comboParkingCliente.getSelectedItem()); }
    public double getCuotaCliente() { return Double.parseDouble(txtCuota.getText()); }
    public int getPlazaCliente() { return txtPlaza.getText().isEmpty() ? 0 : Integer.parseInt(txtPlaza.getText()); }

    public String getDniEmp() { return txtDniEmp.getText().trim(); }
    public String getNombreEmp() { return txtNombreEmp.getText().trim(); }
    public String getApellidosEmp() { return txtApellidosEmp.getText().trim(); }
    public String getRolEmp() { return (String) comboRolEmp.getSelectedItem(); }
    public double getSalarioEmp() { return Double.parseDouble(txtSalario.getText()); }
    public int getParkingEmp() { return Integer.parseInt((String) comboParkingEmp.getSelectedItem()); }
}