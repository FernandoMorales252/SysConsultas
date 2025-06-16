package sysSolutions.presentacion;
import sysSolutions.dominio.Doctor;
import sysSolutions.dominio.Especialidad;
import sysSolutions.persistencia.DoctorDAO;
import sysSolutions.persistencia.EspecialidadDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.util.ArrayList;

public class DoctorWriteForm extends JDialog{
    private boolean saved = false;

    private JTextField txtNombre;
    private JTextField txtCorreo;
    private JComboBox <Especialidad> cbEspecialidad;
    private JButton btnCancelar;
    private JButton btnGuardar;
    private JLabel titulo;

    private Doctor doctor; // null si es creación nueva, no null si es edición
    private final DoctorDAO doctorDAO = new DoctorDAO();
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();

    public DoctorWriteForm(DoctorReadingForm parent, Doctor doctor) {
        super(parent, true);
        this.doctor = doctor;
        setTitle(doctor == null ? "Nuevo Doctor" : "Editar Doctor");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        initComponents();

        try {
            loadEspecialidades(); // Carga inicial del combo con especialidades
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando especialidades: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (doctor != null) {
            loadDoctorData(); // Si es edición, precargar datos del doctor
        }
    }

    private void initComponents() {
        // Configurar título con un color más suave y fuente elegante
        titulo = new JLabel(doctor == null ? "Agregar nuevo doctor" : "Editar doctor", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(33, 97, 140)); // azul hospital

        txtNombre = new JTextField();
        txtCorreo = new JTextField();

        cbEspecialidad = new JComboBox<>();

        // Botones con colores agradables y legibles
        btnGuardar = new JButton(doctor == null ? "Guardar" : "Actualizar");
        btnCancelar = new JButton("Cancelar");

        // Cambiar colores de botones para que sean amigables y hospitalarios
        btnGuardar.setBackground(new Color(0, 123, 255)); // azul vivo
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFocusPainted(false);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnCancelar.setBackground(new Color(220, 53, 69)); // rojo suave para cancelar
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));

        btnGuardar.addActionListener(this::onGuardar);
        btnCancelar.addActionListener(e -> dispose());

        // Panel principal blanco y con bordes con padding
        JPanel panelCampos = new JPanel(new GridLayout(3, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelCampos.setBackground(Color.WHITE);

        // Etiquetas con fuente clara y color gris oscuro
        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblCorreo = new JLabel("Correo:");
        JLabel lblEspecialidad = new JLabel("Especialidad:");

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color labelColor = new Color(55, 55, 55);
        lblNombre.setFont(labelFont);
        lblNombre.setForeground(labelColor);
        lblCorreo.setFont(labelFont);
        lblCorreo.setForeground(labelColor);
        lblEspecialidad.setFont(labelFont);
        lblEspecialidad.setForeground(labelColor);

        panelCampos.add(lblNombre);
        panelCampos.add(txtNombre);
        panelCampos.add(lblCorreo);
        panelCampos.add(txtCorreo);
        panelCampos.add(lblEspecialidad);
        panelCampos.add(cbEspecialidad);

        JPanel panelBotones = new JPanel();
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 0));

        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        add(titulo, BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void loadEspecialidades() throws SQLException {
        ArrayList<Especialidad> especialidades = especialidadDAO.getAll();
        cbEspecialidad.removeAllItems();
        for (Especialidad esp : especialidades) {
            cbEspecialidad.addItem(esp);
        }
    }

    private void loadDoctorData() {
        txtNombre.setText(doctor.getNombre());
        txtCorreo.setText(doctor.getContacto());

        for (int i = 0; i < cbEspecialidad.getItemCount(); i++) {
            Especialidad esp = cbEspecialidad.getItemAt(i);
            if (esp.getId() == doctor.getEspecialidad().getId()) {
                cbEspecialidad.setSelectedIndex(i);
                break;
            }
        }
    }

    private void onGuardar(ActionEvent e) {
        String nombre = txtNombre.getText().trim();
        String correo = txtCorreo.getText().trim();
        Especialidad especialidad = (Especialidad) cbEspecialidad.getSelectedItem();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Nombre es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            txtNombre.requestFocus();
            return;
        }
        if (correo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El campo Correo es obligatorio.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            txtCorreo.requestFocus();
            return;
        }
        if (especialidad == null) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una Especialidad.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            cbEspecialidad.requestFocus();
            return;
        }

        try {
            if (doctor == null) {
                Doctor nuevo = new Doctor();
                nuevo.setNombre(nombre);
                nuevo.setContacto(correo);
                nuevo.setEspecialidad(especialidad);
                doctorDAO.create(nuevo);
                JOptionPane.showMessageDialog(this, "Doctor creado con éxito.");
                limpiarCampos();
                saved = true;  // <-- marca guardado exitoso
            } else {
                doctor.setNombre(nombre);
                doctor.setContacto(correo);
                doctor.setEspecialidad(especialidad);
                doctorDAO.update(doctor);
                JOptionPane.showMessageDialog(this, "Doctor actualizado con éxito.");
                saved = true;  // <-- marca guardado exitoso
                dispose();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar doctor: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtCorreo.setText("");
        if (cbEspecialidad.getItemCount() > 0) {
            cbEspecialidad.setSelectedIndex(0);
        }
        txtNombre.requestFocus();
    }

    // Método público para saber si se guardó el doctor o no
    public boolean isSaved() {
        return saved;
    }
}

