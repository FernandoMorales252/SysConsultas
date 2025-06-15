package sysSolutions.presentacion;
import sysSolutions.persistencia.UserDAO;
import sysSolutions.dominio.User;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
// Importa la clase WindowEvent desde el paquete java.awt.event. WindowEvent representa eventos que ocurren con las ventanas (como abrir, cerrar, minimizar, maximizar, etc.).


import javax.swing.*;

public class LoginForm extends JDialog{
    private JTextField txtEmail;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnSalir;
    private JPanel mainPanel;

    private UserDAO userDAO; // Declaración de una variable de instancia llamada 'userDAO' de tipo UserDAO. Esta variable se utilizará para interactuar con la capa de acceso a datos de los usuarios (por ejemplo, para autenticar usuarios).
    private MainForm mainForm; // Declaración de una variable de instancia llamada 'mainForm' de tipo MainForm. Esta variable  representa la ventana principal de la aplicación y se utiliza para interactuar con ella (por ejemplo, para pasar información del usuario autenticado).

    public LoginForm(MainForm mainForm) {
        this.mainForm = mainForm;
        userDAO = new UserDAO();
        setContentPane(mainPanel);
        setModal(true);
        setTitle("Login");
        pack();
        setLocationRelativeTo(mainForm);

        initStyles(); // Aplicar estilos
        initEvents(); // Configurar eventos
    }

    private void initStyles() {
        // Fondo general
        mainPanel.setBackground(Color.WHITE);

        // Fuentes
        Font inputFont = new Font("Segoe UI", Font.PLAIN, 14);
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);

        txtEmail.setFont(inputFont);
        txtPassword.setFont(inputFont);

        // Bordes de entrada
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));

        // Botones
        btnLogin.setFont(buttonFont);
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);

        btnSalir.setFont(buttonFont);
        btnSalir.setBackground(new Color(220, 53, 69));
        btnSalir.setForeground(Color.WHITE);
        btnSalir.setFocusPainted(false);
    }

    private void initEvents() {
        btnSalir.addActionListener(e -> System.exit(0));
        btnLogin.addActionListener(e -> login());

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    private void login() {
        try {
            User user = new User();
            user.setEmail(txtEmail.getText());
            user.setPasswordHash(new String(txtPassword.getPassword()));

            User userAut = userDAO.authenticate(user);

            if (userAut != null && userAut.getId() > 0 && userAut.getEmail().equals(user.getEmail())) {
                this.mainForm.setUserAuthenticated(userAut);
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(null,
                        "Email y password incorrecto",
                        "Login",
                        JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null,
                    ex.getMessage(),
                    "Sistema",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
