package sysSolutions.persistencia;

import sysSolutions.dominio.Paciente;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PacienteDAO {
    private ConnectionManager conn;

    public PacienteDAO() {
        conn = ConnectionManager.getInstance();
    }


    // Método para crear un nuevo paciente
    public Paciente create(Paciente paciente) throws SQLException {
        Paciente res = null;
        String sql = "INSERT INTO Pacientes (nombre, edad, sexo, contacto, direccion) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;

        try {
            ps = conn.connect().prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, paciente.getNombre());
            ps.setObject(2, paciente.getEdad(), java.sql.Types.INTEGER);
            ps.setString(3, paciente.getSexo() != null ? paciente.getSexo().toString() : null); // <- CAMBIO
            ps.setString(4, paciente.getContacto());
            ps.setString(5, paciente.getDireccion());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación del paciente falló, no se insertó ninguna fila.");
            }

            generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGenerado = generatedKeys.getInt(1);
                res = getById(idGenerado);
            } else {
                throw new SQLException("La creación del paciente falló, no se obtuvo el ID generado.");
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear el paciente: " + ex.getMessage(), ex);
        } finally {
            if (generatedKeys != null) try { generatedKeys.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            try { conn.disconnect(); } catch (SQLException ignored) {}
        }
        return res;
    }


    // Método para actualizar un paciente
    public boolean update(Paciente paciente) throws SQLException {
        boolean res = false;
        PreparedStatement ps = null;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Pacientes SET nombre = ?, edad = ?, sexo = ?, contacto = ?, direccion = ? WHERE id = ?"
            );

            ps.setString(1, paciente.getNombre());
            ps.setObject(2, paciente.getEdad(), java.sql.Types.INTEGER);
            ps.setString(3, paciente.getSexo() != null ? paciente.getSexo().toString() : null); // <- CAMBIO
            ps.setString(4, paciente.getContacto());
            ps.setString(5, paciente.getDireccion());
            ps.setInt(6, paciente.getId());

            res = ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el paciente: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            try { conn.disconnect(); } catch (SQLException ignored) {}
        }
        return res;
    }


    // Método para eliminar un paciente
    public boolean delete(Paciente paciente) throws SQLException {
        boolean res = false;
        PreparedStatement ps = null;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Pacientes WHERE id = ?"
            );
            ps.setInt(1, paciente.getId());

            res = ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el paciente: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            try { conn.disconnect(); } catch (SQLException ignored) {}
        }
        return res;
    }


    // Métodos para obtener pacientes
    public ArrayList<Paciente> getAll() throws SQLException {
        ArrayList<Paciente> pacientes = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre, edad, sexo, contacto, direccion FROM Pacientes ORDER BY nombre"
            );
            rs = ps.executeQuery();

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rs.getInt("id"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setEdad(rs.getObject("edad", Integer.class));
                String sexoStr = rs.getString("sexo");
                paciente.setSexo(sexoStr != null && !sexoStr.isEmpty() ? sexoStr.charAt(0) : null);
                paciente.setContacto(rs.getString("contacto"));
                paciente.setDireccion(rs.getString("direccion"));
                pacientes.add(paciente);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener todos los pacientes: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            try { conn.disconnect(); } catch (SQLException ignored) {}
        }
        return pacientes;
    }


    // Método para buscar pacientes por nombre parcial
    public ArrayList<Paciente> search(String nombreParcial) throws SQLException {
        ArrayList<Paciente> pacientes = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre, edad, sexo, contacto, direccion FROM Pacientes WHERE nombre LIKE ? ORDER BY nombre"
            );
            ps.setString(1, "%" + nombreParcial + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rs.getInt("id"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setEdad(rs.getObject("edad", Integer.class));
                String sexoStr = rs.getString("sexo");
                paciente.setSexo(sexoStr != null && !sexoStr.isEmpty() ? sexoStr.charAt(0) : null);
                paciente.setContacto(rs.getString("contacto"));
                paciente.setDireccion(rs.getString("direccion"));
                pacientes.add(paciente);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar pacientes: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            try { conn.disconnect(); } catch (SQLException ignored) {}
        }
        return pacientes;
    }



    // Método para obtener un paciente por ID
    public Paciente getById(int id) throws SQLException {
        Paciente paciente = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre, edad, sexo, contacto, direccion FROM Pacientes WHERE id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                paciente = new Paciente();
                paciente.setId(rs.getInt("id"));
                paciente.setNombre(rs.getString("nombre"));
                paciente.setEdad(rs.getObject("edad", Integer.class));
                String sexoStr = rs.getString("sexo");
                paciente.setSexo(sexoStr != null && !sexoStr.isEmpty() ? sexoStr.charAt(0) : null);
                paciente.setContacto(rs.getString("contacto"));
                paciente.setDireccion(rs.getString("direccion"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el paciente por ID: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignored) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignored) {}
            try { conn.disconnect(); } catch (SQLException ignored) {}
        }
        return paciente;
    }
}