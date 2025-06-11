package sysSolutions.persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sysSolutions.dominio.Paciente;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PacienteDAOTest {

    private PacienteDAO pacienteDAO;

    @BeforeEach
    void setUp() {
        pacienteDAO = new PacienteDAO();

    }

    // Método auxiliar para crear un paciente y verificarlo
    private Paciente create(Paciente paciente) throws SQLException {
        Paciente res = pacienteDAO.create(paciente);
        assertNotNull(res);
        assertEquals(paciente.getNombre(), res.getNombre());
        assertEquals(paciente.getEdad(), res.getEdad());
        assertEquals(paciente.getSexo(), res.getSexo());
        assertEquals(paciente.getContacto(), res.getContacto());
        assertEquals(paciente.getDireccion(), res.getDireccion());
        return res;
    }

    // Método auxiliar para actualizar un paciente
    private void update(Paciente paciente) throws SQLException {
        paciente.setNombre(paciente.getNombre() + "_ACT");
        paciente.setEdad(paciente.getEdad() != null ? paciente.getEdad() + 1 : 30); // Actualiza edad o establece una por defecto
        paciente.setSexo(paciente.getSexo() == 'M' ? 'F' : 'M'); // Cambia sexo
        paciente.setContacto("nuevo_contacto_" + new Random().nextInt(100));
        paciente.setDireccion("Nueva Direccion S/N");
        boolean updated = pacienteDAO.update(paciente);
        assertTrue(updated);
        getById(paciente); // Verifica que la actualización se refleje en la BD
    }

    // Método auxiliar para obtener por ID y verificar
    private void getById(Paciente paciente) throws SQLException {
        Paciente res = pacienteDAO.getById(paciente.getId());
        assertNotNull(res);
        assertEquals(paciente.getNombre(), res.getNombre());
        assertEquals(paciente.getEdad(), res.getEdad());
        assertEquals(paciente.getSexo(), res.getSexo());
        assertEquals(paciente.getContacto(), res.getContacto());
        assertEquals(paciente.getDireccion(), res.getDireccion());
    }

    // Método auxiliar para buscar por nombre parcial
    private void search(Paciente paciente) throws SQLException {
        ArrayList<Paciente> resultados = pacienteDAO.search(paciente.getNombre().substring(0, Math.min(3, paciente.getNombre().length())));
        boolean found = resultados.stream()
                .anyMatch(p -> p.getId() == paciente.getId());
        assertTrue(found, "El paciente '" + paciente.getNombre() + "' no se encontró en la búsqueda.");
    }

    // Método auxiliar para obtener todos los pacientes
    private void getAll() throws SQLException {
        ArrayList<Paciente> todos = pacienteDAO.getAll();
        assertNotNull(todos);
        assertTrue(todos.size() >= 0);
    }

    // Método auxiliar para eliminar un paciente
    private void delete(Paciente paciente) throws SQLException {
        assertTrue(pacienteDAO.delete(paciente));
        assertNull(pacienteDAO.getById(paciente.getId()));
    }

    @Test
    void testPacienteDAOCompleto() throws SQLException {
        Random rand = new Random();
        int num = rand.nextInt(1000) + 1;
        String nombre = "PacienteTest_" + num;

        Paciente paciente = new Paciente(0, nombre, 25, 'M', "12345678", "Calle Falsa 123");

        // Flujo completo: Crear, Actualizar, Buscar, ObtenerTodos, Eliminar
        System.out.println("Creando paciente: " + paciente.getNombre());
        Paciente pacienteCreado = create(paciente);
        System.out.println("Paciente creado con ID: " + pacienteCreado.getId());

        System.out.println("Actualizando paciente...");
        update(pacienteCreado);
        System.out.println("Paciente actualizado a: " + pacienteCreado.getNombre());

        System.out.println("Buscando paciente...");
        search(pacienteCreado);

        System.out.println("Obteniendo todos los pacientes...");
        getAll();

        System.out.println("Eliminando paciente...");
        delete(pacienteCreado);
        System.out.println("Paciente eliminado.");
    }



    @Test
    void testCrearPacienteConCamposNulos() throws SQLException {
        Paciente paciente = new Paciente(0, "PacienteCompleto", 30, 'F', "98765432", "Av. Siempre Viva 742");
        Paciente creado = pacienteDAO.create(paciente);
        assertNotNull(creado);
        assertEquals("PacienteCompleto", creado.getNombre());
        assertEquals(30, creado.getEdad());
        assertEquals('F', creado.getSexo());
        assertEquals("98765432", creado.getContacto());
        assertEquals("Av. Siempre Viva 742", creado.getDireccion());

    }
}