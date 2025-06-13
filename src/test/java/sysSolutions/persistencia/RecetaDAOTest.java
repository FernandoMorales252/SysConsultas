package sysSolutions.persistencia;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sysSolutions.dominio.Cita;
import sysSolutions.dominio.Doctor;
import sysSolutions.dominio.Paciente;
import sysSolutions.dominio.Receta;


import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class RecetaDAOTest {
    private RecetaDAO recetaDAO;
    private CitaDAO citaDAO;
    private DoctorDAO doctorDAO;
    private PacienteDAO pacienteDAO;

    @BeforeEach
    void setUp() {
        recetaDAO = new RecetaDAO();
        citaDAO = new CitaDAO();
        doctorDAO = new DoctorDAO();
        pacienteDAO = new PacienteDAO();
    }

    private Receta createReceta(Receta receta) throws SQLException {
        Receta res = recetaDAO.create(receta);
        assertNotNull(res);
        assertEquals(receta.getCitaId(), res.getCitaId());
        assertEquals(receta.getMedicamento(), res.getMedicamento());
        assertEquals(receta.getDosis(), res.getDosis());
        assertEquals(receta.getObservaciones(), res.getObservaciones());
        return res;
    }

    private void updateReceta(Receta receta) throws SQLException {
        receta.setObservaciones("Observación modificada");
        boolean updated = recetaDAO.update(receta);
        assertTrue(updated);
        getByIdReceta(receta);
    }

    private void getByIdReceta(Receta receta) throws SQLException {
        Receta res = recetaDAO.getById(receta.getId());
        assertNotNull(res);
        assertEquals(receta.getObservaciones(), res.getObservaciones());
    }

    private void deleteReceta(Receta receta) throws SQLException {
        assertTrue(recetaDAO.delete(receta));
        assertNull(recetaDAO.getById(receta.getId()));
    }

    private void getAllRecetas() throws SQLException {
        ArrayList<Receta> todas = recetaDAO.getAll();
        assertNotNull(todas);
        assertTrue(todas.size() > 0, "La lista de recetas no debe estar vacía");
    }

    private void searchRecetaPorNombre(String nombre) throws SQLException {
        ArrayList<Receta> recetas = recetaDAO.getByPacienteOrDoctor(nombre);
        assertNotNull(recetas);
        assertTrue(recetas.size() > 0, "No se encontraron recetas para el nombre: " + nombre);
    }

    @Test
    void testRecetaDAOcompleto() throws SQLException {
        // Preparar cita con paciente y doctor válidos
        Doctor doctor = doctorDAO.getAll().get(0);
        Paciente paciente = pacienteDAO.getAll().get(0);

        assertNotNull(doctor);
        assertNotNull(paciente);

        LocalDate fecha = LocalDate.now().plusDays(2);
        LocalTime hora = LocalTime.of(11, 0);

        Cita cita = new Cita(0, paciente.getId(), paciente.getNombre(),
                doctor.getId(), doctor.getNombre(),
                fecha, hora, "Revisión anual");

        cita = citaDAO.create(cita);
        assertNotNull(cita);

        Receta receta = new Receta(0, cita.getId(), "Ibuprofeno", "1 cada 8h", "Tomar después de comer");

        System.out.println("Creando receta...");
        receta = createReceta(receta);

        System.out.println("Actualizando receta...");
        updateReceta(receta);

        System.out.println("Obteniendo receta por ID...");
        getByIdReceta(receta);

        System.out.println("Listando todas las recetas...");
        getAllRecetas();

        System.out.println("Buscando receta por paciente o doctor...");
        searchRecetaPorNombre(paciente.getNombre().substring(0, 3));

        System.out.println("Eliminando receta...");
        deleteReceta(receta);
    }

}