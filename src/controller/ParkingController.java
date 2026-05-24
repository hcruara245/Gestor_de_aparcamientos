package controller;

import model.Estancia;
import view.PanelParking;
import model.EstanciaDAO;
import javax.swing.JOptionPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador para la gestión operativa de un parking concreto.
 */
public class ParkingController implements ActionListener {

    private PanelParking vista;
    private EstanciaDAO estanciaDao;
    private int idParking;

    public ParkingController(PanelParking vista, int idParking) {
        this.vista = vista;
        this.idParking = idParking;
        this.estanciaDao = new EstanciaDAO();

        // Conectamos los botones de la vista con este controlador
        this.vista.getBtnRegistrarEntrada().addActionListener(this);
        this.vista.getBtnRegistrarSalida().addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnRegistrarEntrada()) {
            gestionarEntrada();
        } else if (e.getSource() == vista.getBtnRegistrarSalida()) {
            gestionarSalida(); // <-- Conectamos el botón de salida aquí
        }
    }

    private void gestionarSalida() {
        String matricula = vista.getTxtMatricula().getText().trim().toUpperCase();
        boolean tieneTicketCC = vista.getChkTicketCC().isSelected(); // Leemos el estado del checkbox

        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, introduce la matrícula del coche que va a salir.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Llamamos al método del modelo que calcula el precio e inserta la salida
        double importe = estanciaDao.registrarSalida(matricula, tieneTicketCC);

        if (importe == -1) {
            JOptionPane.showMessageDialog(vista, "No se ha encontrado ninguna estancia activa para la matrícula: " + matricula, "Vehículo No Detectado", JOptionPane.ERROR_MESSAGE);
        } else {
            // Creamos un mensaje de desglose súper amigable para el operario
            String mensajeTicket = "=== TICKET DE SALIDA ===\n" +
                    "Vehículo: " + matricula + "\n" +
                    "Descuento 2h CC: " + (tieneTicketCC ? "Aplicado (Sí)" : "No aportado") + "\n" +
                    "-------------------------\n" +
                    "TOTAL A COBRAR: " + String.format("%.2f", importe) + " €\n" +
                    "=========================";

            JOptionPane.showMessageDialog(vista, mensajeTicket, "Cierre de Estancia", JOptionPane.INFORMATION_MESSAGE);

            // Limpiamos los campos para el siguiente cliente
            vista.getTxtMatricula().setText("");
            vista.getChkTicketCC().setSelected(false);
        }
    }

    private void gestionarEntrada() {
        String matricula = vista.getTxtMatricula().getText().trim().toUpperCase();
        String tipoUsuario = (String) vista.getComboTipoUsuario().getSelectedItem();

        // Validación intuitiva de cara al usuario
        if (matricula.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, introduce una matrícula válida.", "Campo Vacío", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Llamamos al modelo para meter los datos en el servidor remoto
        // Dentro de gestionarEntrada() en ParkingController:
        Estancia nuevaEstancia = new Estancia(matricula, idParking, 1);
        boolean exito = estanciaDao.registrarEntrada(nuevaEstancia);

        if (exito) {
            JOptionPane.showMessageDialog(vista, "Vehículo [" + matricula + "] registrado con éxito como " + tipoUsuario + ".");
            vista.getTxtMatricula().setText("");
        } else {
            // Cambiado el último parámetro por un tipo de mensaje nativo y válido
            JOptionPane.showMessageDialog(vista, "Hubo un error al conectar con el servidor para registrar la entrada.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}