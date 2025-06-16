package sysSolutions.presentacion;

import sysSolutions.dominio.Medicamento;
import sysSolutions.persistencia.MedicamentoDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel; // Asegurarse de que este import esté correcto
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MedicamentoReadingForm extends JDialog {
    private JTable tbMedicamentos; // La tabla de medicamentos
    private JButton btnCrear;
    private JButton btnEliminar;
    private JButton btnModificar; // Corresponde a btnEditar en DoctorReadingForm
    private JTextField txtMedi; // txt para buscar por nombre, renombrado a txtBuscar
    private JLabel lblNM; // buscar por nombre
    private JLabel titulo; // Etiqueta para el título principal
    // Eliminamos panelMedi y mainPanel del esqueleto, ya que el JDialog se usa directamente como contenedor principal

    private JButton btnActualizar; // Botón para recargar la lista

    private MedicamentoDAO medicamentoDAO;
    private DefaultTableModel tableModel;

    /**
     * Constructor para MedicamentoReadingForm.
     *
     * @param owner El Frame padre desde donde se llama a este diálogo (por ejemplo, tu MainForm).
     */
    public MedicamentoReadingForm(Frame owner) {
        super(owner, "Gestión de Medicamentos", true); // Título y modal
        medicamentoDAO = new MedicamentoDAO();
        initComponents();
        loadAllMedicamentos(); // Carga inicial de todos los medicamentos
        initEvents();

        setSize(800, 500); // Tamaño adecuado para la tabla
        setLocationRelativeTo(owner);
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

        // Título del formulario (usando la variable 'titulo' de tu esqueleto)
        titulo = new JLabel("Gestión de Medicamentos", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(33, 97, 140)); // Azul hospitalario
        topContainerPanel.add(titulo, BorderLayout.NORTH);

        // Panel de búsqueda (similar al DoctorReadingForm)
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Etiqueta para el campo de búsqueda (usando lblNM de tu esqueleto)
        lblNM = new JLabel("Buscar por nombre:");
        lblNM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNM.setForeground(new Color(55, 55, 55));

        // Campo de texto para la búsqueda (usando txtMedi de tu esqueleto)
        txtMedi = new JTextField(20);
        txtMedi.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnLimpiarBusqueda = new JButton("Limpiar"); // Botón para limpiar la búsqueda
        btnLimpiarBusqueda.setBackground(new Color(108, 117, 125)); // Gris oscuro
        btnLimpiarBusqueda.setForeground(Color.WHITE);
        btnLimpiarBusqueda.setFocusPainted(false);
        btnLimpiarBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpiarBusqueda.addActionListener(e -> {
            txtMedi.setText(""); // Limpia el campo de búsqueda
            loadAllMedicamentos(); // Recarga todos los medicamentos
        });

        searchPanel.add(lblNM); // Añade la etiqueta
        searchPanel.add(txtMedi); // CORRECCIÓN: Ahora añade el JTextField 'txtMedi'
        searchPanel.add(btnLimpiarBusqueda); // Añade el botón de limpiar
        topContainerPanel.add(searchPanel, BorderLayout.CENTER);

        add(topContainerPanel, BorderLayout.NORTH);

        // Tabla de Medicamentos (adaptada para DefaultTableModel)
        // Columnas: ID, Nombre, Contenido, Fecha Expiración
        String[] columnNames = {"ID", "Nombre", "Contenido", "Fecha Expiración"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Las celdas no son editables directamente
            }
        };
        tbMedicamentos = new JTable(tableModel); // Usamos tbMedicamentos del esqueleto
        tbMedicamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbMedicamentos.setRowHeight(25);
        tbMedicamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tbMedicamentos.getTableHeader().setBackground(new Color(0, 123, 255)); // Azul principal
        tbMedicamentos.getTableHeader().setForeground(Color.WHITE);
        tbMedicamentos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tbMedicamentos.setGridColor(new Color(220, 220, 220));
        tbMedicamentos.setFillsViewportHeight(true);
        tbMedicamentos.setAutoCreateRowSorter(true); // Habilita la ordenación

        JScrollPane scrollPane = new JScrollPane(tbMedicamentos);
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
        txtMedi.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                searchMedicamentos();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                searchMedicamentos();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                searchMedicamentos();
            }
        });

        // Acciones de botones
        btnCrear.addActionListener(e -> onCreateMedicamento());
        btnModificar.addActionListener(e -> onModifyMedicamento());
        btnEliminar.addActionListener(e -> onDeleteMedicamento());
        btnActualizar.addActionListener(e -> loadAllMedicamentos());

        // Doble clic en la tabla para modificar
        tbMedicamentos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    onModifyMedicamento(); // Llama a modificar al doble clic
                }
            }
        });
    }

    /**
     * Carga todos los medicamentos desde la base de datos y los muestra en la tabla.
     */
    private void loadAllMedicamentos() {
        try {
            ArrayList<Medicamento> medicamentos = medicamentoDAO.getAll();
            populateTable(medicamentos);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar medicamentos: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Realiza una búsqueda de medicamentos basada en el texto del campo de búsqueda (por nombre).
     */
    private void searchMedicamentos() {
        String filter = txtMedi.getText().trim();
        if (filter.isEmpty()) {
            loadAllMedicamentos(); // Si el campo está vacío, mostrar todos
            return;
        }
        try {
            ArrayList<Medicamento> results = medicamentoDAO.search(filter); // Usamos el nuevo método search del DAO
            populateTable(results);
            if (results.isEmpty()) {
                // Puedes comentar o dejar este JOptionPane si quieres que aparezca un aviso al no encontrar resultados
                // JOptionPane.showMessageDialog(this, "No se encontraron medicamentos con el criterio de búsqueda.", "Sin Resultados", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al buscar medicamentos: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Carga los datos de una lista de Medicamentos en el DefaultTableModel de la tabla.
     *
     * @param medicamentos La lista de objetos Medicamento a mostrar.
     */
    private void populateTable(ArrayList<Medicamento> medicamentos) {
        tableModel.setRowCount(0); // Limpiar todas las filas existentes
        for (Medicamento med : medicamentos) {
            tableModel.addRow(new Object[]{
                    med.getId(),
                    med.getNombre(),
                    med.getContenido(),
                    med.getFechaExpiracion()
            });
        }
    }

    /**
     * Abre el MedicamentoWriteForm para crear un nuevo medicamento.
     */
    private void onCreateMedicamento() {
        MedicamentoWriteForm writeForm = new MedicamentoWriteForm(this, null); // Pasamos 'this' como padre y null
        writeForm.setVisible(true);
        if (writeForm.isSaved()) {
            loadAllMedicamentos(); // Recargar la tabla si se guardó el nuevo medicamento
        }
    }

    /**
     * Abre el MedicamentoWriteForm para modificar el medicamento seleccionado.
     */
    private void onModifyMedicamento() {
        int selectedRow = tbMedicamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento para modificar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Convertir la fila seleccionada en la vista a la fila en el modelo (importante si la tabla está ordenada)
        int modelRow = tbMedicamentos.convertRowIndexToModel(selectedRow);
        int medicamentoId = (int) tableModel.getValueAt(modelRow, 0); // Obtener el ID de la primera columna

        try {
            Medicamento medicamento = medicamentoDAO.getById(medicamentoId); // Obtener el medicamento completo por ID
            if (medicamento != null) {
                MedicamentoWriteForm writeForm = new MedicamentoWriteForm(this, medicamento); // Pasar el medicamento para edición
                writeForm.setVisible(true);
                if (writeForm.isSaved()) {
                    loadAllMedicamentos(); // Recargar la tabla si el medicamento fue actualizado
                }
            } else {
                JOptionPane.showMessageDialog(this, "Medicamento no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener medicamento: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Elimina el medicamento seleccionado de la base de datos.
     */
    private void onDeleteMedicamento() {
        int selectedRow = tbMedicamentos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tbMedicamentos.convertRowIndexToModel(selectedRow);
        int medicamentoId = (int) tableModel.getValueAt(modelRow, 0); // Obtener el ID del medicamento

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el medicamento con ID: " + medicamentoId + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = medicamentoDAO.delete(medicamentoId); // Tu DAO ya usa el ID directamente
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Medicamento eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadAllMedicamentos(); // Recargar la tabla
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo eliminar el medicamento. Puede que esté en uso o no exista.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar medicamento: " + ex.getMessage(), "Error DB", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }
}
