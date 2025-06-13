package sysSolutions.persistencia;
import sysSolutions.dominio.Receta;
import sysSolutions.persistencia.ConnectionManager;
import sysSolutions.dominio.Cita;
import java.time.LocalDateTime;
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
       String sql = "INSERT INTO Recetas (cita_id, medicamento, dosis,observaciones) VALUES (?, ?, ?,?)";
         Receta nuevaReceta = null;

         try (Connection connection = conn.connect();
              PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
             ps.setInt(1, receta.getCitaId());
             ps.setString(2, receta.getMedicamento());
             ps.setString(3, receta.getDosis());
             ps.setString(4, receta.getObservaciones());

             int filas = ps.executeUpdate();
             if (filas == 0) throw new SQLException("Error al insertar la receta, no se insertaron filas");

             try (ResultSet keys = ps.getGeneratedKeys()) {
                 if (keys.next()) {
                     nuevaReceta = new Receta(
                             keys.getInt(1),
                             receta.getCitaId(),
                             receta.getMedicamento(),
                             receta.getDosis(),
                             receta.getObservaciones()
                     );
                 }
             }
         }
        return nuevaReceta;
    }

    // Actualizar una receta
    public boolean update(Receta receta) throws SQLException {
        String sql = "UPDATE Recetas SET cita_id = ?, medicamento = ?, dosis = ?, observaciones = ? WHERE id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, receta.getCitaId());
            ps.setString(2, receta.getMedicamento());
            ps.setString(3, receta.getDosis());
            ps.setString(4, receta.getObservaciones());
            ps.setInt(5, receta.getId());

           return ps.executeUpdate() > 0;
        }
    }
    // Eliminar una receta

    public boolean delete(Receta receta) throws SQLException {
        String sql = "DELETE FROM Recetas WHERE id = ?";

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, receta.getId());
            return ps.executeUpdate() > 0;
        }
    }

    // Obtener una receta por ID

    public Receta getById(int id) throws SQLException {
       String sql = "SELECT r.id, r.cita_id, r.medicamento, r.dosis, r.observaciones, " +
               "p.nombre AS paciente_nombre, d.nombre AS doctor_nombre " +
               "FROM Recetas r " +
               "JOIN Citas c ON r.cita_id = c.id " +
               "JOIN Pacientes p ON c.paciente_id = p.id " +
               "JOIN Doctores d ON c.doctor_id = d.id " +
               "WHERE r.id = ?";

        Receta receta = null;
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    receta = new Receta(
                            rs.getInt("id"),
                            rs.getInt("cita_id"),
                            rs.getString("medicamento"),
                            rs.getString("dosis"),
                            rs.getString("observaciones"),
                            rs.getString("paciente_nombre"),
                            rs.getString("doctor_nombre")
                    );
                }
            }
        }
        return receta;
    }
    // Obtener todas las recetas

    public ArrayList<Receta> getAll() throws SQLException {
        String sql ="SELECT r.id, r.cita_id, r.medicamento, r.dosis, r.observaciones, " +
                "p.nombre AS paciente_nombre, d.nombre AS doctor_nombre " +
                "FROM Recetas r " +
                "JOIN Citas c ON r.cita_id = c.id " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "ORDER BY r.id";

        ArrayList<Receta> recetas = new ArrayList<>();
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Receta receta = new Receta(
                        rs.getInt("id"),
                        rs.getInt("cita_id"),
                        rs.getString("medicamento"),
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


    // Obtener recetas por nombre de paciente o doctor

    public ArrayList<Receta> getByPacienteOrDoctor(String nombre) throws SQLException {
        String sql = "SELECT r.id, r.cita_id, r.medicamento, r.dosis, r.observaciones, " +
                "p.nombre AS paciente_nombre, d.nombre AS doctor_nombre " +
                "FROM Recetas r " +
                "JOIN Citas c ON r.cita_id = c.id " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "WHERE p.nombre LIKE ? OR d.nombre LIKE ? " +
                "ORDER BY r.id";

        ArrayList<Receta> recetas = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Receta receta = new Receta(
                            rs.getInt("id"),
                            rs.getInt("cita_id"),
                            rs.getString("medicamento"),
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
