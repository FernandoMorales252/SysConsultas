package sysSolutions.persistencia;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import sysSolutions.dominio.Medicamento;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;


import java.sql.*;

public class MedicamentoDAO {
    private final ConnectionManager conn;

    public MedicamentoDAO() {
        this.conn = ConnectionManager.getInstance();
    }

    //crear un medicamento
 public Medicamento create(Medicamento medicamento) throws SQLException {
        String sql = "INSERT INTO Medicamentos (nombre, contenido, fecha_expiracion) VALUES (?, ?, ?)";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, medicamento.getNombre());
            ps.setBigDecimal(2,  medicamento.getContenido());
            ps.setDate(3, Date.valueOf(medicamento.getFechaExpiracion()));

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("No se pudo insertar el medicamento.");
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    medicamento.setId(rs.getInt(1));
                }
            }
        }
        return medicamento;
    }


    // LISTAR TODOS
    public ArrayList<Medicamento> getAll() throws SQLException {
        String sql = "SELECT * FROM Medicamentos ORDER BY id";
        ArrayList<Medicamento> medicamentos = new ArrayList<>();

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medicamento m = new Medicamento(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getBigDecimal("contenido"),
                        rs.getDate("fecha_expiracion").toLocalDate()
                );
                medicamentos.add(m);
            }
        }
        return medicamentos;
    }

    // BUSCAR POR ID
    public Medicamento getById(int id) throws SQLException {
        String sql = "SELECT * FROM Medicamentos WHERE id = ?";
        Medicamento m = null;

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    m = new Medicamento(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getBigDecimal("contenido"),
                            rs.getDate("fecha_expiracion").toLocalDate()
                    );
                }
            }
        }
        return m;
    }

    // ACTUALIZAR
    public boolean update(Medicamento medicamento) throws SQLException {
        String sql = "UPDATE Medicamentos SET nombre = ?, contenido = ?, fecha_expiracion = ? WHERE id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, medicamento.getNombre());
            ps.setBigDecimal(2,  medicamento.getContenido());
            ps.setDate(3, Date.valueOf(medicamento.getFechaExpiracion()));
            ps.setInt(4, medicamento.getId());

            return ps.executeUpdate() > 0;
        }
    }

    // ELIMINAR
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM Medicamentos WHERE id = ?";
        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    public ArrayList<Medicamento> search(String nombre) throws SQLException {
        ArrayList<Medicamento> medicamentos = new ArrayList<>();
        String sql = "SELECT id, nombre, contenido, fecha_expiracion FROM Medicamentos WHERE nombre LIKE ? ORDER BY nombre";

        try (Connection connection = conn.connect();
             PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, "%" + nombre + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Medicamento m = new Medicamento(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getBigDecimal("contenido"),
                            rs.getDate("fecha_expiracion").toLocalDate()
                    );
                    medicamentos.add(m);
                }
            }
        }
        return medicamentos;
    }




    }

