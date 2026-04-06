package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import modelo.Producto;

public class InventarioDao {

    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // 1. Método para obtener el stock actual de un producto por su ID
    public int obtenerStock(int idProducto) {
        int stock = 0;
        String sql = "SELECT stock_actual FROM inventario WHERE id_producto = ?";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, idProducto);
            rs = ps.executeQuery();
            if (rs.next()) {
                stock = rs.getInt("stock_actual");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener stock: " + e.getMessage());
        } finally {
            cerrarConexiones();
        }
        return stock;
    }

    // 2. Método para actualizar el stock (Suma lo nuevo a lo viejo)
    public boolean actualizarStock(int nuevaCantidad, int idProducto) {
        String sql = "UPDATE inventario SET stock_actual = ? WHERE id_producto = ?";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, nuevaCantidad);
            ps.setInt(2, idProducto);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al actualizar inventario: " + e.getMessage());
            return false;
        } finally {
            cerrarConexiones();
        }
    }

    // 3. Método para cerrar conexiones y evitar que se congele SQLyog
    private void cerrarConexiones() {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (con != null) {
                con.close();
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar: " + e.getMessage());
        }
    }
}
