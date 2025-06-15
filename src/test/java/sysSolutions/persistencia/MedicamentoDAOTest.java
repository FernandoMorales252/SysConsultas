package sysSolutions.persistencia;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sysSolutions.dominio.Medicamento;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Random;

class MedicamentoDAOTest {
    private MedicamentoDAO medicamentoDAO;

    @BeforeEach
    void setUp() {
        medicamentoDAO = new MedicamentoDAO();
    }

    private Medicamento create(Medicamento medicamento) throws SQLException {
        Medicamento creado = medicamentoDAO.create(medicamento);
        assertNotNull(creado);
        assertTrue(creado.getId() > 0);
        assertEquals(medicamento.getNombre(), creado.getNombre());
        return creado;
    }

    private void update(Medicamento medicamento) throws SQLException {
        medicamento.setNombre(medicamento.getNombre() + "_upd");
        medicamento.setContenido(medicamento.getContenido().add(new BigDecimal("0.5")));
        medicamento.setFechaExpiracion(medicamento.getFechaExpiracion().plusDays(30));
        boolean actualizado = medicamentoDAO.update(medicamento);
        assertTrue(actualizado);
        getById(medicamento);
    }

    private void getById(Medicamento medicamento) throws SQLException {
        Medicamento encontrado = medicamentoDAO.getById(medicamento.getId());
        assertNotNull(encontrado);
        assertEquals(medicamento.getNombre(), encontrado.getNombre());

        // ⚠️ Solución aplicada: usamos compareTo() para evitar fallo por diferencias en la escala de BigDecimal
        // ❌ Error original: assertEquals(medicamento.getContenido(), encontrado.getContenido()); // Esto fallaba si la escala era distinta
        assertEquals(0, medicamento.getContenido().compareTo(encontrado.getContenido()),
                "El contenido del medicamento debe ser numéricamente igual, sin importar la escala");
    }

    private void getAll() throws SQLException {
        ArrayList<Medicamento> lista = medicamentoDAO.getAll();
        assertNotNull(lista);
        assertTrue(lista.size() >= 0); // puede estar vacía pero no debe ser null
    }

    private void delete(Medicamento medicamento) throws SQLException {
        assertTrue(medicamentoDAO.delete(medicamento.getId()));
        Medicamento eliminado = medicamentoDAO.getById(medicamento.getId());
        assertNull(eliminado);
    }

    @Test
    void testMedicamentoDAO() throws SQLException {
        Random rand = new Random();
        int num = rand.nextInt(1000) + 1;

        Medicamento nuevo = new Medicamento(
                0,
                "Ibuprofeno_" + num,
                new BigDecimal("200.0"),
                LocalDate.now().plusMonths(6)
        );

        Medicamento creado = create(nuevo);
        update(creado);
        getAll();
        delete(creado);
    }

    @Test
    void crearMedicamentoEspecifico() throws SQLException {
        Medicamento medicamento = new Medicamento(
                0,
                "Paracetamol",
                new BigDecimal("500.0"),
                LocalDate.of(2026, 1, 1)
        );
        Medicamento creado = medicamentoDAO.create(medicamento);
        assertNotNull(creado);
    }

}