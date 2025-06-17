package sysSolutions.presentacion;
import sysSolutions.dominio.Paciente;
import sysSolutions.persistencia.PacienteDAO;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;



public class PacienteReadingForm extends JDialog {
    private JTextField txtTitulo;
    private JTextField txtBuscar;
    private JButton btnCrear;
    private JPanel mainPaci;
    private JTable tbPacientes;
    private JButton btnEliminar;
    private JButton btnModificar;

    private final PacienteDAO pacienteDAO;

    public PacienteReadingForm(JFrame parent) {
        super(parent, "Gestión de Pacientes", true);
        pacienteDAO = new PacienteDAO();

        initComponents();
        setContentPane(mainPaci);
        setSize(700, 450);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        cargarTabla("");
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     * Configura el diseño, colores, fuentes y eventos de los botones.
     */
    private void initComponents() {
        // Colores y fuentes
        Color fondo = new Color(240, 245, 250);
        Color botones = new Color(50, 110, 190);
        Color hover = new Color(30, 80, 160);
        Color danger = new Color(180, 50, 50);
        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);

        mainPaci = new JPanel(new BorderLayout(10, 10));
        mainPaci.setBackground(fondo);
        mainPaci.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel panelTop = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panelTop.setBackground(fondo);

        txtBuscar = new JTextField(20);
        txtBuscar.setFont(fuente);
        panelTop.add(new JLabel("Buscar:"));
        panelTop.add(txtBuscar);

        btnCrear = crearBoton("Crear", botones, hover);
        btnModificar = crearBoton("Modificar", botones, hover);
        btnEliminar = crearBoton("Eliminar", danger, new Color(140, 30, 30));

        panelTop.add(btnCrear);
        panelTop.add(btnModificar);
        panelTop.add(btnEliminar);

        mainPaci.add(panelTop, BorderLayout.NORTH);

        tbPacientes = new JTable();
        JScrollPane scroll = new JScrollPane(tbPacientes);
        mainPaci.add(scroll, BorderLayout.CENTER);

        // Eventos
        txtBuscar.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                cargarTabla(txtBuscar.getText().trim());
            }
        });

        btnCrear.addActionListener(e -> crearPaciente());
        btnModificar.addActionListener(e -> modificarPaciente());
        btnEliminar.addActionListener(e -> eliminarPaciente());
    }

    /**
     * Crea un botón con un texto, color de fondo y color de hover.
     * Configura el estilo del botón y agrega un efecto hover.
     *
     * @param texto El texto del botón.
     * @param fondo El color de fondo del botón.
     * @param hover El color de fondo al pasar el mouse por encima.
     * @return Un JButton configurado.
     */
    private JButton crearBoton(String texto, Color fondo, Color hover) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(fondo);
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Segoe UI", Font.BOLD, 13));
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(hover);
            }

            public void mouseExited(MouseEvent e) {
                boton.setBackground(fondo);
            }
        });
        return boton;
    }

    /**
     * Carga la tabla de pacientes desde la base de datos.
     * Si se proporciona un filtro, busca pacientes que coincidan con el filtro.
     * Si no hay filtro, carga todos los pacientes.
     * Actualiza el modelo de la tabla con los datos obtenidos.
     */
    private void cargarTabla(String filtro) {
        try {
            ArrayList<Paciente> lista = filtro.isEmpty()
                    ? pacienteDAO.getAll()
                    : pacienteDAO.search(filtro);

            String[] columnas = {"ID", "Nombre", "Edad", "Sexo", "Contacto", "Dirección"};
            DefaultTableModel modelo = new DefaultTableModel(columnas, 0);

            for (Paciente p : lista) {
                Object[] fila = {
                        p.getId(),
                        p.getNombre(),
                        p.getEdad(),
                        p.getSexoDescripcion(),
                        p.getContacto(),
                        p.getDireccion()
                };
                modelo.addRow(fila);
            }

            tbPacientes.setModel(modelo);
            tbPacientes.setRowHeight(24);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al cargar pacientes: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea un nuevo paciente a través de un formulario.
     * Si el formulario se completa correctamente, guarda el paciente en la base de datos
     * y actualiza la tabla de pacientes.
     */
    private void crearPaciente() {
        PacienteWriteForm form = new PacienteWriteForm(this);
        form.limpiarCampos();
        form.setVisible(true);

        if (!form.getNombre().isEmpty()) {
            try {
                Paciente nuevo = new Paciente();
                nuevo.setNombre(form.getNombre());
                nuevo.setEdad(parsearEdad(form.getEdad()));
                nuevo.setSexo(form.getSexo());
                nuevo.setContacto(form.getContacto());
                nuevo.setDireccion(form.getDireccion());

                pacienteDAO.create(nuevo);
                cargarTabla("");
                JOptionPane.showMessageDialog(this, "Paciente creado con éxito.");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al crear paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


/**
     * Modifica el paciente seleccionado en la tabla.
     * Si no hay un paciente seleccionado, muestra un mensaje de advertencia.
     * Si se selecciona un paciente, abre un formulario para editar sus datos.
     * Después de modificar, actualiza la tabla y muestra un mensaje de éxito.
     */
    private void modificarPaciente() {
        int fila = tbPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para modificar.");
            return;
        }

        int id = (int) tbPacientes.getValueAt(fila, 0);
        try {
            Paciente paciente = pacienteDAO.getById(id);
            if (paciente == null) return;

            PacienteWriteForm form = new PacienteWriteForm(this);

            form.setNombre(paciente.getNombre());
            form.setEdad(paciente.getEdad() != null ? paciente.getEdad().toString() : "");
            form.setSexo(paciente.getSexoDescripcion());
            form.setContacto(paciente.getContacto());
            form.setDireccion(paciente.getDireccion());

            form.setVisible(true);

            if (!form.getNombre().isEmpty()) {
                paciente.setNombre(form.getNombre());
                paciente.setEdad(parsearEdad(form.getEdad()));
                paciente.setSexo(form.getSexo());
                paciente.setContacto(form.getContacto());
                paciente.setDireccion(form.getDireccion());

                pacienteDAO.update(paciente);
                cargarTabla("");
                JOptionPane.showMessageDialog(this, "Paciente actualizado.");
            }

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

   /**
     * Elimina el paciente seleccionado después de confirmar la acción.
     */
    private void eliminarPaciente() {
        int fila = tbPacientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un paciente para eliminar.");
            return;
        }

        int id = (int) tbPacientes.getValueAt(fila, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "¿Deseas eliminar este paciente?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                Paciente paciente = pacienteDAO.getById(id);
                if (paciente != null) {
                    boolean eliminado = pacienteDAO.delete(paciente);
                    if (eliminado) {
                        cargarTabla("");
                        JOptionPane.showMessageDialog(this, "Paciente eliminado.");
                    } else {
                        JOptionPane.showMessageDialog(this, "No se pudo eliminar el paciente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Paciente no encontrado.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Error al eliminar paciente: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private Integer parsearEdad(String edadTexto) {
        try {
            return Integer.parseInt(edadTexto);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
