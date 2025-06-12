package sysSolutions.persistencia;

import sysSolutions.dominio.Cita;
import sysSolutions.dominio.Doctor; // Necesario para obtener el nombre del doctor
import sysSolutions.dominio.Paciente; // Necesario para obtener el nombre del paciente

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CitaDAO {

    private final ConnectionManager conn;

    public CitaDAO() {
        conn = ConnectionManager.getInstance();
    }

    // Método para crear una nueva cita
    public Cita create(Cita cita) throws SQLException {
        String sql = "INSERT INTO Citas (paciente_id, doctor_id, fecha, hora, motivo) VALUES (?, ?, ?, ?, ?)";
        Cita nuevaCita = null;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, cita.getPacienteId());
            ps.setInt(2, cita.getDoctorId());
            ps.setDate(3, Date.valueOf(cita.getFecha()));
            ps.setTime(4, Time.valueOf(cita.getHora()));
            ps.setString(5, cita.getMotivo());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La creación de la cita falló, no se insertó ninguna fila.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    nuevaCita = new Cita();
                    nuevaCita.setId(generatedKeys.getInt(1));
                    nuevaCita.setPacienteId(cita.getPacienteId());
                    nuevaCita.setDoctorId(cita.getDoctorId());
                    nuevaCita.setFecha(cita.getFecha());
                    nuevaCita.setHora(cita.getHora());
                    nuevaCita.setMotivo(cita.getMotivo());
                } else {
                    throw new SQLException("La creación de la cita falló, no se obtuvo el ID generado.");
                }
            }
        }
        return nuevaCita;
    }

    // Método para actualizar una cita
    public boolean update(Cita cita) throws SQLException {
        String sql = "UPDATE Citas SET paciente_id = ?, doctor_id = ?, fecha = ?, hora = ?, motivo = ? WHERE id = ?";
        boolean actualizado;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cita.getPacienteId());
            ps.setInt(2, cita.getDoctorId());
            ps.setDate(3, Date.valueOf(cita.getFecha()));
            ps.setTime(4, Time.valueOf(cita.getHora()));
            ps.setString(5, cita.getMotivo());
            ps.setInt(6, cita.getId());

            actualizado = ps.executeUpdate() > 0;
        }
        return actualizado;
    }

    // Método para eliminar una cita
    public boolean delete(Cita cita) throws SQLException {
        String sql = "DELETE FROM Citas WHERE id = ?";
        boolean eliminado;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, cita.getId());
            eliminado = ps.executeUpdate() > 0;
        }
        return eliminado;
    }

    // Método para obtener una cita por su ID, incluyendo nombres de paciente y doctor
    public Cita getById(int id) throws SQLException {
        String sql = "SELECT c.id, c.paciente_id, p.nombre AS paciente_nombre, " +
                "c.doctor_id, d.nombre AS doctor_nombre, " +
                "c.fecha, c.hora, c.motivo " +
                "FROM Citas c " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "WHERE c.id = ?";
        Cita cita = null;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    cita = new Cita();
                    cita.setId(rs.getInt("id"));
                    cita.setPacienteId(rs.getInt("paciente_id"));
                    cita.setPacienteNombre(rs.getString("paciente_nombre"));
                    cita.setDoctorId(rs.getInt("doctor_id"));
                    cita.setDoctorNombre(rs.getString("doctor_nombre"));
                    cita.setFecha(rs.getDate("fecha").toLocalDate());
                    cita.setHora(rs.getTime("hora").toLocalTime());
                    cita.setMotivo(rs.getString("motivo"));
                }
            }
        }
        return cita;
    }

    // Método para obtener todas las citas, incluyendo nombres de paciente y doctor
    public ArrayList<Cita> getAll() throws SQLException {
        String sql = "SELECT c.id, c.paciente_id, p.nombre AS paciente_nombre, " +
                "c.doctor_id, d.nombre AS doctor_nombre, " +
                "c.fecha, c.hora, c.motivo " +
                "FROM Citas c " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "ORDER BY c.fecha DESC, c.hora DESC";

        ArrayList<Cita> citas = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita cita = new Cita();
                cita.setId(rs.getInt("id"));
                cita.setPacienteId(rs.getInt("paciente_id"));
                cita.setPacienteNombre(rs.getString("paciente_nombre"));
                cita.setDoctorId(rs.getInt("doctor_id"));
                cita.setDoctorNombre(rs.getString("doctor_nombre"));
                cita.setFecha(rs.getDate("fecha").toLocalDate());
                cita.setHora(rs.getTime("hora").toLocalTime());
                cita.setMotivo(rs.getString("motivo"));
                citas.add(cita);
            }
        }
        return citas;
    }

    // Método para buscar citas por el nombre del paciente
    public ArrayList<Cita> searchByPacienteName(String pacienteNombre) throws SQLException {
        String sql = "SELECT c.id, c.paciente_id, p.nombre AS paciente_nombre, " +
                "c.doctor_id, d.nombre AS doctor_nombre, " +
                "c.fecha, c.hora, c.motivo " +
                "FROM Citas c " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "WHERE p.nombre LIKE ? " +
                "ORDER BY c.fecha DESC, c.hora DESC";

        ArrayList<Cita> citas = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + pacienteNombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita();
                    cita.setId(rs.getInt("id"));
                    cita.setPacienteId(rs.getInt("paciente_id"));
                    cita.setPacienteNombre(rs.getString("paciente_nombre"));
                    cita.setDoctorId(rs.getInt("doctor_id"));
                    cita.setDoctorNombre(rs.getString("doctor_nombre"));
                    cita.setFecha(rs.getDate("fecha").toLocalDate());
                    cita.setHora(rs.getTime("hora").toLocalTime());
                    cita.setMotivo(rs.getString("motivo"));
                    citas.add(cita);
                }
            }
        }
        return citas;
    }

    // Método para buscar citas por el nombre del doctor
    public ArrayList<Cita> searchByDoctorName(String doctorNombre) throws SQLException {
        String sql = "SELECT c.id, c.paciente_id, p.nombre AS paciente_nombre, " +
                "c.doctor_id, d.nombre AS doctor_nombre, " +
                "c.fecha, c.hora, c.motivo " +
                "FROM Citas c " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "WHERE d.nombre LIKE ? " +
                "ORDER BY c.fecha DESC, c.hora DESC";

        ArrayList<Cita> citas = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + doctorNombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita();
                    cita.setId(rs.getInt("id"));
                    cita.setPacienteId(rs.getInt("paciente_id"));
                    cita.setPacienteNombre(rs.getString("paciente_nombre"));
                    cita.setDoctorId(rs.getInt("doctor_id"));
                    cita.setDoctorNombre(rs.getString("doctor_nombre"));
                    cita.setFecha(rs.getDate("fecha").toLocalDate());
                    cita.setHora(rs.getTime("hora").toLocalTime());
                    cita.setMotivo(rs.getString("motivo"));
                    citas.add(cita);
                }
            }
        }
        return citas;
    }

    // Método para obtener citas por fecha
    public ArrayList<Cita> getCitasByFecha(LocalDate fecha) throws SQLException {
        String sql = "SELECT c.id, c.paciente_id, p.nombre AS paciente_nombre, " +
                "c.doctor_id, d.nombre AS doctor_nombre, " +
                "c.fecha, c.hora, c.motivo " +
                "FROM Citas c " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "WHERE c.fecha = ? " +
                "ORDER BY c.hora ASC";

        ArrayList<Cita> citas = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita();
                    cita.setId(rs.getInt("id"));
                    cita.setPacienteId(rs.getInt("paciente_id"));
                    cita.setPacienteNombre(rs.getString("paciente_nombre"));
                    cita.setDoctorId(rs.getInt("doctor_id"));
                    cita.setDoctorNombre(rs.getString("doctor_nombre"));
                    cita.setFecha(rs.getDate("fecha").toLocalDate());
                    cita.setHora(rs.getTime("hora").toLocalTime());
                    cita.setMotivo(rs.getString("motivo"));
                    citas.add(cita);
                }
            }
        }
        return citas;
    }

    // Método para obtener citas por rango de fechas
    public ArrayList<Cita> getCitasByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) throws SQLException {
        String sql = "SELECT c.id, c.paciente_id, p.nombre AS paciente_nombre, " +
                "c.doctor_id, d.nombre AS doctor_nombre, " +
                "c.fecha, c.hora, c.motivo " +
                "FROM Citas c " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "WHERE c.fecha BETWEEN ? AND ? " +
                "ORDER BY c.fecha ASC, c.hora ASC";

        ArrayList<Cita> citas = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(fechaInicio));
            ps.setDate(2, Date.valueOf(fechaFin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita();
                    cita.setId(rs.getInt("id"));
                    cita.setPacienteId(rs.getInt("paciente_id"));
                    cita.setPacienteNombre(rs.getString("paciente_nombre"));
                    cita.setDoctorId(rs.getInt("doctor_id"));
                    cita.setDoctorNombre(rs.getString("doctor_nombre"));
                    cita.setFecha(rs.getDate("fecha").toLocalDate());
                    cita.setHora(rs.getTime("hora").toLocalTime());
                    cita.setMotivo(rs.getString("motivo"));
                    citas.add(cita);
                }
            }
        }
        return citas;
    }
}