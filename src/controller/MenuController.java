package controller;

import view.MenuPrincipal;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Controlador para el Menú Principal.
 * Gestiona los clics de la vista y decide qué base de datos/parking controlar.
 */
public class MenuController implements ActionListener {

    private MenuPrincipal vistaMenu;

    public MenuController(MenuPrincipal vistaMenu) {
        this.vistaMenu = vistaMenu;

        // Le decimos a la vista que este controlador escuchará sus botones
        this.vistaMenu.conectarListeners(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int idParkingSeleccionado = 0;
        String nombreParking = "";

        // 1. Identificamos qué botón ha levantado el evento
        if (e.getSource() == vistaMenu.getBtnParkingNorte()) {
            idParkingSeleccionado = 1;
            nombreParking = "Parking Los Panteras - Norte";
        } else if (e.getSource() == vistaMenu.getBtnParkingCentro()) {
            idParkingSeleccionado = 2;
            nombreParking = "Parking Los Panteras - Centro";
        } else if (e.getSource() == vistaMenu.getBtnParkingSur()) {
            idParkingSeleccionado = 3;
            nombreParking = "Parking Los Panteras - Sur";
        }

        // 2. Si se pulsó un botón válido, hacemos la magia de la transición
        if (idParkingSeleccionado != 0) {
            // Ocultamos el menú principal
            vistaMenu.setVisible(false);

            // Creamos la vista
            view.PanelParking panelControl = new view.PanelParking(idParkingSeleccionado, nombreParking);

            // El propio constructor de ParkingController se encargará de encender la pantalla
            new controller.ParkingController(panelControl, idParkingSeleccionado);
        }
    }
}