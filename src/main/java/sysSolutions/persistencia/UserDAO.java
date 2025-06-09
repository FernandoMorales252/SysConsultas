package sysSolutions.persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import sysSolutions.dominio.User;
import sysSolutions.utils.PasswordHasher;

public class UserDAO {
    private ConnectionManager conn;
    private PreparedStatement ps;
    private ResultSet rs;

    public UserDAO() {
        conn = ConnectionManager.getInstance();
    }

    // Crear un nuevo usuario y devolverlo desde la base de datos (incluyendo su ID generado)
    public User create(User user) throws SQLException {
        User res = null;
        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO users (name, email, passwordHash, status) VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, PasswordHasher.hashPassword(user.getPasswordHash()));
            ps.setByte(4, user.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getById(idGenerado); // Se devuelve el usuario completo desde BD
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("error al crear el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    // Actualiza los datos de un usuario (excepto la contraseña)
    public boolean update(User user) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE users SET name = ?, email = ?, status = ? WHERE id = ?"
            );
            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setByte(3, user.getStatus());
            ps.setInt(4, user.getId());

            res = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    // Elimina un usuario por su ID
    public boolean delete(User user) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM users WHERE id = ?");
            ps.setInt(1, user.getId());
            res = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    // Buscar usuarios cuyo nombre contenga una subcadena
    public ArrayList<User> search(String name) throws SQLException {
        ArrayList<User> records = new ArrayList<>();
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, name, email, status FROM users WHERE name LIKE ?"
            );
            ps.setString(1, "%" + name + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setStatus(rs.getByte(4));
                records.add(user);
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al buscar usuarios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    // Obtener un usuario por su ID
    public User getById(int id) throws SQLException {
        User user = null;
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, name, email, status FROM users WHERE id = ?"
            );
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setStatus(rs.getByte(4));
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al obtener el usuario por ID: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return user;
    }

    // Autenticar usuario con email y contraseña (si está activo)
    public User authenticate(User user) throws SQLException {
        User userAuthenticate = null;
        try {
            ps = conn.connect().prepareStatement(
                    "SELECT id, name, email, status FROM users WHERE email = ? AND passwordHash = ? AND status = 1"
            );
            ps.setString(1, user.getEmail());
            ps.setString(2, PasswordHasher.hashPassword(user.getPasswordHash()));
            rs = ps.executeQuery();

            if (rs.next()) {
                userAuthenticate = new User();
                userAuthenticate.setId(rs.getInt(1));
                userAuthenticate.setName(rs.getString(2));
                userAuthenticate.setEmail(rs.getString(3));
                userAuthenticate.setStatus(rs.getByte(4));
            }

            ps.close();
            rs.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al autenticar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return userAuthenticate;
    }

    // Actualizar la contraseña de un usuario
    public boolean updatePassword(User user) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE users SET passwordHash = ? WHERE id = ?"
            );
            ps.setString(1, PasswordHasher.hashPassword(user.getPasswordHash()));
            ps.setInt(2, user.getId());
            res = ps.executeUpdate() > 0;
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar la contraseña del usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }
}