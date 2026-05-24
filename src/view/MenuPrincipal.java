package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Vista del Menú Principal.
 * Permite seleccionar el aparcamiento con el que se va a trabajar.
 */
public class MenuPrincipal extends JFrame {

    // Botones para cada uno de los 3 parkings oficiales
    private JButton btnParkingNorte;
    private JButton btnParkingCentro;
    private JButton btnParkingSur;

    public MenuPrincipal() {
        // Configuración básica de la ventana
        setTitle("Sistema Gestor de Aparcamientos - Los Panteras");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Centra la ventana en la pantalla
        setResizable(false);

        // Contenedor principal con un diseño limpio y márgenes
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 20));
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panelPrincipal.setBackground(new Color(245, 247, 250)); // Fondo gris azulado suave

        // 1. TÍTULO SUPERIOR (Bienvenida)
        JLabel lblTitulo = new JLabel("¿Con qué aparcamiento deseas trabajar hoy?", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 43, 54));
        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // 2. CUERPO CENTRAL (Botones de los Parkings)
        JPanel panelBotones = new JPanel(new GridLayout(3, 1, 0, 15));
        panelBotones.setOpaque(false);

        // Estilo común para los botones (fácil de leer y moderno)
        Font fontBotones = new Font("Arial", Font.PLAIN, 14);

        btnParkingNorte = new JButton("Aparcamiento Norte (Sucursal A)");
        btnParkingCentro = new JButton("Aparcamiento Centro (Sucursal B)");
        btnParkingSur = new JButton("Aparcamiento Sur (Sucursal C)");

        // Aplicamos estilos básicos
        configurarBoton(btnParkingNorte, fontBotones);
        configurarBoton(btnParkingCentro, fontBotones);
        configurarBoton(btnParkingSur, fontBotones);

        panelBotones.add(btnParkingNorte);
        panelBotones.add(btnParkingCentro);
        panelBotones.add(btnParkingSur);

        panelPrincipal.add(panelBotones, BorderLayout.CENTER);

        // Añadimos el panel estructurado al marco de la ventana
        add(panelPrincipal);
    }

    /**
     * Aplica un diseño homogéneo e intuitivo a los botones.
     */
    private void configurarBoton(JButton boton, Font fuente) {
        boton.setFont(fuente);
        boton.setFocusPainted(false);
        boton.setBackground(Color.WHITE);
        boton.setForeground(new Color(51, 65, 85));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(203, 213, 225), 1),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
    }

    // Métodos para que el Controlador pueda escuchar los clics de forma limpia
    public void conectarListeners(ActionListener listener) {
        btnParkingNorte.addActionListener(listener);
        btnParkingCentro.addActionListener(listener);
        btnParkingSur.addActionListener(listener);
    }

    // Getters para identificar qué botón se ha pulsado
    public JButton getBtnParkingNorte() { return btnParkingNorte; }
    public JButton getBtnParkingCentro() { return btnParkingCentro; }
    public JButton getBtnParkingSur() { return btnParkingSur; }
}