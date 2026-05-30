package controller;

import view.MenuPrincipal;
import view.PanelParking;
import view.PanelAdmin; // Importamos el nuevo panel de administración

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuController implements ActionListener {

    private MenuPrincipal vistaMenu;

    public MenuController(MenuPrincipal vistaMenu) {
        this.vistaMenu = vistaMenu;
        this.vistaMenu.conectarListeners(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int idParkingSeleccionado = 0;
        String nombreParking = "";

        // Comprobamos qué sucursal ha seleccionado el operario
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

        // Si es un parking válido, ocultamos el menú y abrimos el panel operativo
        if (idParkingSeleccionado != 0) {
            vistaMenu.setVisible(false);

            // Los imports de arriba limpian el código de rutas largas
            PanelParking panelControl = new PanelParking(idParkingSeleccionado, nombreParking);
            new ParkingController(panelControl, idParkingSeleccionado);
        }
    }
}