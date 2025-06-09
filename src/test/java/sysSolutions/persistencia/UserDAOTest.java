package sysSolutions.persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sysSolutions.dominio.User;
import java.util.ArrayList;
import java.util.Random;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;

class UserDAOTest {
    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAO();
    }

    // Prueba privada para crear un usuario y validar que fue creado correctamente
    private User create(User user) throws SQLException {
        User res = userDAO.create(user);
        assertNotNull(res);
        assertEquals(user.getName(), res.getName());
        assertEquals(user.getEmail(), res.getEmail());
        assertEquals(user.getStatus(), res.getStatus());
        return res;
    }

    // Actualiza el nombre/email/status y valida el cambio
    private void update(User user) throws SQLException {
        user.setName(user.getName() + "_u");
        user.setEmail("u_" + user.getEmail());
        user.setStatus((byte) 1);
        boolean updated = userDAO.update(user);
        assertTrue(updated);
        getById(user);
    }

    // Busca un usuario por ID y compara campos
    private void getById(User user) throws SQLException {
        User res = userDAO.getById(user.getId());
        assertNotNull(res);
        assertEquals(user.getName(), res.getName());
        assertEquals(user.getEmail(), res.getEmail());
        assertEquals(user.getStatus(), res.getStatus());
    }

    // Valida que se pueda buscar por nombre parcialmente
    private void search(User user) throws SQLException {
        ArrayList<User> users = userDAO.search(user.getName());
        boolean find = users.stream().anyMatch(u -> u.getName().contains(user.getName()));
        assertTrue(find);
    }

    // Elimina usuario y verifica que ya no exista
    private void delete(User user) throws SQLException {
        assertTrue(userDAO.delete(user));
        assertNull(userDAO.getById(user.getId()));
    }

    // Autenticación exitosa con usuario activo
    private void autenticate(User user) throws SQLException {
        User res = userDAO.authenticate(user);
        assertNotNull(res);
        assertEquals(res.getEmail(), user.getEmail());
        assertEquals(res.getStatus(), 1);
    }

    // Fallo al autenticar (ej. contraseña incorrecta)
    private void autenticateFails(User user) throws SQLException {
        assertNull(userDAO.authenticate(user));
    }

    // Cambia la contraseña y prueba autenticación
    private void updatePassword(User user) throws SQLException {
        assertTrue(userDAO.updatePassword(user));
        autenticate(user);
    }

    // Test general de todos los métodos
    @Test
    void testUserDAO() throws SQLException {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String strEmail = "test" + num + "@example.com";
        User user = new User(0, "Test User " + num, strEmail, "password123", (byte) 2);

        User testUser = create(user);
        update(testUser);
        search(testUser);

        testUser.setPasswordHash(user.getPasswordHash()); // Restaurar contraseña original
        autenticate(testUser);
        testUser.setPasswordHash("12345");
        autenticateFails(testUser);
        delete(testUser);
    }

    // Crea un usuario específico (admin)
    @Test
    void createUser() throws SQLException {
        User user = new User(0, "admin", "12345", "admin@gmail.com", (byte) 1);
        User res = userDAO.create(user);
        assertNotNull(res);
    }
}