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

    public User create(User user) throws SQLException {
        User res = null;
        try {
            PreparedStatement ps = conn.connect().prepareStatement(
                    "INSERT INTO users (name, email, password_hash, status) VALUES (?, ?, ?, ?)",
                    java.sql.Statement.RETURN_GENERATED_KEYS
            );

            ps.setString(1, user.getName());
            ps.setString(2, PasswordHasher.hashPassword(user.getPasswordHash()));
            ps.setString(3, user.getEmail());
            ps.setByte(4, user.getStatus());

            int affectedRows = ps.executeUpdate();

            if (affectedRows != 0) {
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    int idGenerado = generatedKeys.getInt(1);
                    res = getBYId(idGenerado);
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


    public boolean update(User user) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE users SET name = ?" +
                            ", email = ?" +
                            ", status = ? " +
                            "WHERE id = ?"
            );

            ps.setString(1, user.getName());
            ps.setString(3, user.getEmail());
            ps.setByte(4, user.getStatus());
            ps.setInt(5, user.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al actualizar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }

    public boolean delete (User user) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement("DELETE FROM users WHERE id = ?");

            ps.setInt(1, user.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }

            ps.close();
        } catch (SQLException ex) {
            throw new SQLException("Error al eliminar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            conn.disconnect();
        }
        return res;
    }


    public ArrayList<User> search(String name) throws SQLException{
        ArrayList<User> records = new ArrayList<>();
        try{
            ps= conn.connect().prepareStatement
                    (
                    "SELECT id, name, email, status " + "FROM users" +
                            " WHERE name LIKE ?"
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

        }
        catch (SQLException ex) {
            throw new SQLException("Error al buscar usuarios: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return records;
    }

    public User getBYId(int id) throws SQLException {
        User user = null;
        try {
            ps = conn.connect().prepareStatement("SELECT id, name, email, status" + " FROM users "
                    + "WHERE id = ?");

            ps.setInt(1, id);

            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setEmail(rs.getString(3));
                user.setStatus(rs.getByte(4));
            } else {
                user = null;
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
         return  user;




    }

    public User authenticate (User user) throws SQLException {
        User userAuthenticate = null;
        try{
            ps= conn.connect().prepareStatement
                    (
                            "SELECT id, name, email, status" + " FROM users " + "WHERE email = ? AND password_hash = ? AND status = 1"
                    );
            ps.setString(1,user.getEmail());
            ps.setString(2, PasswordHasher.hashPassword(user.getPasswordHash()));
            rs = ps.executeQuery();

            if (rs.next()) {

                userAuthenticate.setId(rs.getInt(1));
                userAuthenticate.setName(rs.getString(2));
                userAuthenticate.setEmail(rs.getString(3));
                userAuthenticate.setStatus(rs.getByte(4));
            } else {
                userAuthenticate = null;
            }
            ps.close();
            rs.close();
        }
        catch (SQLException ex) {
            throw new SQLException("Error al autenticar el usuario: " + ex.getMessage(), ex);
        } finally {
            ps = null;
            rs = null;
            conn.disconnect();
        }
        return userAuthenticate;

    }

    public boolean updatePassword(User user) throws SQLException {
        boolean res = false;
        try {
            ps = conn.connect().prepareStatement(
                    "UPDATE users " +
                            "SET password_hash = ? " + "WHERE id = ?"
            );

            ps.setString(1, PasswordHasher.hashPassword(user.getPasswordHash()));
            ps.setInt(2, user.getId());

            if (ps.executeUpdate() > 0) {
                res = true;
            }
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
