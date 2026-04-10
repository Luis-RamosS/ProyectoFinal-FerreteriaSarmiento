package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
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

    
    public boolean actualizarStock(int nuevaCantidad, int idProducto) {
        // Definimos las dos consultas: una para inventario y otra para productos
        String sqlInv = "UPDATE inventario SET stock_actual = ? WHERE id_producto = ?";
        String sqlProd = "UPDATE productos SET cantidad = ? WHERE id_producto = ?";

        try (Connection con = conectar.getConnection()) {
            // Iniciamos transacción manual
            con.setAutoCommit(false);

            try (PreparedStatement psInv = con.prepareStatement(sqlInv); PreparedStatement psProd = con.prepareStatement(sqlProd)) {

                // 1. Actualizamos la tabla INVENTARIO
                psInv.setInt(1, nuevaCantidad);
                psInv.setInt(2, idProducto);
                psInv.executeUpdate();

                // 2. Actualizamos la tabla PRODUCTOS (para que coincidan)
                psProd.setInt(1, nuevaCantidad);
                psProd.setInt(2, idProducto);
                psProd.executeUpdate();

                // Si ambos procesos terminan bien, guardamos cambios
                con.commit();
                return true;

            } catch (SQLException e) {
                // Si algo falla, deshacemos todo (Rollback)
                con.rollback();
                JOptionPane.showMessageDialog(null, "Error al actualizar stock: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de conexión: " + e.getMessage());
            return false;
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
    
    public List<Object[]> listarInventarioCompleto() {
        List<Object[]> lista = new ArrayList<>();
        // Esta consulta une el nombre del producto con sus datos de inventario
        String sql = "SELECT p.nombre, i.stock_actual, i.stock_minimo, i.ultima_actualizacion "
                + "FROM inventario i JOIN productos p ON i.id_producto = p.id_producto";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[4];
                fila[0] = rs.getString("nombre");
                fila[1] = rs.getInt("stock_actual");
                fila[2] = rs.getInt("stock_minimo");
                fila[3] = rs.getTimestamp("ultima_actualizacion");
                lista.add(fila);
            }
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.toString());
        }
        return lista;
    }
}
