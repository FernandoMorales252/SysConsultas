package sysSolutions.presentacion;
import sysSolutions.dominio.Cita;
import sysSolutions.dominio.Medicamento;
import sysSolutions.dominio.Receta;
import sysSolutions.persistencia.RecetaDAO;
import sysSolutions.persistencia.MedicamentoDAO;
import sysSolutions.persistencia.CitaDAO;

import javax.swing.*;
import java.awt.*; // Asegúrate de que java.awt.* esté importado para Dialog
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JTextField;


public class RecetaWriteForm extends JDialog {
    private boolean saved = false; // Indica si la operación de guardar/actualizar fue exitosa

    private JLabel lblTitulo;
    private JComboBox <Cita> cbCitaID;
    private JComboBox <Medicamento> cbMedicamento;
    private JTextField txtObservaciones;
    private JTextField txtIndicaciones;
    private JButton btnGuardar;
    private JButton btnCancelar;
    private JPanel contentPane;

    private Receta receta; // Instancia de Receta: null para nueva, con datos para edición
    private final RecetaDAO recetaDAO = new RecetaDAO();
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();
    private final CitaDAO citaDAO = new CitaDAO();

 // Constructor para el formulario de escritura de recetas
    public RecetaWriteForm(JDialog parent, Receta receta) { // ADAPTACIÓN: Cambiado de Dialog a JDialog para ser como DoctorWriteForm
        super(parent, true); // true para hacerlo modal
        this.receta = receta;
        setTitle(receta == null ? "Nueva Receta" : "Editar Receta");
        setSize(450, 400); // Tamaño ajustado para una mejor visualización
        setLocationRelativeTo(parent); // Centrar en relación a la ventana padre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Cierra solo este diálogo al hacer clic en X

        initComponents(); // Inicializa y configura los componentes de la UI

        try {
            loadCitas(); // Carga las citas disponibles desde la BD
            loadMedicamentos(); // Carga los medicamentos disponibles desde la BD
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando datos para combos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        if (receta != null) {
            loadRecetaData(); // Si es edición, precarga los datos de la receta en los campos
        }
    }


    // inicializa y configura todos los componentes de la interfaz de usuario,
    private void initComponents() {
        // Configuración del título con estilo similar al DoctorWriteForm
        lblTitulo = new JLabel(receta == null ? "Agregar nueva receta" : "Editar receta", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 97, 140));

        // Inicialización de campos de entrada
        cbCitaID = new JComboBox<>();
        cbMedicamento = new JComboBox<>();
        txtIndicaciones = new JTextField(20);
        txtObservaciones = new JTextField(20);


        // Configuración y estilo de los botones
        btnGuardar = new JButton(receta == null ? "Guardar" : "Actualizar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(0, 123, 255));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Asignar ActionListeners a los botones
        btnGuardar.addActionListener(this::onGuardar);
        btnCancelar.addActionListener(e -> dispose()); // Cierra el diálogo al cancelar

        // Panel para los campos de entrada usando GridLayout
        JPanel panelCampos = new JPanel(new GridLayout(4, 2, 10, 10)); // 4 filas, 2 columnas, con espacios
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelCampos.setBackground(Color.WHITE);

        // Etiquetas para los campos con estilo
        JLabel lblCitaID = new JLabel("Cita:");
        JLabel lblMedicamento = new JLabel("Medicamento:");
        JLabel lblDosis = new JLabel("Dosis:");
        JLabel lblObservaciones = new JLabel("Observaciones:");

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color labelColor = new Color(55, 55, 55);
        lblCitaID.setFont(labelFont);
        lblCitaID.setForeground(labelColor);
        lblMedicamento.setFont(labelFont);
        lblMedicamento.setForeground(labelColor);
        lblDosis.setFont(labelFont);
        lblDosis.setForeground(labelColor);
        lblObservaciones.setFont(labelFont);
        lblObservaciones.setForeground(labelColor);

        // Añadir etiquetas y componentes al panel de campos
        panelCampos.add(lblCitaID);
        panelCampos.add(cbCitaID);
        panelCampos.add(lblMedicamento);
        panelCampos.add(cbMedicamento);
        panelCampos.add(lblDosis);
        panelCampos.add(txtIndicaciones);
        panelCampos.add(lblObservaciones);
        panelCampos.add(txtObservaciones);

        // Panel para los botones, alineados a la derecha
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        // Añadir componentes al JDialog
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        add(lblTitulo, BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    // Carga las citas disponibles desde la base de datos y las añade al JComboBox de Citas.
    private void loadCitas() throws SQLException {
        List<Cita> citas = citaDAO.getAll();
        cbCitaID.removeAllItems();
        for (Cita cita : citas) {
            cbCitaID.addItem(cita);
        }
    }

   // Carga los medicamentos disponibles desde la base de datos y los añade al JComboBox de Medicamentos.
    private void loadMedicamentos() throws SQLException {
        List<Medicamento> medicamentos = medicamentoDAO.getAll(); // Usando tu MedicamentoDAO real
        cbMedicamento.removeAllItems();
        for (Medicamento med : medicamentos) {
            cbMedicamento.addItem(med);
        }
    }

   // Precarga los datos de una receta existente en los campos del formulario
    private void loadRecetaData() {
        // Seleccionar la Cita en el JComboBox
        for (int i = 0; i < cbCitaID.getItemCount(); i++) {
            Cita item = cbCitaID.getItemAt(i);
            if (item != null && receta.getCitaId() == item.getId()) {
                cbCitaID.setSelectedItem(item);
                break;
            }
        }

        // Seleccionar el Medicamento en el JComboBox
        for (int i = 0; i < cbMedicamento.getItemCount(); i++) {
            Medicamento item = cbMedicamento.getItemAt(i);
            if (item != null && receta.getMedicamento() != null && receta.getMedicamento().getId() == item.getId()) {
                cbMedicamento.setSelectedItem(item);
                break;
            }
        }

        txtIndicaciones.setText(receta.getDosis());
        txtObservaciones.setText(receta.getObservaciones());
    }

   // Maneja el evento de guardar o actualizar la receta en la base de datos.
    private void onGuardar(ActionEvent e) {
        // Obtener los valores de los campos
        Cita selectedCita = (Cita) cbCitaID.getSelectedItem();
        Medicamento selectedMedicamento = (Medicamento) cbMedicamento.getSelectedItem();
        String dosis = txtIndicaciones.getText().trim();
        String observaciones = txtObservaciones.getText().trim();

        // Validaciones de campos obligatorios
        if (selectedCita == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una Cita.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            cbCitaID.requestFocus();
            return;
        }
        if (selectedMedicamento == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar un Medicamento.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            cbMedicamento.requestFocus();
            return;
        }
        if (dosis.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Dosis es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            txtIndicaciones.requestFocus();
            return;
        }
        if (observaciones.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Observaciones es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            txtObservaciones.requestFocus();
            return;
        }

        try {
            if (receta == null) {
                Receta nuevaReceta = new Receta();
                nuevaReceta.setCitaId(selectedCita.getId());
                nuevaReceta.setMedicamento(selectedMedicamento);
                nuevaReceta.setDosis(dosis);
                nuevaReceta.setObservaciones(observaciones);

                Receta creada = recetaDAO.create(nuevaReceta);
                if (creada != null) {
                    JOptionPane.showMessageDialog(this, "Receta creada con éxito. ID: " + creada.getId());
                    saved = true;
                    limpiarCampos();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear la receta.");
                }
            } else {
                receta.setCitaId(selectedCita.getId());
                receta.setMedicamento(selectedMedicamento);
                receta.setDosis(dosis);
                receta.setObservaciones(observaciones);

                boolean actualizado = recetaDAO.update(receta);
                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Receta actualizada con éxito.");
                    saved = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró la receta para actualizar o no hubo cambios.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la receta: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error de Validación", JOptionPane.WARNING_MESSAGE);
        }
    }


    // Limpia los campos del formulario para permitir una nueva entrada
    private void limpiarCampos() {
        cbCitaID.setSelectedIndex(-1);
        cbMedicamento.setSelectedIndex(-1);
        txtIndicaciones.setText("");
        txtObservaciones.setText("");
        cbCitaID.requestFocus();
        this.receta = null;
        setTitle("Nueva Receta");
        lblTitulo.setText("Agregar nueva receta");
        btnGuardar.setText("Guardar");
    }

    // Método público para saber si se guardó la receta o no
    public boolean isSaved() {
        return saved;
    }


}
