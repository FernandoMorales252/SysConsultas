package sysSolutions.presentacion;
import javax.swing.*;
import java.awt.*;

public class PacienteWriteForm extends JDialog{
    private JTextField txtTitulo;
    private JTextField txtNombre;
    private JTextField txtEdad;
    private JComboBox <String> CBSexo;
    private JTextField txtContacto;
    private JTextField txtDireccion;
    private JButton btnAceptar;
    private JButton btnCancelar;


    private boolean datosValidados = false; // Nuevo: para saber si se debe guardar

    public PacienteWriteForm(JDialog parent) {
        super(parent, "Registro de Paciente", true);
        initComponents();
        setLocationRelativeTo(parent);
        setSize(450, 380);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    /**
     * Inicializa y configura todos los componentes de la interfaz de usuario,
     * aplicando un estilo consistente.
     */
    private void initComponents() {
        Color fondo = new Color(240, 245, 250);
        Color panelColor = Color.WHITE;
        Font fuente = new Font("Segoe UI", Font.PLAIN, 14);
        Color azul = new Color(50, 110, 190);
        Color azulHover = new Color(30, 80, 160);
        Color rojo = new Color(180, 50, 50);
        Color rojoHover = new Color(140, 30, 30);

        setLayout(new BorderLayout());
        getContentPane().setBackground(fondo);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));
        add(panel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblNombre = new JLabel("Nombre:");
        JLabel lblEdad = new JLabel("Edad:");
        JLabel lblSexo = new JLabel("Sexo:");
        JLabel lblContacto = new JLabel("Contacto:");
        JLabel lblDireccion = new JLabel("Dirección:");

        txtNombre = new JTextField();
        txtEdad = new JTextField();
        CBSexo = new JComboBox<>(new String[]{"No especificado", "Masculino", "Femenino"});
        txtContacto = new JTextField();
        txtDireccion = new JTextField();

        JLabel[] labels = {lblNombre, lblEdad, lblSexo, lblContacto, lblDireccion};
        JTextField[] campos = {txtNombre, txtEdad, null, txtContacto, txtDireccion};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0;
            gbc.gridy = i;
            labels[i].setFont(fuente);
            panel.add(labels[i], gbc);

            gbc.gridx = 1;
            if (i == 2) {
                CBSexo.setFont(fuente);
                panel.add(CBSexo, gbc);
            } else {
                campos[i].setFont(fuente);
                panel.add(campos[i], gbc);
            }
        }


        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(panelColor);
        add(panelBotones, BorderLayout.SOUTH);

        btnAceptar = new JButton("Aceptar");
        btnCancelar = new JButton("Cancelar");

        btnAceptar.setBackground(azul);
        btnAceptar.setForeground(Color.WHITE);
        btnAceptar.setFocusPainted(false);
        btnAceptar.setFont(fuente);

        btnAceptar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnAceptar.setBackground(azulHover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btnAceptar.setBackground(azul);
            }
        });

        btnCancelar.setBackground(rojo);
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setFont(fuente);

        btnCancelar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btnCancelar.setBackground(rojoHover);
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
                btnCancelar.setBackground(rojo);
            }
        });

        btnCancelar.addActionListener(e -> dispose());

        btnAceptar.addActionListener(e -> {
            if (validarCampos()) {
                datosValidados = true; // Se puede guardar
                dispose();
            } else {
                datosValidados = false;
                JOptionPane.showMessageDialog(this,
                        "Todos los campos deben estar completos y el sexo no puede ser 'No especificado'.",
                        "Campos vacíos",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        panelBotones.add(btnCancelar);
        panelBotones.add(btnAceptar);
    }

    // Método para validar campos
    private boolean validarCampos() {
        return !getNombre().isEmpty()
                && !getEdad().isEmpty()
                && getSexo() != null
                && !getContacto().isEmpty()
                && !getDireccion().isEmpty();
    }


    public boolean isDatosValidados() {
        return datosValidados;
    }

    // Métodos públicos para interactuar
    public String getNombre() { return txtNombre.getText().trim(); }
    public String getEdad() { return txtEdad.getText().trim(); }
    public Character getSexo() {
        String s = (String) CBSexo.getSelectedItem();
        return s.equals("Masculino") ? 'M' : s.equals("Femenino") ? 'F' : null;
    }
    public String getContacto() { return txtContacto.getText().trim(); }
    public String getDireccion() { return txtDireccion.getText().trim(); }

    // Métodos para establecer valores en los campos
    public void setNombre(String nombre) { txtNombre.setText(nombre); }
    public void setEdad(String edad) { txtEdad.setText(edad); }
    public void setSexo(String sexoDescripcion) { CBSexo.setSelectedItem(sexoDescripcion); }
    public void setContacto(String contacto) { txtContacto.setText(contacto); }
    public void setDireccion(String direccion) { txtDireccion.setText(direccion); }

    /**
     * Limpia todos los campos del formulario.
     * Útil para reiniciar el formulario después de guardar o cancelar.
     */
    public void limpiarCampos() {
        txtNombre.setText("");
        txtEdad.setText("");
        CBSexo.setSelectedIndex(0);
        txtContacto.setText("");
        txtDireccion.setText("");
    }
}
