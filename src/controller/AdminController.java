package controller;

import model.ConexionBD;
import view.PanelAdmin;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminController implements ActionListener {

    private PanelAdmin vista;

    public AdminController(PanelAdmin vista) {
        this.vista = vista;
        this.vista.conectarListeners(this);
        this.vista.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == vista.getBtnRegistrarCliente()) {
            ejecutarAltaCliente();
        } else if (e.getSource() == vista.getBtnRegistrarEmpleado()) {
            ejecutarAltaEmpleado();
        }
    }

    private void ejecutarAltaCliente() {
        String sql = "INSERT INTO Clientes (tipo_usuario, nombre, dni, tarjeta_abonado, cuota_mensual, id_parking_asignado, n_plaza_asignada) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vista.getTipoCliente());
            ps.setString(2, vista.getNombreCliente());
            ps.setString(3, vista.getDniCliente());

            // Si es un cliente normal, los campos de abonos van vacíos a la BBDD
            if (vista.getTipoCliente().equals("normal")) {
                ps.setNull(4, java.sql.Types.VARCHAR);
                ps.setNull(5, java.sql.Types.DECIMAL);
                ps.setNull(6, java.sql.Types.INTEGER);
                ps.setNull(7, java.sql.Types.INTEGER);
            } else {
                // Controlamos que el DNI tenga texto suficiente para no romper el substring
                String dni = vista.getDniCliente();
                String codigoTarjeta = (dni.length() >= 4) ? "TARJ-" + dni.substring(0, 4) : "TARJ-" + dni;

                ps.setString(4, codigoTarjeta);
                ps.setDouble(5, vista.getCuotaCliente());
                ps.setInt(6, vista.getParkingCliente());
                ps.setInt(7, vista.getPlazaCliente());
            }

            ps.executeUpdate();
            JOptionPane.showMessageDialog(vista, "Cliente guardado correctamente en la base de datos.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException | NumberFormatException ex) {
            JOptionPane.showMessageDialog(vista, "Error al insertar el cliente: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void ejecutarAltaEmpleado() {
        String sql = "INSERT INTO Empleados (dni, nombre, apellidos, rol, jornada_semanal, salario_hora, id_parking) VALUES (?, ?, ?, ?, 40, ?, ?)";

        try (Connection con = ConexionBD.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, vista.getDniEmp());
            ps.setString(2, vista.getNombreEmp());
            ps.setString(3, vista.getApellidosEmp());
            ps.setString(4, vista.getRolEmp());
            ps.setDouble(5, vista.getSalarioEmp());
            ps.setInt(6, vista.getParkingEmp());

            ps.executeUpdate();
            JOptionPane.showMessageDialog(vista, "Empleado registrado y asignado a la sucursal.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "Error al insertar el empleado: " + ex.getMessage(), "Error SQL", JOptionPane.ERROR_MESSAGE);
        }
    }
}