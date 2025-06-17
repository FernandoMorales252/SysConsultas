package sysSolutions.presentacion;
import sysSolutions.dominio.Cita;
import sysSolutions.dominio.Doctor;
import sysSolutions.dominio.Paciente;
import sysSolutions.persistencia.CitaDAO;
import sysSolutions.persistencia.DoctorDAO;
import sysSolutions.persistencia.PacienteDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class CitaWriteForm extends JDialog {
    private JLabel titulo;
    private JComboBox<Paciente> cbPaciente; // JComboBox para seleccionar paciente
    private JComboBox<Doctor> cbDoctor;
    private JTextField txtFecha;
    private JTextField txtHora;
    private JTextField txtMotivo;
    private JButton btnCancelar;
    private JButton btnGuardar;

    private boolean saved = false;
    private Cita cita; // null si es nueva, no null si es edición
    private final CitaDAO citaDAO = new CitaDAO();
    private final PacienteDAO pacienteDAO = new PacienteDAO();
    private final DoctorDAO doctorDAO = new DoctorDAO();

    public CitaWriteForm(CitaReadingForm parent, Cita cita) {
        super(parent, true);
        this.cita = cita;
        setTitle(cita == null ? "Nueva Cita" : "Editar Cita");
        setSize(500, 350);
        setLocationRelativeTo(parent);
        initComponents();

        try {
            cargarPacientes();
            cargarDoctores();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error cargando datos: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }

        if (cita != null) {
            cargarDatosCita();
        }
    }

    private void initComponents() {
        titulo = new JLabel(cita == null ? "Agregar nueva cita" : "Editar cita", JLabel.CENTER);
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titulo.setForeground(new Color(33, 97, 140));

        cbPaciente = new JComboBox<>();
        cbDoctor = new JComboBox<>();
        txtFecha = new JTextField("2025-01-01");
        txtHora = new JTextField("10:00");
        txtMotivo = new JTextField();

        btnGuardar = new JButton(cita == null ? "Guardar" : "Actualizar");
        btnCancelar = new JButton("Cancelar");

        btnGuardar.setBackground(new Color(0, 123, 255));
        btnGuardar.setForeground(Color.WHITE);
        btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnGuardar.setFocusPainted(false);

        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnCancelar.setFocusPainted(false);

        btnGuardar.addActionListener(this::onGuardar);
        btnCancelar.addActionListener(e -> dispose());

        JPanel panelCampos = new JPanel(new GridLayout(5, 2, 10, 10));
        panelCampos.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panelCampos.setBackground(Color.WHITE);

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
        Color labelColor = new Color(55, 55, 55);

        panelCampos.add(new JLabel("Paciente:")).setFont(labelFont);
        panelCampos.add(cbPaciente);

        panelCampos.add(new JLabel("Doctor:")).setFont(labelFont);
        panelCampos.add(cbDoctor);

        panelCampos.add(new JLabel("Fecha (YYYY-MM-DD):")).setFont(labelFont);
        panelCampos.add(txtFecha);

        panelCampos.add(new JLabel("Hora (HH:MM):")).setFont(labelFont);
        panelCampos.add(txtHora);

        panelCampos.add(new JLabel("Motivo:")).setFont(labelFont);
        panelCampos.add(txtMotivo);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
        panelBotones.add(btnCancelar);
        panelBotones.add(btnGuardar);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);
        add(titulo, BorderLayout.NORTH);
        add(panelCampos, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
    }

    private void cargarPacientes() throws SQLException {
        ArrayList<Paciente> pacientes = pacienteDAO.getAll();
        cbPaciente.removeAllItems();
        for (Paciente p : pacientes) {
            cbPaciente.addItem(p);
        }
    }

    private void cargarDoctores() throws SQLException {
        ArrayList<Doctor> doctores = doctorDAO.getAll();
        cbDoctor.removeAllItems();
        for (Doctor d : doctores) {
            cbDoctor.addItem(d);
        }
    }

    private void cargarDatosCita() {
        txtFecha.setText(cita.getFecha().toString());
        txtHora.setText(cita.getHora().toString());
        txtMotivo.setText(cita.getMotivo());

        for (int i = 0; i < cbPaciente.getItemCount(); i++) {
            if (cbPaciente.getItemAt(i).getId() == cita.getPacienteId()) {
                cbPaciente.setSelectedIndex(i);
                break;
            }
        }

        for (int i = 0; i < cbDoctor.getItemCount(); i++) {
            if (cbDoctor.getItemAt(i).getId() == cita.getDoctorId()) {
                cbDoctor.setSelectedIndex(i);
                break;
            }
        }
    }

    private void onGuardar(ActionEvent e) {
        try {
            Paciente paciente = (Paciente) cbPaciente.getSelectedItem();
            Doctor doctor = (Doctor) cbDoctor.getSelectedItem();
            LocalDate fecha = LocalDate.parse(txtFecha.getText().trim());
            LocalTime hora = LocalTime.parse(txtHora.getText().trim());
            String motivo = txtMotivo.getText().trim();

            if (paciente == null || doctor == null || motivo.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                        "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (cita == null) {
                Cita nueva = new Cita(0, paciente.getId(), paciente.getNombre(),
                        doctor.getId(), doctor.getNombre(), fecha, hora, motivo);
                citaDAO.create(nueva);
                JOptionPane.showMessageDialog(this, "Cita creada con éxito.");
                limpiarCampos();
                saved = true;
            } else {
                cita.setPacienteId(paciente.getId());
                cita.setPacienteNombre(paciente.getNombre());
                cita.setDoctorId(doctor.getId());
                cita.setDoctorNombre(doctor.getNombre());
                cita.setFecha(fecha);
                cita.setHora(hora);
                cita.setMotivo(motivo);
                citaDAO.update(cita);
                JOptionPane.showMessageDialog(this, "Cita actualizada con éxito.");
                saved = true;
                dispose();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al guardar la cita: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void limpiarCampos() {
        txtFecha.setText("2025-01-01");
        txtHora.setText("10:00");
        txtMotivo.setText("");
        if (cbPaciente.getItemCount() > 0) cbPaciente.setSelectedIndex(0);
        if (cbDoctor.getItemCount() > 0) cbDoctor.setSelectedIndex(0);
        txtFecha.requestFocus();
    }

    public boolean isSaved() {
        return saved;
    }
}
