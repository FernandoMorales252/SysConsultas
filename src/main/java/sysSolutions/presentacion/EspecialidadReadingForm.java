package sysSolutions.presentacion;
import sysSolutions.dominio.Especialidad;
import sysSolutions.persistencia.EspecialidadDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class EspecialidadReadingForm extends JDialog{
    private JTextField txtBuscarPorNombre;
    private JButton btnCrear;
    private JButton btnModificar;
    private JButton btnEliminar;
    private JTable tbEspecialidadesList;

    private JLabel lblTitulo; // Título principal del formulario
    private JButton btnActualizar;


    private EspecialidadDAO especialidadDAO;
    private DefaultTableModel tableModel; // Usaremos DefaultTableModel

    public EspecialidadReadingForm(Frame owner) {
        super(owner, "Gestión de Especialidades", true); // Título y modal
        especialidadDAO = new EspecialidadDAO();
        initComponents();
        loadAllEspecialidades(); // Carga inicial de todas las especialidades
        initEvents();

        setSize(700, 400); // Tamaño adecuado para este formulario más simple
        setLocationRelativeTo(owner); // Centrar en relación a la ventana padre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Inicializa y organiza los componentes de la interfaz de usuario,
     * aplicando estilos similares a DoctorReadingForm.
     */
    private void initComponents() {
        setLayout(new BorderLayout()); // Establecemos el layout del JDialog
        getContentPane().setBackground(Color.WHITE);

        // Panel superior para el título y la búsqueda
        JPanel topContainerPanel = new JPanel(new BorderLayout());
        topContainerPanel.setBackground(Color.WHITE);

        // Título del formulario
        lblTitulo = new JLabel("Gestión de Especialidades", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setForeground(new Color(33, 97, 140)); // Azul hospitalario
        topContainerPanel.add(lblTitulo, BorderLayout.NORTH);

        // Panel de búsqueda (similar al DoctorReadingForm)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblBuscar = new JLabel("Buscar por nombre:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBuscar.setForeground(new Color(55, 55, 55));

        txtBuscarPorNombre = new JTextField(20); // Campo de texto para la búsqueda
        txtBuscarPorNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnLimpiarBusqueda = new JButton("Limpiar"); // Botón para limpiar la búsqueda
        btnLimpiarBusqueda.setBackground(new Color(108, 117, 125)); // Gris oscuro
        btnLimpiarBusqueda.setForeground(Color.WHITE);
        btnLimpiarBusqueda.setFocusPainted(false);
        btnLimpiarBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpiarBusqueda.addActionListener(e -> {
            txtBuscarPorNombre.setText(""); // Limpia el campo de búsqueda
            loadAllEspecialidades(); // Recarga todas las especialidades
        });

        searchPanel.add(lblBuscar);
        searchPanel.add(txtBuscarPorNombre);
        searchPanel.add(btnLimpiarBusqueda);
        topContainerPanel.add(searchPanel, BorderLayout.CENTER);

        add(topContainerPanel, BorderLayout.NORTH);

        // Tabla de Especialidades (adaptada para DefaultTableModel)
        // Columnas: ID, Nombre
        String[] columnNames = {"ID", "Nombre"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables directamente
            }
        };
        tbEspecialidadesList = new JTable(tableModel); // Usamos tbEspecialidadesList del esqueleto
        tbEspecialidadesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbEspecialidadesList.setRowHeight(25);
        tbEspecialidadesList.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tbEspecialidadesList.getTableHeader().setBackground(new Color(0, 123, 255)); // Azul principal
        tbEspecialidadesList.getTableHeader().setForeground(Color.WHITE);
        tbEspecialidadesList.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tbEspecialidadesList.setGridColor(new Color(220, 220, 220));
        tbEspecialidadesList.setFillsViewportHeight(true);
        tbEspecialidadesList.setAutoCreateRowSorter(true); // Habilita la ordenación

        JScrollPane scrollPane = new JScrollPane(tbEspecialidadesList);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);

        // Panel de botones de acción (Crear, Modificar, Eliminar, Actualizar)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        btnCrear = new JButton("Crear");
        btnModificar = new JButton("Modificar"); // Nombre del botón
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
        txtBuscarPorNombre.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchEspecialidades();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                searchEspecialidades();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                searchEspecialidades();
            }
        });

        // Acciones de botones
        btnCrear.addActionListener(e -> onCreateEspecialidad());
        btnModificar.addActionListener(e -> onModifyEspecialidad());
        btnEliminar.addActionListener(e -> onDeleteEspecialidad());
        btnActualizar.addActionListener(e -> loadAllEspecialidades());

        // Doble clic en la tabla para modificar
        tbEspecialidadesList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    onModifyEspecialidad(); // Llama a modificar al doble clic
                }
            }
        });
    }

    /**
     * Carga todas las especialidades desde la base de datos y las muestra en la tabla.
     */
    private void loadAllEspecialidades() {
        try {
            ArrayList<Especialidad> especialidades = especialidadDAO.getAll();
            populateTable(especialidades);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar especialidades: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Realiza una búsqueda de especialidades basada en el texto del campo de búsqueda.
     */
    private void searchEspecialidades() {
        String filter = txtBuscarPorNombre.getText().trim();
        if (filter.isEmpty()) {
            loadAllEspecialidades(); // Si el campo está vacío, mostrar todas
            return;
        }
        try {
            ArrayList<Especialidad> results = especialidadDAO.search(filter); // Usamos el método search del DAO
            populateTable(results);
            if (results.isEmpty()) {
                // Puedes comentar o dejar este JOptionPane si quieres que aparezca un aviso al no encontrar resultados
                // JOptionPane.showMessageDialog(this, "No se encontraron especialidades con el criterio de búsqueda.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar especialidades: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Carga los datos de una lista de Especialidades en el DefaultTableModel de la tabla.
     *
     * @param especialidades La lista de objetos Especialidad a mostrar.
     */
    private void populateTable(ArrayList<Especialidad> especialidades) {
        tableModel.setRowCount(0); // Limpiar todas las filas existentes
        for (Especialidad esp : especialidades) {
            tableModel.addRow(new Object[]{
                    esp.getId(),
                    esp.getNombre()
            });
        }
    }

    /**
     * Abre el EspecialidadWriteForm para crear una nueva especialidad.
     */
    private void onCreateEspecialidad() {
        EspecialidadWriteForm writeForm = new EspecialidadWriteForm(this, null); // Pasamos 'this' como padre y null
        writeForm.setVisible(true);
        if (writeForm.isSaved()) {
            loadAllEspecialidades(); // Recargar la tabla si se guardó la nueva especialidad
        }
    }

    /**
     * Abre el EspecialidadWriteForm para modificar la especialidad seleccionada.
     */
    private void onModifyEspecialidad() {
        int selectedRow = tbEspecialidadesList.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una especialidad para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convertir la fila seleccionada en la vista a la fila en el modelo (importante si la tabla está ordenada)
        int modelRow = tbEspecialidadesList.convertRowIndexToModel(selectedRow);
        int especialidadId = (int) tableModel.getValueAt(modelRow, 0); // Obtener el ID de la primera columna

        try {
            Especialidad especialidad = especialidadDAO.getById(especialidadId); // Obtener la especialidad completa por ID
            if (especialidad != null) {
                EspecialidadWriteForm writeForm = new EspecialidadWriteForm(this, especialidad); // Pasar la especialidad para edición
                writeForm.setVisible(true);
                if (writeForm.isSaved()) {
                    loadAllEspecialidades(); // Recargar la tabla si la especialidad fue actualizada
                }
            } else {
                JOptionPane.showMessageDialog(this, "Especialidad no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener especialidad: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Elimina la especialidad seleccionada de la base de datos.
     */
    private void onDeleteEspecialidad() {
        int selectedRow = tbEspecialidadesList.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una especialidad para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tbEspecialidadesList.convertRowIndexToModel(selectedRow);
        int especialidadId = (int) tableModel.getValueAt(modelRow, 0); // Obtener el ID de la especialidad

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar la especialidad con ID: " + especialidadId + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = especialidadDAO.delete(especialidadId); // Tu DAO ya usa el ID directamente
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Especialidad eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadAllEspecialidades(); // Recargar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar la especialidad. Puede que esté en uso o no exista.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar especialidad: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
