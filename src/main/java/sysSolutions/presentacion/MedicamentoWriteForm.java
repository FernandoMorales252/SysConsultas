package sysSolutions.presentacion;
import sysSolutions.dominio.Medicamento;
import sysSolutions.persistencia.MedicamentoDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;


public class MedicamentoWriteForm extends JDialog {
    private boolean saved = false;
    private JTextField txtNombre;
    private JTextField txtContenido;
    private JTextField txtFecha;
    private JButton btnCancelar;
    private JButton btnGuardar;
    private JLabel lblTitulo;

    private Medicamento medicamento;
    private final MedicamentoDAO medicamentoDAO = new MedicamentoDAO();


    /**
     * Constructor para el formulario de escritura de medicamentos.
     *
     * @param parent La ventana padre (JDialog) desde donde se llama a este formulario.
     * @param medicamento La instancia de Medicamento a editar. Si es null, se asume que es un nuevo medicamento.
     */
    public MedicamentoWriteForm(JDialog parent, Medicamento medicamento) {
        super(parent, true);
        this.medicamento = medicamento;
        setTitle(medicamento == null ? "Nuevo Medicamento" : "Editar Medicamento");
        setSize(450, 350);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        initComponents();

        if (medicamento != null) {
            loadMedicamentoData();
        }
    }

    /**
     * Inicializa y configura todos los componentes de la interfaz de usuario,
     * aplicando un estilo consistente.
     */
    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // Configuración del título
        lblTitulo = new JLabel(medicamento == null ? "Agregar nuevo medicamento" : "Editar medicamento", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 97, 140)); // Azul hospital

        // Inicialización de campos de texto
        txtNombre = new JTextField(20);
        txtContenido = new JTextField(20);
        txtFecha = new JTextField(20); // Fecha: formato YYYY-MM-DD

        // Configuración y estilo de los botones
        btnGuardar = new JButton(medicamento == null ? "Guardar" : "Actualizar");
        btnCancelar = new JButton("Cancelar");

        // Estilo de botones similar a otros formularios
        btnGuardar.setBackground(new Color(0, 123, 255));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Asignar ActionListeners
        btnGuardar.addActionListener(this::onSave);
        btnCancelar.addActionListener(e -> dispose());

        // Panel para los campos de entrada
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 10)); // 3 filas, 2 columnas
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelCampos.setBackground(Color.WHITE);

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblContenido = new JLabel("Contenido (mg/ml):");
        JLabel lblFecha = new JLabel("Fecha Expiración (YYYY-MM-DD):");

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color labelColor = new Color(55, 55, 55);
        lblNombre.setFont(labelFont);
        lblNombre.setForeground(labelColor);
        lblContenido.setFont(labelFont);
        lblContenido.setForeground(labelColor);
        lblFecha.setFont(labelFont);
        lblFecha.setForeground(labelColor);

        panelCampos.add(lblNombre);
        panelCampos.add(txtNombre);
        panelCampos.add(lblContenido);
        panelCampos.add(txtContenido);
        panelCampos.add(lblFecha);
        panelCampos.add(txtFecha);

        // Panel para los botones, alineados a la derecha
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        // Añadir componentes al JDialog
        add(lblTitulo, BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Precarga los datos de un medicamento existente en los campos del formulario
     * cuando el formulario se abre en modo de edición.
     */
    private void loadMedicamentoData() {
        txtNombre.setText(medicamento.getNombre());
        txtContenido.setText(medicamento.getContenido().toPlainString()); // Convertir BigDecimal a String
        txtFecha.setText(medicamento.getFechaExpiracion().toString()); // Convertir LocalDate a String
    }

    /**
     * Maneja el evento de guardar o actualizar el medicamento en la base de datos.
     * Realiza validaciones y llama al DAO correspondiente.
     *
     * @param e El evento de acción.
     */
    private void onSave(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        String contenidoStr = txtContenido.getText().trim();
        String fechaStr = txtFecha.getText().trim();

        // Validaciones de campos obligatorios
        if (nombre.isEmpty() || contenidoStr.isEmpty() || fechaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal contenido;
        try {
            contenido = new BigDecimal(contenidoStr);
            if (contenido.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "El contenido debe ser un valor positivo.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Contenido debe ser un número válido.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        LocalDate fechaExpiracion;
        try {
            fechaExpiracion = LocalDate.parse(fechaStr);
            if (fechaExpiracion.isBefore(LocalDate.now())) {
                JOptionPane.showMessageDialog(this, "La fecha de expiración no puede ser en el pasado.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Fecha de expiración inválida. Use formato YYYY-MM-DD.",
                    "Error de Formato", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if (medicamento == null) { // Modo de creación
                Medicamento nuevoMedicamento = new Medicamento();
                nuevoMedicamento.setNombre(nombre);
                nuevoMedicamento.setContenido(contenido);
                nuevoMedicamento.setFechaExpiracion(fechaExpiracion);

                Medicamento creado = medicamentoDAO.create(nuevoMedicamento);
                if (creado != null) {
                    JOptionPane.showMessageDialog(this, "Medicamento creado con éxito. ID: " + creado.getId());
                    saved = true;
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear el medicamento.");
                }
            } else { // Modo de edición
                medicamento.setNombre(nombre);
                medicamento.setContenido(contenido);
                medicamento.setFechaExpiracion(fechaExpiracion);

                boolean actualizado = medicamentoDAO.update(medicamento);
                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Medicamento actualizado con éxito.");
                    saved = true;
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró el medicamento para actualizar o no hubo cambios.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar el medicamento: " + ex.getMessage(),
                    "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void clearFields() {
        txtNombre.setText("");
        txtContenido.setText("");
        txtFecha.setText("");
        txtNombre.requestFocus();
        this.medicamento = null;
        setTitle("Nuevo Medicamento");
        lblTitulo.setText("Agregar nuevo medicamento");
        btnGuardar.setText("Guardar");
    }

    /**
     * @return true si la operación de guardar/actualizar fue exitosa, false en caso contrario.
     */
    public boolean isSaved() {
        return saved;
    }
}
