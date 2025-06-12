package sysSolutions.persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sysSolutions.dominio.Especialidad;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class EspecialidadDAOTest {

    private EspecialidadDAO especialidadDAO;

    @BeforeEach
    void setUp() {
        especialidadDAO = new EspecialidadDAO();
    }

    // Crear especialidad y verificar que se creó correctamente
    private Especialidad create(Especialidad especialidad) throws SQLException {
        Especialidad res = especialidadDAO.create(especialidad);
        assertNotNull(res);
        assertEquals(especialidad.getNombre(), res.getNombre());
        return res;
    }

    // Actualizar nombre de especialidad
    private void update(Especialidad especialidad) throws SQLException {
        especialidad.setNombre(especialidad.getNombre() + "_upd");
        boolean updated = especialidadDAO.update(especialidad);
        assertTrue(updated);
        getById(especialidad);
    }

    // Buscar por ID y comparar campos
    private void getById(Especialidad especialidad) throws SQLException {
        Especialidad res = especialidadDAO.getById(especialidad.getId());
        assertNotNull(res);
        assertEquals(especialidad.getNombre(), res.getNombre());
    }

    // Buscar por nombre parcial
    private void search(Especialidad especialidad) throws SQLException {
        ArrayList<Especialidad> resultados = especialidadDAO.search(especialidad.getNombre().substring(0, 3));
        boolean found = resultados.stream()
                .anyMatch(e -> e.getId() == especialidad.getId());
        assertTrue(found);
    }

    // Obtener todas las especialidades
    private void getAll() throws SQLException {
        ArrayList<Especialidad> todas = especialidadDAO.getAll();
        assertNotNull(todas);
        assertTrue(todas.size() >= 0); // puede estar vacía, pero no null
    }

    // Eliminar especialidad y comprobar que fue eliminada
    private void delete(Especialidad especialidad) throws SQLException {
        assertTrue(especialidadDAO.delete(especialidad.getId()));  // Pasamos  id
        Especialidad res = especialidadDAO.getById(especialidad.getId());
        assertNull(res);  // Verificamos que ya no existe
    }

    @Test
    void testEspecialidadDAO() throws SQLException {
        Random rand = new Random();
        int num = rand.nextInt(1000) + 1;
        String nombre = "Especialidad_" + num;

        Especialidad esp = new Especialidad(0, nombre);

        // Flujo completo
        Especialidad creada = create(esp);
        update(creada);
        search(creada);
        getAll();
        delete(creada);
    }

    @Test
    void crearEspecialidadEspecifica() throws SQLException {
        Especialidad esp = new Especialidad(0, "Cardiología test");
        Especialidad creada = especialidadDAO.create(esp);
        assertNotNull(creada);
    }
}
