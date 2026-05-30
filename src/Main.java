import javax.swing.SwingUtilities;

import controller.AdminController;
import view.MenuPrincipal;
import controller.MenuController; // Importamos el nuevo controlador
import view.PanelAdmin;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MenuPrincipal ventanaMenu = new MenuPrincipal();

                // Creamos el controlador y le pasamos la vista para que la maneje
                MenuController controlador = new MenuController(ventanaMenu);

                ventanaMenu.setVisible(true);
            }
        });
        PanelAdmin vistaAdmin = new PanelAdmin();
        new AdminController(vistaAdmin);
    }
}