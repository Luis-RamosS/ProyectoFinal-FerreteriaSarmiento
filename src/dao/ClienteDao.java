package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Cliente;
import javax.swing.JOptionPane;

public class ClienteDao {
    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Metodo para Insertar un Cliente
    public int agregar(Cliente c) {
        String sql = "INSERT INTO clientes (identidad, nombre_completo, correo, telefono, direccion) VALUES (?,?,?,?,?)";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, c.getIdentidad());
            ps.setString(2, c.getNombre_completo());
            ps.setString(3, c.getCorreo());
            ps.setString(4, c.getTelefono());
            ps.setString(5, c.getDireccion());
            return ps.executeUpdate(); // Retorna 1 si se inserto correctamente
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al guardar cliente: " + e.getMessage());
            return 0;
        } finally {
            try { if(con != null) con.close(); } catch (SQLException ex) {}
        }
    }

    // Metodo para Listar Clientes (para mostrar en tu JTable)
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT * FROM clientes";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Cliente c = new Cliente();
                
                c.setId_cliente(rs.getInt("id_cliente"));
                c.setIdentidad(rs.getString("identidad"));
                c.setNombre_completo(rs.getString("nombre_completo"));
                c.setTelefono(rs.getString("telefono"));
                c.setCorreo(rs.getString("correo"));
                c.setDireccion(rs.getString("direccion"));
                lista.add(c);
            }
        } catch (SQLException e) {
            System.err.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }
    
    public boolean actualizar(Cliente cl) {
        String sql = "UPDATE clientes SET identidad=?, nombre_completo=?, correo=?, telefono=?, direccion=? WHERE id_cliente=?";
        boolean success = false; // Variable para rastrear el resultado

        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, cl.getIdentidad());
            ps.setString(2, cl.getNombre_completo());
            ps.setString(3, cl.getCorreo());
            ps.setString(4, cl.getTelefono());
            ps.setString(5, cl.getDireccion());
            ps.setInt(6, cl.getId_cliente());

            // executeUpdate devuelve el número de filas afectadas
            int resultado = ps.executeUpdate();

            if (resultado > 0) {
                success = true; 
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar: " + e.getMessage());
            success = false;
        }

        return success; 
    }
    
    public void eliminar(int id) {
    String sql = "DELETE FROM Clientes WHERE id_cliente = ?";
    try {
        con = conectar.getConnection();
        ps = con.prepareStatement(sql);
        ps.setInt(1, id);
        ps.executeUpdate();
        }catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
        }
    }
}
