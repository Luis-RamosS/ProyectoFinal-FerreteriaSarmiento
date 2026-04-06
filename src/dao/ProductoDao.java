package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Producto;
import javax.swing.JOptionPane;

public class ProductoDao {
    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Metodo para Insertar Producto
    public int agregar(Producto p) {
        String sql = "INSERT INTO Productos (nombre, descripcion, precio_venta, categoria, cantidad) VALUES (?,?,?,?,?)";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setString(4, p.getCategoria());
            ps.setInt(5, p.getCantidad());
            
            return ps.executeUpdate();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar producto: " + e.getMessage());
            return 0;
        }
    }

    // Metodo para Listar Productos
    public List<Producto> listar() {
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM Productos";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Producto p = new Producto();
                p.setId_producto(rs.getInt("id_producto"));
                p.setNombre(rs.getString("nombre"));
                p.setDescripcion(rs.getString("descripcion"));
                p.setPrecio(rs.getDouble("precio_venta"));
                p.setCategoria(rs.getString("categoria"));
                p.setCantidad(rs.getInt("cantidad"));
                lista.add(p);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar productos: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean actualizar(Producto p) {
        String sql = "UPDATE productos SET nombre=?, descripcion=?, precio_venta=?, categoria=?, cantidad=? WHERE id_producto=?";
        boolean success = false;
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getDescripcion());
            ps.setDouble(3, p.getPrecio());
            ps.setString(4, p.getCategoria());
            ps.setInt(5, p.getCantidad());
            ps.setInt(6,p.getId_producto());
            
            if (ps.executeUpdate() > 0) {
                success = true;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al actualizar producto: " + e.getMessage());
        }
        return success;
    }
    
    public boolean eliminar(int id) {
        String sql = "DELETE FROM productos WHERE id_producto = ?";
        boolean success = false;
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            if (ps.executeUpdate() > 0) {
                success = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar producto: " + e.getMessage());
        }
        return success;
    }
    
    public int obtenerCantidadPorNombre(String nombre) {
        
        int stock = 0;
        // Consulta que une la tabla productos con inventario
        String sql = "SELECT i.stock_actual FROM inventario i "
                + "JOIN productos p ON i.id_producto = p.id_producto "
                + "WHERE p.nombre = ?";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            rs = ps.executeQuery();
            if (rs.next()) {
                stock = rs.getInt("stock_actual");
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener stock: " + e.getMessage());
        } 
        return stock;
    }
}