package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import modelo.Usuario;
import javax.swing.JOptionPane;

public class UsuarioDao {
    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;
    
    
    public boolean agregarUsuario(String user, String pass, String rol) {
        String sql = "INSERT INTO usuarios (nombre_usuario, password, rol) VALUES (?,?,?)";
        boolean success = false;
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, rol);

            if (ps.executeUpdate() > 0) {
                success = true;
            }
        } catch (SQLException e) {
            System.out.println("Error al crear usuario: " + e.getMessage());
        }
        return success;
    }
    
    public List<Object[]> listarUsuarios() {
        List<Object[]> lista = new ArrayList<>();
        String sql = "SELECT id_usuario, nombre_usuario, rol FROM usuarios";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Object[] fila = new Object[3];
                fila[0] = rs.getInt("id_usuario");
                fila[1] = rs.getString("nombre_usuario");
                fila[2] = rs.getString("rol");
                lista.add(fila);
            }
            // Cierre manual de recursos
            rs.close();
            ps.close();
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
        return lista;
    }
    
    // Metodo para el Login
    public Usuario login(String nombre, String pass) {
        Usuario us = new Usuario();
        String sql = "SELECT * FROM usuarios WHERE nombre_usuario = ? AND password = ?";
        try {
            con = conectar.getConnection();
            ps = con.prepareStatement(sql);
            ps.setString(1, nombre);
            ps.setString(2, pass);
            rs = ps.executeQuery();
            
            if (rs.next()) {
                us.setId_usuario(rs.getInt("id_usuario"));
                us.setNombre_usuario(rs.getString("nombre_usuario"));
                us.setPassword(rs.getString("password"));
                us.setRol(rs.getString("rol"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error en login: " + e.getMessage());
        }
        return us;
    }
}