package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MenuPrincipal extends JFrame {

    private JButton btnParkingNorte;
    private JButton btnParkingCentro;
    private JButton btnParkingSur;

    public MenuPrincipal() {
        setTitle("Sistema Gestor de Aparcamientos - Los Panteras");
        setSize(520, 380); // Un pelín más de aire para el diseño moderno
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Contenedor base
        JPanel panelPrincipal = new JPanel(new BorderLayout(0, 25));
        panelPrincipal.setBackground(new Color(240, 244, 248)); // El mismo gris azulado de las otras pantallas

        // Cabecera superior con la identidad del grupo
        JPanel panelHeader = new JPanel(new GridLayout(2, 1, 0, 4));
        panelHeader.setBackground(new Color(30, 41, 59)); // Azul marino oscuro corporativo
        panelHeader.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitulo = new JLabel("ACCESO AL SISTEMA OPERATIVO", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitulo.setForeground(Color.WHITE);

        JLabel lblSubtitulo = new JLabel("Selecciona la sucursal para iniciar la jornada", SwingConstants.CENTER);
        lblSubtitulo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSubtitulo.setForeground(new Color(148, 163, 184));

        panelHeader.add(lblTitulo);
        panelHeader.add(lblSubtitulo);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        // Cuerpo central con los botones de selección
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 15));
        panelBotones.setOpaque(false);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(0, 35, 25, 35)); // Margen lateral para que los botones no peguen al borde

        btnParkingNorte = new JButton("Aparcamiento Norte (Sucursal A)");
        btnParkingCentro = new JButton("Aparcamiento Centro (Sucursal B)");
        btnParkingSur = new JButton("Aparcamiento Sur (Sucursal C)");

        configurarBoton(btnParkingNorte);
        configurarBoton(btnParkingCentro);
        configurarBoton(btnParkingSur);

        panelBotones.add(btnParkingNorte);
        panelBotones.add(btnParkingCentro);
        panelBotones.add(btnParkingSur);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        add(panelPrincipal);
    }

    /**
     * Aplica los estilos modernos del dashboard a los botones de entrada.
     */
    private void configurarBoton(JButton boton) {
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setFocusPainted(false);
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(51, 65, 85));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Efecto click manual

        // Borde estilizado con padding interno para el texto
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(226, 232, 240), 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
    }

    public void conectarListeners(ActionListener listener) {
        btnParkingNorte.addActionListener(listener);
        btnParkingCentro.addActionListener(listener);
        btnParkingSur.addActionListener(listener);
    }

    // Getters para el MenuController
    public JButton getBtnParkingNorte() { return btnParkingNorte; }
    public JButton getBtnParkingCentro() { return btnParkingCentro; }
    public JButton getBtnParkingSur() { return btnParkingSur; }
}