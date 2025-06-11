package sysSolutions.persistencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sysSolutions.dominio.Doctor;
import sysSolutions.dominio.Especialidad;



import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;


import static org.junit.jupiter.api.Assertions.*;

class DoctorDAOTest {

    private DoctorDAO doctorDAO;
    private EspecialidadDAO especialidadDAO;

    // Método auxiliar para crear una especialidad
    @BeforeEach
    void setUp() {
        doctorDAO = new DoctorDAO();
        especialidadDAO = new EspecialidadDAO();
    }

    // Método para crear un doctor y verificarlo
    private Doctor createDoctor(Doctor doctor) throws SQLException {
        Doctor res = doctorDAO.create(doctor);
        assertNotNull(res);
        assertEquals(doctor.getNombre(), res.getNombre());
        assertEquals(doctor.getContacto(), res.getContacto());
        assertEquals(doctor.getEspecialidad().getId(), res.getEspecialidad().getId());
        return res;
    }

    // Método para actualizar un doctor
    private void updateDoctor(Doctor doctor) throws SQLException {
        doctor.setNombre(doctor.getNombre() + "_ACT");
        doctor.setContacto("nuevo_contacto_" + new Random().nextInt(1000));
        boolean updated = doctorDAO.update(doctor);
        assertTrue(updated);
        getByIdDoctor(doctor); // Verifica que la actualización se refleje en la BD
    }

    // Método para obtener un doctor por ID y verificar
    private void getByIdDoctor(Doctor doctor) throws SQLException {
        Doctor res = doctorDAO.getById(doctor.getId());
        assertNotNull(res);
        assertEquals(doctor.getNombre(), res.getNombre());
        assertEquals(doctor.getContacto(), res.getContacto());
        assertEquals(doctor.getEspecialidad().getId(), res.getEspecialidad().getId());
    }

    // Método para eliminar un doctor
    private void deleteDoctor(Doctor doctor) throws SQLException {
      assertTrue(doctorDAO.delete(doctor));
      assertNull(doctorDAO.getById(doctor.getId()));
    }

    // Método para buscar por nombre
    private void search(Doctor doctor) throws SQLException {
        ArrayList<Doctor>  resultados = doctorDAO.searchByName(doctor.getNombre().substring(0,3));
        boolean found = resultados.stream().anyMatch(d -> d.getId() == doctor.getId());
        assertTrue(found, "El doctor '" + doctor.getNombre() + "' no se encontró en la búsqueda.");
    }

    //Método para obtener todos los doctores
    private void getAllDoctors() throws SQLException {
        ArrayList<Doctor> todos = doctorDAO.getAll();
        assertNotNull(todos);
        assertTrue(todos.size() > 0, "La lista de doctores no debe estar vacía");
    }


    @Test
    void testDoctorDAOcompleto() throws SQLException {
        Especialidad especialidad = especialidadDAO.getAll().get(0);
        assertNotNull(especialidad, "Debe haber al menos una especialidad en la base de datos para realizar esta prueba");
     Random random = new Random();
     int num = random.nextInt(1000) ;
      String nombreDoctor = "Doctor_" + num;

      Doctor doctor = new Doctor(0, nombreDoctor ,  especialidad, "contacto_" + num);

      System.out.println("Creando doctor: " + doctor.getNombre());
      doctor = createDoctor(doctor);

        System.out.println("Actualizando doctor..." );
        updateDoctor(doctor);

        System.out.println("Obteniendo doctor por nombre..");
        search(doctor);

        System.out.println("Obteniendo todos los doctores...");
        getAllDoctors();

        System.out.println("Eliminando doctor...");
        deleteDoctor(doctor);




    }



}