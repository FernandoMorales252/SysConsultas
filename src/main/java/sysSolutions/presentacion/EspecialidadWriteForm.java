package sysSolutions.presentacion;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;


import sysSolutions.dominio.*;
import sysSolutions.persistencia.*;


public class EspecialidadWriteForm extends JDialog{
    private boolean saved = false;

    private JTextField txtNombreEspecialidad;
    private JButton btnAceptar;
    private JButton btnCancelar;
    private JLabel lblTitulo;


    private Especialidad especialidad; // null si es creación nueva, con datos si es edición
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();

    /**
     * Constructor para el formulario de escritura de especialidades.
     *
     * @param parent La ventana padre (JDialog) desde donde se llama a este formulario.
     * @param especialidad La instancia de Especialidad a editar. Si es null, se asume que es una nueva especialidad.
     */
    public EspecialidadWriteForm(JDialog parent, Especialidad especialidad) {
        super(parent, true); // true para hacerlo modal
        this.especialidad = especialidad;
        setTitle(especialidad == null ? "Nueva Especialidad" : "Editar Especialidad");
        setSize(400, 250); // Tamaño ajustado para este formulario más simple
        setLocationRelativeTo(parent); // Centrar en relación a la ventana padre
        setDefaultCloseOperation(DISPOSE_ON_CLOSE); // Cierra solo este diálogo al hacer clic en X

        initComponents(); // Inicializa y configura los componentes de la UI

        if (especialidad != null) {
            loadEspecialidadData(); // Si es edición, precarga los datos de la especialidad
        }
    }

    /**
     * Inicializa y configura todos los componentes de la interfaz de usuario,
     * aplicando un estilo consistente.
     */
    private void initComponents() {
        setLayout(new BorderLayout()); // Establece el layout del JDialog directamente
        getContentPane().setBackground(Color.WHITE);

        // Configuración del título
        lblTitulo = new JLabel(especialidad == null ? "Agregar nueva especialidad" : "Editar especialidad", JLabel.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setForeground(new Color(33, 97, 140)); // Azul hospital

        // Inicialización del campo de texto
        txtNombreEspecialidad = new JTextField(20);

        // Configuración y estilo de los botones
        btnAceptar = new JButton(especialidad == null ? "Guardar" : "Actualizar");
        btnCancelar = new JButton("Cancelar");

        // Estilo de botones similar a otros formularios
        btnAceptar.setBackground(new Color(0, 123, 255)); // Azul vivo
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnCancelar.setBackground(new Color(220, 53, 69)); // Rojo suave para cancelar
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Asignar ActionListeners
        btnAceptar.addActionListener(this::onSave);
        btnCancelar.addActionListener(e -> dispose()); // Cierra el diálogo al cancelar

        // Panel para el campo de entrada
        JPanel panelCampos = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // FlowLayout para centrar
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelCampos.setBackground(Color.WHITE);

        JLabel lblNombre = new JLabel("Nombre de la Especialidad:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblNombre.setForeground(new Color(55, 55, 55));

        panelCampos.add(lblNombre);
        panelCampos.add(txtNombreEspecialidad);

        // Panel para los botones, alineados a la derecha
        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);

        // Añadir componentes al JDialog
        add(lblTitulo, BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    /**
     * Precarga los datos de una especialidad existente en el campo del formulario
     * cuando el formulario se abre en modo de edición.
     */
    private void loadEspecialidadData() {
        txtNombreEspecialidad.setText(especialidad.getNombre());
    }

    /**
     * Maneja el evento de guardar o actualizar la especialidad en la base de datos.
     * Realiza validaciones y llama al DAO correspondiente.
     *
     * @param e El evento de acción.
     */
    private void onSave(ActionEvent e) {
        String nombre = txtNombreEspecialidad.getText().trim();

        // Validación de campo obligatorio
        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Nombre de Especialidad es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombreEspecialidad.requestFocus();
            return;
        }

        try {
            if (especialidad == null) { // Modo de creación
                Especialidad nuevaEspecialidad = new Especialidad();
                nuevaEspecialidad.setNombre(nombre);

                Especialidad creada = especialidadDAO.create(nuevaEspecialidad);
                if (creada != null) {
                    JOptionPane.showMessageDialog(this, "Especialidad creada con éxito. ID: " + creada.getId());
                    saved = true; // Marca la operación como exitosa
                    clearFields(); // Limpia para otra posible entrada
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear la especialidad.");
                }
            } else { // Modo de edición
                especialidad.setNombre(nombre);

                boolean actualizado = especialidadDAO.update(especialidad);
                if (actualizado) {
                    JOptionPane.showMessageDialog(this, "Especialidad actualizada con éxito.");
                    saved = true; // Marca la operación como exitosa
                    dispose(); // Cierra el diálogo después de la actualización exitosa
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontró la especialidad para actualizar o no hubo cambios.");
                }
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la especialidad: " + ex.getMessage(),
                    "Error DB", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    /**
     * Limpia los campos del formulario.
     */
    private void clearFields() {
        txtNombreEspecialidad.setText("");
        txtNombreEspecialidad.requestFocus();
        this.especialidad = null; // Restablecer para el modo de creación
        setTitle("Nueva Especialidad");
        lblTitulo.setText("Agregar nueva especialidad");
        btnAceptar.setText("Guardar");
    }

    /**
     * @return true si la operación de guardar/actualizar fue exitosa, false en caso contrario.
     */
    public boolean isSaved() {
        return saved;
    }
}
