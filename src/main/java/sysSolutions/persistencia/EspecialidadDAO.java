package sysSolutions.persistencia;


import sysSolutions.dominio.Especialidad;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EspecialidadDAO {
    private ConnectionManager conn;

    public EspecialidadDAO() {
        conn = ConnectionManager.getInstance();
    }

    public Especialidad create(Especialidad especialidad) throws SQLException {
        Especialidad res = null;
        String sql = "INSERT INTO Especialidades (nombre) VALUES (?)";
        PreparedStatement ps = null;
        ResultSet generatedKeys = null;

        try {
            ps = conn.connect().prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, especialidad.getNombre());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("La creación falló, no se insertó ninguna fila.");
            }

            generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                int idGenerado = generatedKeys.getInt(1);
                res = getById(idGenerado);
            } else {
                throw new SQLException("La creación falló, no se obtuvo el ID generado.");
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al crear la especialidad: " + ex.getMessage(), ex);
        } finally {
            if (generatedKeys != null) {
                try { generatedKeys.close(); } catch (SQLException e) { /* log error */ }
            }
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { /* log error */ }
            }
            try { conn.disconnect(); } catch (SQLException e) { /* log error */ }
        }

        return res;
    }

    public boolean update(Especialidad especialidad) throws SQLException {
        boolean res = false;
        PreparedStatement ps = null;

        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE Especialidades SET nombre = ? WHERE id = ?"
            );

            ps.setString(1, especialidad.getNombre());
            ps.setInt(2, especialidad.getId());

            res = ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar la especialidad: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { /* log error */ }
            }
            try { conn.disconnect(); } catch (SQLException e) { /* log error */ }
        }
        return res;
    }

    // Cambiado a recibir id directamente para facilitar la llamada desde test y otros
    public boolean delete(int id) throws SQLException {
        boolean res = false;
        PreparedStatement ps = null;

        try {
            ps = conn.connect().prepareStatement(
                    "DELETE FROM Especialidades WHERE id = ?"
            );
            ps.setInt(1, id);

            res = ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar la especialidad: " + ex.getMessage(), ex);
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { /* log error */ }
            }
            try { conn.disconnect(); } catch (SQLException e) { /* log error */ }
        }
        return res;
    }

    public ArrayList<Especialidad> getAll() throws SQLException {
        ArrayList<Especialidad> especialidades = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre FROM Especialidades ORDER BY nombre"
            );
            rs = ps.executeQuery();

            while (rs.next()) {
                Especialidad especialidad = new Especialidad();
                especialidad.setId(rs.getInt("id"));
                especialidad.setNombre(rs.getString("nombre"));
                especialidades.add(especialidad);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener todas las especialidades: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* log error */ }
            }
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { /* log error */ }
            }
            try { conn.disconnect(); } catch (SQLException e) { /* log error */ }
        }
        return especialidades;
    }

    public ArrayList<Especialidad> search(String nombre) throws SQLException {
        ArrayList<Especialidad> especialidades = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre FROM Especialidades WHERE nombre LIKE ? ORDER BY nombre"
            );
            ps.setString(1, "%" + nombre + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                Especialidad especialidad = new Especialidad();
                especialidad.setId(rs.getInt("id"));
                especialidad.setNombre(rs.getString("nombre"));
                especialidades.add(especialidad);
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al buscar especialidades: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* log error */ }
            }
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { /* log error */ }
            }
            try { conn.disconnect(); } catch (SQLException e) { /* log error */ }
        }
        return especialidades;
    }

    public Especialidad getById(int id) throws SQLException {
        Especialidad especialidad = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, nombre FROM Especialidades WHERE id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                especialidad = new Especialidad();
                especialidad.setId(rs.getInt("id"));
                especialidad.setNombre(rs.getString("nombre"));
            }

        } catch (SQLException ex) {
            throw new SQLException("Error al obtener la especialidad por ID: " + ex.getMessage(), ex);
        } finally {
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { /* log error */ }
            }
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { /* log error */ }
            }
            try { conn.disconnect(); } catch (SQLException e) { /* log error */ }
        }
        return especialidad;
    }
}

