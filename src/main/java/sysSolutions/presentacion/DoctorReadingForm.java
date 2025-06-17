package sysSolutions.presentacion;
import sysSolutions.dominio.Doctor;
import sysSolutions.persistencia.DoctorDAO;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class DoctorReadingForm extends JDialog {
    private JTable table1;
    private JButton btnCrear;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnEditar;
    private JTextField txtBuscar;

    private DoctorDAO doctorDAO;

    private DefaultTableModel tableModel;

    /**
     * Constructor de la clase DoctorReadingForm.
     * Inicializa el formulario para gestionar doctores.
     *
     * @param owner Ventana padre del diálogo.
     */
    public DoctorReadingForm(Frame owner) {
        super(owner, "Gestión de Doctores", true);
        doctorDAO = new DoctorDAO();
        initComponents();
        cargarDoctores();
        initEvents();

        setSize(700, 400);
        setLocationRelativeTo(owner);
    }

    /**
     * Inicializa los componentes de la interfaz gráfica.
     * Configura el diseño, colores, fuentes y eventos de los componentes.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Panel superior: etiqueta y campo búsqueda con estilo
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

        // Tabla con modelo personalizado y estilos
        tableModel = new DefaultTableModel(new Object[]{"ID", "Nombre", "Especialidad", "Contacto"}, 0) {
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

        // Panel botones con colores y espaciado
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        btnCrear = new JButton("Crear");
        btnEditar = new JButton("Editar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");

        // Estilos botones
        Font btnFont = new Font("Segoe UI", Font.BOLD, 14);

        btnCrear.setBackground(new Color(0, 123, 255)); // azul
        btnCrear.setForeground(Color.WHITE);
        btnCrear.setFocusPainted(false);
        btnCrear.setFont(btnFont);

        btnEditar.setBackground(new Color(0, 123, 255));
        btnEditar.setForeground(Color.WHITE);
        btnEditar.setFocusPainted(false);
        btnEditar.setFont(btnFont);

        btnActualizar.setBackground(new Color(23, 162, 184)); // cian suave
        btnActualizar.setForeground(Color.WHITE);
        btnActualizar.setFocusPainted(false);
        btnActualizar.setFont(btnFont);

        btnEliminar.setBackground(new Color(220, 53, 69)); // rojo suave
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
     * Inicializa los eventos de los componentes.
     * Configura los listeners para manejar acciones como búsqueda, creación, edición y eliminación de doctores.
     */
    private void initEvents() {
        // Búsqueda en tiempo real
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                buscarDoctores();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                buscarDoctores();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                buscarDoctores();
            }
        });

        // Crear nuevo doctor
        btnCrear.addActionListener(e -> {
            DoctorWriteForm form = new DoctorWriteForm(this, null);
            form.setVisible(true);
            if (form.isSaved()) {
                cargarDoctores();
            }
        });

        // Editar doctor seleccionado
        btnEditar.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un doctor para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int doctorId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                Doctor doctor = doctorDAO.getById(doctorId);
                if (doctor != null) {
                    DoctorWriteForm form = new DoctorWriteForm(this, doctor);
                    form.setVisible(true);
                    if (form.isSaved()) {
                        cargarDoctores();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Doctor no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al obtener doctor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Recargar lista completa
        btnActualizar.addActionListener(e -> cargarDoctores());

        // Eliminar doctor con confirmación
        btnEliminar.addActionListener(e -> {
            int selectedRow = table1.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Seleccione un doctor para eliminar.", "Aviso", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int doctorId = (int) tableModel.getValueAt(selectedRow, 0);

            int confirm = JOptionPane.showConfirmDialog(this, "¿Está seguro de eliminar este doctor?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    Doctor doctor = doctorDAO.getById(doctorId);
                    if (doctor != null) {
                        boolean eliminado = doctorDAO.delete(doctor);
                        if (eliminado) {
                            JOptionPane.showMessageDialog(this, "Doctor eliminado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            cargarDoctores();
                        } else {
                            JOptionPane.showMessageDialog(this, "No se pudo eliminar el doctor.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Doctor no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(this, "Error al eliminar doctor: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    /**
     * Carga la lista de doctores desde la base de datos y actualiza la tabla.
     * Maneja excepciones y muestra mensajes de error si ocurre algún problema.
     */
    private void cargarDoctores() {
        try {
            ArrayList<Doctor> doctores = doctorDAO.getAll();
            cargarTabla(doctores);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar doctores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Busca doctores por nombre según el texto ingresado en el campo de búsqueda.
     * Actualiza la tabla con los resultados encontrados.
     * Si el campo de búsqueda está vacío, recarga todos los doctores.
     */
    private void buscarDoctores() {
        String filtro = txtBuscar.getText().trim();
        if (filtro.isEmpty()) {
            cargarDoctores();
            return;
        }
        try {
            ArrayList<Doctor> doctores = doctorDAO.searchByName(filtro);
            cargarTabla(doctores);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al buscar doctores: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Carga los doctores en la tabla.
     * Limpia la tabla y agrega cada doctor como una nueva fila.
     *
     * @param doctores Lista de doctores a mostrar en la tabla.
     */
    private void cargarTabla(ArrayList<Doctor> doctores) {
        tableModel.setRowCount(0);
        for (Doctor doc : doctores) {
            tableModel.addRow(new Object[]{
                    doc.getId(),
                    doc.getNombre(),
                    doc.getEspecialidad() != null ? doc.getEspecialidad().getNombre() : "Sin especialidad",
                    doc.getContacto()
            });
        }
    }
}
