package dao;

import conexion.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import modelo.Usuario;
import javax.swing.JOptionPane;

public class UsuarioDao {
    Conexion conectar = new Conexion();
    Connection con;
    PreparedStatement ps;
    ResultSet rs;

    // Metodo fundamental para el Login
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