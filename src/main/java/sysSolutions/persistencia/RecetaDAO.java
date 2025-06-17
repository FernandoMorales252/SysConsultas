package sysSolutions.persistencia;
import sysSolutions.dominio.Receta;
import sysSolutions.dominio.Medicamento;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;


public class RecetaDAO {
    private final ConnectionManager conn;

    public RecetaDAO() {
        this.conn = ConnectionManager.getInstance();
    }

    // Crear una receta
    public Receta create(Receta receta) throws SQLException {
        String sql = "INSERT INTO Recetas (cita_id, medicamento_id, dosis, observaciones) VALUES (?, ?, ?, ?)";
        Receta nuevaReceta = null;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, receta.getCitaId());
            ps.setInt(2, receta.getMedicamento().getId());
            ps.setString(3, receta.getDosis());
            ps.setString(4, receta.getObservaciones());

            int filas = ps.executeUpdate();
            if (filas == 0) throw new SQLException("Error al insertar la receta");

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    receta.setId(keys.getInt(1));
                    nuevaReceta = receta;
                }
            }
        }
        return nuevaReceta;
    }

    // Actualizar receta
    public boolean update(Receta receta) throws SQLException {
        String sql = "UPDATE Recetas SET cita_id = ?, medicamento_id = ?, dosis = ?, observaciones = ? WHERE id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, receta.getCitaId());
            ps.setInt(2, receta.getMedicamento().getId());
            ps.setString(3, receta.getDosis());
            ps.setString(4, receta.getObservaciones());
            ps.setInt(5, receta.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // Eliminar receta
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Recetas WHERE id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }


    // Obtener receta por ID
    public Receta getById(int id) throws SQLException {
        String sql = "SELECT r.*, " +
                "p.nombre AS paciente_nombre, d.nombre AS doctor_nombre, " +
                "m.id AS medicamento_id, m.nombre AS medicamento_nombre, m.contenido, m.fecha_expiracion " +
                "FROM Recetas r " +
                "JOIN Citas c ON r.cita_id = c.id " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "JOIN Medicamentos m ON r.medicamento_id = m.id " +
                "WHERE r.id = ?";

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medicamento med = new Medicamento(
                            rs.getInt("medicamento_id"),
                            rs.getString("medicamento_nombre"),
                            rs.getBigDecimal("contenido"),
                            rs.getDate("fecha_expiracion").toLocalDate()
                    );

                    return new Receta(
                            rs.getInt("id"),
                            rs.getInt("cita_id"),
                            med,
                            rs.getString("dosis"),
                            rs.getString("observaciones"),
                            rs.getString("paciente_nombre"),
                            rs.getString("doctor_nombre")
                    );
                }
            }
        }
        return null;
    }

    // Obtener todas las recetas
    public ArrayList<Receta> getAll() throws SQLException {
        String sql = "SELECT r.*, " +
                "p.nombre AS paciente_nombre, d.nombre AS doctor_nombre, " +
                "m.id AS medicamento_id, m.nombre AS medicamento_nombre, m.contenido, m.fecha_expiracion " +
                "FROM Recetas r " +
                "JOIN Citas c ON r.cita_id = c.id " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "JOIN Medicamentos m ON r.medicamento_id = m.id " +
                "ORDER BY r.id";
        ArrayList<Receta> recetas = new ArrayList<>();
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Medicamento med = new Medicamento(
                        rs.getInt("medicamento_id"),
                        rs.getString("medicamento_nombre"),
                        rs.getBigDecimal("contenido"),
                        rs.getDate("fecha_expiracion").toLocalDate()
                );

                Receta receta = new Receta(
                        rs.getInt("id"),
                        rs.getInt("cita_id"),
                        med,
                        rs.getString("dosis"),
                        rs.getString("observaciones"),
                        rs.getString("paciente_nombre"),
                        rs.getString("doctor_nombre")
                );
                recetas.add(receta);
            }
        }
        return recetas;
    }


    // Buscar recetas por nombre de paciente o doctor
    public ArrayList<Receta> getByPacienteOrDoctor(String nombre) throws SQLException {
        String sql = "SELECT r.*, " +
                "p.nombre AS paciente_nombre, d.nombre AS doctor_nombre, " +
                "m.id AS medicamento_id, m.nombre AS medicamento_nombre, m.contenido, m.fecha_expiracion " +
                "FROM Recetas r " +
                "JOIN Citas c ON r.cita_id = c.id " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "JOIN Medicamentos m ON r.medicamento_id = m.id " +
                "WHERE p.nombre LIKE ? OR d.nombre LIKE ? " +
                "ORDER BY r.id";

        ArrayList<Receta> recetas = new ArrayList<>();
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medicamento med = new Medicamento(
                            rs.getInt("medicamento_id"),
                            rs.getString("medicamento_nombre"),
                            rs.getBigDecimal("contenido"),
                            rs.getDate("fecha_expiracion").toLocalDate()
                    );

                    Receta receta = new Receta(
                            rs.getInt("id"),
                            rs.getInt("cita_id"),
                            med,
                            rs.getString("dosis"),
                            rs.getString("observaciones"),
                            rs.getString("paciente_nombre"),
                            rs.getString("doctor_nombre")
                    );
                    recetas.add(receta);
                }
            }
        }
        return recetas;
    }
}
