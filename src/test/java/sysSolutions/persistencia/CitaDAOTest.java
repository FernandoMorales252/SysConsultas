package sysSolutions.persistencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sysSolutions.dominio.Cita;
import sysSolutions.dominio.Doctor;
import sysSolutions.dominio.Paciente;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Random;


import static org.junit.jupiter.api.Assertions.*;

class CitaDAOTest {
    private CitaDAO citaDAO;
    private DoctorDAO doctorDAO;
    private PacienteDAO pacienteDAO;

    @BeforeEach
    void setUp() {
        citaDAO = new CitaDAO();
        doctorDAO = new DoctorDAO();
        pacienteDAO = new PacienteDAO();
    }

    private Cita createCita(Cita cita) throws SQLException {
        Cita res = citaDAO.create(cita);
        assertNotNull(res);
        assertEquals(cita.getPacienteId(), res.getPacienteId());
        assertEquals(cita.getDoctorId(), res.getDoctorId());
        assertEquals(cita.getFecha(), res.getFecha());
        assertEquals(cita.getHora(), res.getHora());
        assertEquals(cita.getMotivo(), res.getMotivo());
        return res;
    }

    private void updateCita(Cita cita) throws SQLException {
        cita.setMotivo("Motivo actualizado");
        boolean updated = citaDAO.update(cita);
        assertTrue(updated);
        getByIdCita(cita);
    }

    private void getByIdCita(Cita cita) throws SQLException {
        Cita res = citaDAO.getById(cita.getId());
        assertNotNull(res);
        assertEquals(cita.getMotivo(), res.getMotivo());
        assertEquals(cita.getFecha(), res.getFecha());
        assertEquals(cita.getHora(), res.getHora());
    }

    private void deleteCita(Cita cita) throws SQLException {
        assertTrue(citaDAO.delete(cita));
        assertNull(citaDAO.getById(cita.getId()));
    }

    private void search(Cita cita) throws SQLException {
        ArrayList<Cita> resultados = citaDAO.searchBYName(cita.getPacienteNombre().substring(0, 3));
        boolean found = resultados.stream().anyMatch(c -> c.getId() == cita.getId());
        assertTrue(found, "La cita con paciente '" + cita.getPacienteNombre() + "' no se encontró en la búsqueda.");
    }

    private void getAllCitas() throws SQLException {
        ArrayList<Cita> todas = citaDAO.getAll();
        assertNotNull(todas);
        assertTrue(todas.size() > 0, "La lista de citas no debe estar vacía");
    }

    @Test
    void testCitaDAOcompleto() throws SQLException {
        // Obtener un doctor y un paciente reales de la base de datos
        Doctor doctor = doctorDAO.getAll().get(0);
        Paciente paciente = pacienteDAO.getAll().get(0);

        assertNotNull(doctor);
        assertNotNull(paciente);

        LocalDate fecha = LocalDate.now().plusDays(1);
        LocalTime hora = LocalTime.of(10, 30);

        Cita cita = new Cita(0, paciente.getId(), paciente.getNombre(),
                doctor.getId(), doctor.getNombre(),
                fecha, hora, "Consulta general");

        System.out.println("Creando cita...");
        cita = createCita(cita);

        System.out.println("Actualizando cita...");
        updateCita(cita);

        System.out.println("Buscando cita por paciente...");
        search(cita);

        System.out.println("Listando todas las citas...");
        getAllCitas();

        System.out.println("Eliminando cita...");
        deleteCita(cita);
    }

}