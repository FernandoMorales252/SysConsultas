package sysSolutions.persistencia;

import sysSolutions.dominio.Cita;

import java.sql.*;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.LocalTime;

public class CitaDAO {
    private final ConnectionManager conn;

    public CitaDAO() {
        this.conn = ConnectionManager.getInstance();
    }


    //crear una cita
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

            int filas = ps.executeUpdate();
            if (filas == 0) throw new SQLException("Error al insertar la cita, no se insertaron filas");

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    nuevaCita = new Cita(
                            keys.getInt(1),
                            cita.getPacienteId(),
                            cita.getPacienteNombre(),
                            cita.getDoctorId(),
                            cita.getDoctorNombre(),
                            cita.getFecha(),
                            cita.getHora(),
                            cita.getMotivo()
                    );
                }
            }
        }
        return nuevaCita;
    }

    //actualizar una cita
    public boolean update(Cita cita) throws SQLException {
        String sql = "UPDATE Citas SET paciente_id = ?, doctor_id = ?, fecha = ?, hora = ?, motivo = ? WHERE id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cita.getPacienteId());
            ps.setInt(2, cita.getDoctorId());
            ps.setDate(3, Date.valueOf(cita.getFecha()));
            ps.setTime(4, Time.valueOf(cita.getHora()));
            ps.setString(5, cita.getMotivo());
            ps.setInt(6, cita.getId());

            return ps.executeUpdate() > 0;
        }
    }


    //eliminar una cita
    public boolean delete(Cita cita) throws SQLException {
        String sql = "DELETE FROM Citas WHERE id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, cita.getId());
            return ps.executeUpdate() > 0;
        }
    }

    //obtener una cita por identificador
    public Cita getById(int id) throws SQLException {
        String sql = "SELECT c.id, c.paciente_id, p.nombre AS paciente_nombre, " +
                "c.doctor_id, d.nombre AS doctor_nombre, c.fecha, c.hora, c.motivo " +
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
                    cita = new Cita(
                            rs.getInt("id"),
                            rs.getInt("paciente_id"),
                            rs.getString("paciente_nombre"),
                            rs.getInt("doctor_id"),
                            rs.getString("doctor_nombre"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora").toLocalTime(),
                            rs.getString("motivo")
                    );
                }
            }
        }
        return cita;
    }

    //obtener todas las citas
    public ArrayList<Cita> getAll() throws SQLException {
        String sql = "SELECT c.id, c.paciente_id, p.nombre AS paciente_nombre, " +
                "c.doctor_id, d.nombre AS doctor_nombre, c.fecha, c.hora, c.motivo " +
                "FROM Citas c " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "ORDER BY c.fecha, c.hora";

        ArrayList<Cita> citas = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita cita = new Cita(
                        rs.getInt("id"),
                        rs.getInt("paciente_id"),
                        rs.getString("paciente_nombre"),
                        rs.getInt("doctor_id"),
                        rs.getString("doctor_nombre"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getTime("hora").toLocalTime(),
                        rs.getString("motivo")
                );
                citas.add(cita);
            }
        }

        return citas;
    }

    //obtener citas por paciente o doctor
    public ArrayList<Cita> searchBYName(String nombre) throws SQLException {
        String sql = "SELECT c.id, c.paciente_id, p.nombre AS paciente_nombre, " +
                "c.doctor_id, d.nombre AS doctor_nombre, c.fecha, c.hora, c.motivo " +
                "FROM Citas c " +
                "JOIN Pacientes p ON c.paciente_id = p.id " +
                "JOIN Doctores d ON c.doctor_id = d.id " +
                "WHERE p.nombre LIKE ? OR d.nombre LIKE ? " +
                "ORDER BY c.fecha, c.hora";

        ArrayList<Cita> citas = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");
            ps.setString(2, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Cita cita = new Cita(
                            rs.getInt("id"),
                            rs.getInt("paciente_id"),
                            rs.getString("paciente_nombre"),
                            rs.getInt("doctor_id"),
                            rs.getString("doctor_nombre"),
                            rs.getDate("fecha").toLocalDate(),
                            rs.getTime("hora").toLocalTime(),
                            rs.getString("motivo")
                    );
                    citas.add(cita);
                }
            }
        }
        return citas;
    }
}