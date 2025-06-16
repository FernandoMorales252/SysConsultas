package sysSolutions.presentacion;

import sysSolutions.dominio.Receta;
import sysSolutions.persistencia.RecetaDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel; // Usaremos DefaultTableModel
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RecetasReadingForm extends JDialog {

    private JTable tableRecetas; // La tabla de recetas
    private JButton btnCrear;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JButton btnActualizar; // Nuevo botón para actualizar lista
    private JTextField txtBuscar; // Campo de texto para la búsqueda
    private JLabel lblTitulo; // Etiqueta para el título del formulario

    private RecetaDAO recetaDAO;
    // Ya no necesitamos CitaDAO directamente aquí si Receta ya trae los nombres
    // y si las columnas de Paciente/Doctor no se muestran en esta versión de la tabla.

    private DefaultTableModel tableModel; // Usaremos DefaultTableModel

    /**
     * Constructor para RecetasReadingForm.
     *
     * @param owner El Frame padre desde donde se llama a este diálogo (JFrame o otro JDialog).
     */
    public RecetasReadingForm(Frame owner) { // Constructor adaptado a 'Frame owner'
        super(owner, "Gestión de Recetas", true); // Título y modal
        recetaDAO = new RecetaDAO();
        initComponents();
        loadAllRecetas(); // Carga inicial de todas las recetas
        initEvents();

        setSize(900, 600); // Tamaño adecuado para una tabla con más columnas
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Inicializa y organiza los componentes de la interfaz de usuario,
     * aplicando estilos similares a DoctorReadingForm.
     */
    private void initComponents() {
        setLayout(new BorderLayout()); // Establecemos el layout del JDialog directamente
        getContentPane().setBackground(Color.WHITE);

        // Panel superior para el título y la búsqueda
        JPanel topContainerPanel = new JPanel(new BorderLayout());
        topContainerPanel.setBackground(Color.WHITE);

        // Título del formulario
        lblTitulo = new JLabel("Gestión de Recetas", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 97, 140)); // Azul hospitalario
        topContainerPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel de búsqueda (similar al DoctorReadingForm)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblBuscar = new JLabel("Buscar (ID, Paciente o Doctor):");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBuscar.setForeground(new Color(55, 55, 55));

        txtBuscar = new JTextField(25); // Campo de texto para la búsqueda
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnLimpiarBusqueda = new JButton("Limpiar"); // Botón para limpiar la búsqueda
        btnLimpiarBusqueda.setBackground(new Color(108, 117, 125)); // Gris oscuro
        btnLimpiarBusqueda.setForeground(Color.WHITE);
        btnLimpiarBusqueda.setFocusPainted(false);
        btnLimpiarBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpiarBusqueda.addActionListener(e -> {
            txtBuscar.setText(""); // Limpia el campo de búsqueda
            loadAllRecetas(); // Recarga todas las recetas
        });

        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscar);
        searchPanel.add(btnLimpiarBusqueda);
        topContainerPanel.add(searchPanel, BorderLayout.CENTER);

        add(topContainerPanel, BorderLayout.NORTH);

        // Tabla de Recetas (adaptada para DefaultTableModel)
        // Columnas: ID, ID Cita, Medicamento, Dosis, Observaciones
        // Ya no se usan "Paciente" y "Doctor" en las columnas según tu request.
        String[] columnNames = {"ID", "ID Cita", "Medicamento", "Dosis", "Observaciones"};
        tableModel = new DefaultTableModel(columnNames, 0) { // Inicialización directa
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables directamente
            }
        };
        tableRecetas = new JTable(tableModel);
        tableRecetas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tableRecetas.setRowHeight(25);
        tableRecetas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableRecetas.getTableHeader().setBackground(new Color(0, 123, 255)); // Azul principal
        tableRecetas.getTableHeader().setForeground(Color.WHITE);
        tableRecetas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableRecetas.setGridColor(new Color(220, 220, 220));
        tableRecetas.setFillsViewportHeight(true);
        tableRecetas.setAutoCreateRowSorter(true); // Habilita la ordenación

        JScrollPane scrollPane = new JScrollPane(tableRecetas);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones de acción (Crear, Modificar, Eliminar, Actualizar)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        btnCrear = new JButton("Crear");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar Lista"); // Botón para recargar

        // Estilo de botones de acción
        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);

        btnCrear.setBackground(new Color(40, 167, 69)); // Verde éxito
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setFont(btnFont);

        btnModificar.setBackground(new Color(0, 123, 255)); // Azul info
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFocusPainted(false);
        btnModificar.setFont(btnFont);

        btnEliminar.setBackground(new Color(220, 53, 69)); // Rojo peligro
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(btnFont);

        btnActualizar.setBackground(new Color(23, 162, 184)); // Cian suave
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(btnFont);

        buttonPanel.add(btnCrear);
        buttonPanel.add(btnModificar);
        buttonPanel.add(btnEliminar);
        buttonPanel.add(btnActualizar);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Inicializa los listeners de eventos para los componentes de la UI.
     */
    private void initEvents() {
        // Búsqueda en tiempo real (similar a DoctorReadingForm)
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchRecetas();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                searchRecetas();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                searchRecetas();
            }
        });

        // Acciones de botones con lambdas (similar a DoctorReadingForm)
        btnCrear.addActionListener(e -> onCreateReceta());
        btnModificar.addActionListener(e -> onModifyReceta());
        btnEliminar.addActionListener(e -> onDeleteReceta());
        btnActualizar.addActionListener(e -> loadAllRecetas()); // Recargar lista completa

        // Doble clic en la tabla para modificar
        tableRecetas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    onModifyReceta(); // Llama a modificar al doble clic
                }
            }
        });
    }

    /**
     * Carga todas las recetas desde la base de datos y las muestra en la tabla.
     */
    private void loadAllRecetas() {
        try {
            ArrayList<Receta> recetas = recetaDAO.getAll();
            populateTable(recetas);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar recetas: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Realiza una búsqueda de recetas basada en el texto del campo de búsqueda.
     * Intenta buscar por ID de Receta o por nombre de Paciente/Doctor.
     */
    private void searchRecetas() {
        String filter = txtBuscar.getText().trim();
        if (filter.isEmpty()) {
            loadAllRecetas(); // Si el campo está vacío, mostrar todas
            return;
        }
        try {
            List<Receta> results;
            // Intentar buscar por ID de Receta si el texto es numérico
            if (filter.matches("\\d+")) {
                int id = Integer.parseInt(filter);
                Receta receta = recetaDAO.getById(id);
                results = new ArrayList<>();
                if (receta != null) {
                    results.add(receta);
                }
            } else {
                // Si no es un ID numérico, buscar por nombre de paciente o doctor
                results = recetaDAO.getByPacienteOrDoctor(filter);
            }
            populateTable((ArrayList<Receta>) results);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar recetas: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (NumberFormatException ex) {
            // Si el texto no es un número, simplemente lo tratamos como búsqueda de nombre
            try {
                populateTable((ArrayList<Receta>) recetaDAO.getByPacienteOrDoctor(filter));
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al buscar recetas por nombre: " + e.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    /**
     * Carga los datos de una lista de Recetas en el DefaultTableModel de la tabla.
     *
     * @param recetas La lista de objetos Receta a mostrar.
     */
    private void populateTable(ArrayList<Receta> recetas) {
        tableModel.setRowCount(0); // Limpiar todas las filas existentes
        for (Receta rec : recetas) {
            tableModel.addRow(new Object[]{
                    rec.getId(),
                    rec.getCitaId(),
                    rec.getMedicamento() != null ? rec.getMedicamento().getNombre() : "N/A",
                    rec.getDosis(),
                    rec.getObservaciones()
            });
        }
    }


    /**
     * Abre el RecetaWriteForm para crear una nueva receta.
     */
    private void onCreateReceta() {
        RecetaWriteForm writeForm = new RecetaWriteForm(this, null); // Pasamos 'this' como padre y null para nueva
        writeForm.setVisible(true);
        if (writeForm.isSaved()) {
            loadAllRecetas(); // Recargar la tabla si se guardó una nueva receta
        }
    }

    /**
     * Abre el RecetaWriteForm para modificar la receta seleccionada.
     */
    private void onModifyReceta() {
        int selectedRow = tableRecetas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una receta para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convertir la fila seleccionada en la vista a la fila en el modelo (importante si la tabla está ordenada)
        int modelRow = tableRecetas.convertRowIndexToModel(selectedRow);
        int recetaId = (int) tableModel.getValueAt(modelRow, 0); // Obtener el ID de la primera columna

        try {
            Receta receta = recetaDAO.getById(recetaId); // Obtener la receta completa por ID
            if (receta != null) {
                RecetaWriteForm writeForm = new RecetaWriteForm(this, receta); // Pasar la receta para edición
                writeForm.setVisible(true);
                if (writeForm.isSaved()) {
                    loadAllRecetas(); // Recargar la tabla si la receta fue actualizada
                }
            } else {
                JOptionPane.showMessageDialog(this, "Receta no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener receta: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Elimina la receta seleccionada de la base de datos.
     */
    private void onDeleteReceta() {
        int selectedRow = tableRecetas.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una receta para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tableRecetas.convertRowIndexToModel(selectedRow);
        int recetaId = (int) tableModel.getValueAt(modelRow, 0); // Obtener el ID de la receta

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la receta con ID: " + recetaId + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = recetaDAO.delete(recetaId); // Tu DAO ya usa el ID directamente
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Receta eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadAllRecetas(); // Recargar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la receta. Puede que ya no exista.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar receta: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
