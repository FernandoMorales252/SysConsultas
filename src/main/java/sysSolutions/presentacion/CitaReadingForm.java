package sysSolutions.presentacion;
import sysSolutions.dominio.Cita;
import sysSolutions.persistencia.CitaDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class CitaReadingForm extends JDialog {
    private JTable table1;
    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnEditar;
    private JTextField txtBuscar;

    private CitaDAO citaDAO;
    private DefaultTableModel tableModel;

    /**
     * Constructor de la clase CitaReadingForm.
     * Inicializa el formulario de gestión de citas.
     *
     * @param owner Ventana padre del diálogo.
     */
    public CitaReadingForm(Frame owner) {
        super(owner, "Gestión de Citas", true);
        citaDAO = new CitaDAO();
        initComponents();
        cargarCitas();
        initEvents();

        setSize(800, 400);
        setLocationRelativeTo(owner);
    }

    /**
     * Método para inicializar los componentes de la interfaz gráfica.
     * Configura el diseño, los paneles, las tablas y los botones.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelBuscar.setBackground(Color.WHITE);
        panelBuscar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel lblBuscar = new JLabel("Buscar por nombre:");
        lblBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblBuscar.setForeground(new Color(55, 55, 55));

        txtBuscar = new JTextField(20);
        txtBuscar.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        panelBuscar.add(lblBuscar);
        panelBuscar.add(txtBuscar);
        add(panelBuscar, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Paciente", "Doctor", "Fecha", "Hora", "Motivo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table1 = new JTable(tableModel);
        table1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table1.getTableHeader().setBackground(new Color(0, 123, 255));
        table1.getTableHeader().setForeground(Color.WHITE);
        table1.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table1.setRowHeight(25);
        add(new JScrollPane(table1), BorderLayout.CENTER);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        btnCrear = new JButton("Crear");
        btnEditar = new JButton("Editar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);

        btnCrear.setBackground(new Color(0, 123, 255));
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setFont(btnFont);

        btnEditar.setBackground(new Color(0, 123, 255));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setFont(btnFont);

        btnActualizar.setBackground(new Color(23, 162, 184));
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(btnFont);

        btnEliminar.setBackground(new Color(220, 53, 69));
        btnEliminar.setForeground(Color.WHITE);
        btnEliminar.setFocusPainted(false);
        btnEliminar.setFont(btnFont);

        panelBotones.add(btnCrear);
        panelBotones.add(btnEditar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);

        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Método para inicializar los eventos de los componentes.
     * Configura los listeners para los botones y el campo de búsqueda.
     */
    private void initEvents() {
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarCitas();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarCitas();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarCitas();
            }
        });

        btnCrear.addActionListener(e -> {
            CitaWriteForm form = new CitaWriteForm(this, null);
            form.setVisible(true);
            if (form.isSaved()) {
                cargarCitas();
            }
        });

        btnEditar.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una cita para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int citaId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                Cita cita = citaDAO.getById(citaId);
                if (cita != null) {
                    CitaWriteForm form = new CitaWriteForm(this, cita);
                    form.setVisible(true);
                    if (form.isSaved()) {
                        cargarCitas();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Cita no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al obtener cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnActualizar.addActionListener(e -> cargarCitas());

        btnEliminar.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione una cita para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int citaId = (int) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar esta cita?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Cita cita = citaDAO.getById(citaId);
                    if (cita != null) {
                        boolean eliminado = citaDAO.delete(cita);
                        if (eliminado) {
                            JOptionPane.showMessageDialog(this, "Cita eliminada correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            cargarCitas();
                        } else {
                            JOptionPane.showMessageDialog(this, "No se pudo eliminar la cita.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Cita no encontrada.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar cita: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Método para cargar todas las citas desde la base de datos y mostrarlas en la tabla.
     * Maneja excepciones y muestra mensajes de error si ocurre algún problema.
     */
    private void cargarCitas() {
        try {
            ArrayList<Cita> citas = citaDAO.getAll();
            cargarTabla(citas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar citas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Método para buscar citas por nombre de paciente o doctor.
     * Actualiza la tabla con los resultados de la búsqueda.
     */
    private void buscarCitas() {
        String filtro = txtBuscar.getText().trim();
        if (filtro.isEmpty()) {
            cargarCitas();
            return;
        }

        try {
            ArrayList<Cita> citas = citaDAO.searchBYName(filtro);
            cargarTabla(citas);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar citas: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    /**
     * Método para cargar los datos de las citas en la tabla.
     * Limpia la tabla y agrega las filas correspondientes a cada cita.
     *
     * @param citas Lista de citas a mostrar en la tabla.
     */
    private void cargarTabla(ArrayList<Cita> citas) {
        tableModel.setRowCount(0);
        for (Cita cita : citas) {
            tableModel.addRow(new Object[]{
                    cita.getId(),
                    cita.getPacienteNombre(),
                    cita.getDoctorNombre(),
                    cita.getFecha(),
                    cita.getHora(),
                    cita.getMotivo()
            });
        }
    }
}
