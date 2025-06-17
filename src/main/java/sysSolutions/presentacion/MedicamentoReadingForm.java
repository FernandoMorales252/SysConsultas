package sysSolutions.presentacion;

import sysSolutions.dominio.Medicamento;
import sysSolutions.persistencia.MedicamentoDAO;

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

public class MedicamentoReadingForm extends JDialog {
    private JTable tbMedicamentos; // La tabla de medicamentos
    private JButton btnCrear;// Botón para crear un nuevo medicamento
    private JButton btnEliminar;// Botón para eliminar un medicamento
    private JButton btnModificar; // Botón para modificar un medicamento
    private JTextField txtMedi; // Campo de búsqueda por nombre del medicamento
    private JLabel lblNM; // Etiqueta para el campo de búsqueda por nombre
    private JLabel titulo; // Título de la ventana


    private JButton btnActualizar;

    private MedicamentoDAO medicamentoDAO;
    private DefaultTableModel tableModel;

    /**
     * Constructor para MedicamentoReadingForm.
     *
     * @param owner El Frame padre desde donde se llama a este diálogo (por ejemplo, tu MainForm).
     */
    public MedicamentoReadingForm(Frame owner) {
        super(owner, "Gestión de Medicamentos", true);
        medicamentoDAO = new MedicamentoDAO();
        initComponents();
        loadAllMedicamentos();
        initEvents();

        setSize(800, 500);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Inicializa y organiza los componentes de la interfaz de usuario,
     * aplicando estilos similares a DoctorReadingForm.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Panel superior para el título y la búsqueda
        JPanel topContainerPanel = new JPanel(new BorderLayout());
        topContainerPanel.setBackground(Color.WHITE);


        titulo = new JLabel("Gestión de Medicamentos", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titulo.setForeground(new Color(33, 97, 140)); // Azul hospitalario
        topContainerPanel.add(titulo, BorderLayout.NORTH);


        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));


        lblNM = new JLabel("Buscar por nombre:");
        lblNM.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNM.setForeground(new Color(55, 55, 55));

        txtMedi = new JTextField(20);
        txtMedi.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton btnLimpiarBusqueda = new JButton("Limpiar");
        btnLimpiarBusqueda.setBackground(new Color(108, 117, 125));
        btnLimpiarBusqueda.setForeground(Color.WHITE);
        btnLimpiarBusqueda.setFocusPainted(false);
        btnLimpiarBusqueda.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnLimpiarBusqueda.addActionListener(e -> {
            txtMedi.setText("");
            loadAllMedicamentos();
        });

        searchPanel.add(lblNM);
        searchPanel.add(txtMedi);
        searchPanel.add(btnLimpiarBusqueda);
        topContainerPanel.add(searchPanel, BorderLayout.CENTER);

        add(topContainerPanel, BorderLayout.NORTH);


        // Columnas: ID, Nombre, Contenido, Fecha Expiración
        String[] columnNames = {"ID", "Nombre", "Contenido", "Fecha Expiración"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tbMedicamentos = new JTable(tableModel);
        tbMedicamentos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tbMedicamentos.setRowHeight(25);
        tbMedicamentos.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tbMedicamentos.getTableHeader().setBackground(new Color(0, 123, 255)); // Azul principal
        tbMedicamentos.getTableHeader().setForeground(Color.WHITE);
        tbMedicamentos.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tbMedicamentos.setGridColor(new Color(220, 220, 220));
        tbMedicamentos.setFillsViewportHeight(true);
        tbMedicamentos.setAutoCreateRowSorter(true);

        JScrollPane scrollPane = new JScrollPane(tbMedicamentos);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        add(scrollPane, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        btnCrear = new JButton("Crear");
        btnModificar = new JButton("Modificar");
        btnEliminar = new JButton("Eliminar");
        btnActualizar = new JButton("Actualizar Lista");

        // Estilo de botones de acción
        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);

        btnCrear.setBackground(new Color(40, 167, 69));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setFont(btnFont);

        btnModificar.setBackground(new Color(0, 123, 255));
        btnModificar.setForeground(Color.WHITE);
        btnModificar.setFocusPainted(false);
        btnModificar.setFont(btnFont);

        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(btnFont);

        btnActualizar.setBackground(new Color(23, 162, 184));
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
                    onModifyMedicamento();
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
            loadAllMedicamentos();
            return;
        }
        try {
            ArrayList<Medicamento> results = medicamentoDAO.search(filter);
            populateTable(results);
            if (results.isEmpty()) {}
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
        tableModel.setRowCount(0);
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
            loadAllMedicamentos();
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
        int modelRow = tbMedicamentos.convertRowIndexToModel(selectedRow);
        int medicamentoId = (int) tableModel.getValueAt(modelRow, 0);

        try {
            Medicamento medicamento = medicamentoDAO.getById(medicamentoId);
            if (medicamento != null) {
                MedicamentoWriteForm writeForm = new MedicamentoWriteForm(this, medicamento);
                writeForm.setVisible(true);
                if (writeForm.isSaved()) {
                    loadAllMedicamentos();
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
        int medicamentoId = (int) tableModel.getValueAt(modelRow, 0);

        int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar el medicamento con ID: " + medicamentoId + "?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                boolean deleted = medicamentoDAO.delete(medicamentoId);
                if (deleted) {
                    JOptionPane.showMessageDialog(this, "Medicamento eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    loadAllMedicamentos();
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
