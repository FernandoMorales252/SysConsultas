package sysSolutions.persistencia;

import sysSolutions.dominio.Doctor;
import sysSolutions.dominio.Especialidad;

import java.sql.*;
import java.util.ArrayList;

public class DoctorDAO {

    private final ConnectionManager conn;

    public DoctorDAO() {
        conn = ConnectionManager.getInstance();
    }

    // Método para crear un nuevo doctor
    public Doctor create(Doctor doctor) throws SQLException {
        String sql = "INSERT INTO Doctores (nombre, contacto, especialidad_id) VALUES (?, ?, ?)";
        Doctor nuevoDoctor = null;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, doctor.getNombre());
            ps.setString(2, doctor.getContacto());
            ps.setInt(3, doctor.getEspecialidad().getId());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La creación falló, no se insertó ninguna fila.");
            }

            try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    nuevoDoctor = new Doctor();
                    nuevoDoctor.setId(generatedKeys.getInt(1));
                    nuevoDoctor.setNombre(doctor.getNombre());
                    nuevoDoctor.setContacto(doctor.getContacto());
                    nuevoDoctor.setEspecialidad(doctor.getEspecialidad());
                } else {
                    throw new SQLException("La creación falló, no se obtuvo el ID generado.");
                }
            }
        }

        return nuevoDoctor;
    }

    // Método para actualizar un doctor
    public boolean update(Doctor doctor) throws SQLException {
        String sql = "UPDATE Doctores SET nombre = ?, contacto = ?, especialidad_id = ? WHERE id = ?";
        boolean actualizado;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, doctor.getNombre());
            ps.setString(2, doctor.getContacto());
            ps.setInt(3, doctor.getEspecialidad().getId());
            ps.setInt(4, doctor.getId());

            actualizado = ps.executeUpdate() > 0;
        }

        return actualizado;
    }

    // Método para eliminar un doctor
    public boolean delete(Doctor doctor) throws SQLException {
        String sql = "DELETE FROM Doctores WHERE id = ?";
        boolean eliminado;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, doctor.getId());
            eliminado = ps.executeUpdate() > 0;
        }

        return eliminado;
    }

    // Método para obtener un doctor por su ID
    public Doctor getById(int id) throws SQLException {
        String sql = "SELECT d.id, d.nombre, d.contacto, e.id AS especialidad_id, e.nombre AS especialidad_nombre " +
                "FROM Doctores d " +
                "JOIN Especialidades e ON d.especialidad_id = e.id " +
                "WHERE d.id = ?";
        Doctor doctor = null;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Especialidad especialidad = new Especialidad(
                            rs.getInt("especialidad_id"),
                            rs.getString("especialidad_nombre")
                    );

                    doctor = new Doctor();
                    doctor.setId(rs.getInt("id"));
                    doctor.setNombre(rs.getString("nombre"));
                    doctor.setContacto(rs.getString("contacto"));
                    doctor.setEspecialidad(especialidad);
                }
            }
        }

        return doctor;
    }

    // Método para obtener todos los doctores
    public ArrayList<Doctor> getAll() throws SQLException {
        String sql = "SELECT d.id, d.nombre, d.contacto, e.id AS especialidad_id, e.nombre AS especialidad_nombre " +
                "FROM Doctores d " +
                "JOIN Especialidades e ON d.especialidad_id = e.id " +
                "ORDER BY d.nombre";

        ArrayList<Doctor> doctores = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Especialidad especialidad = new Especialidad(
                        rs.getInt("especialidad_id"),
                        rs.getString("especialidad_nombre")
                );

                Doctor doctor = new Doctor();
                doctor.setId(rs.getInt("id"));
                doctor.setNombre(rs.getString("nombre"));
                doctor.setContacto(rs.getString("contacto"));
                doctor.setEspecialidad(especialidad);
                doctores.add(doctor);
            }
        }

        return doctores;
    }

    // Método para buscar doctores por nombre
    public ArrayList<Doctor> searchByName(String nombre) throws SQLException {
        String sql = "SELECT d.id, d.nombre, d.contacto, e.id AS especialidad_id, e.nombre AS especialidad_nombre " +
                "FROM Doctores d " +
                "JOIN Especialidades e ON d.especialidad_id = e.id " +
                "WHERE d.nombre LIKE ? " +
                "ORDER BY d.nombre";

        ArrayList<Doctor> doctores = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Especialidad especialidad = new Especialidad(
                            rs.getInt("especialidad_id"),
                            rs.getString("especialidad_nombre")
                    );

                    Doctor doctor = new Doctor();
                    doctor.setId(rs.getInt("id"));
                    doctor.setNombre(rs.getString("nombre"));
                    doctor.setContacto(rs.getString("contacto"));
                    doctor.setEspecialidad(especialidad);
                    doctores.add(doctor);
                }
            }
        }

        return doctores;
    }
}
