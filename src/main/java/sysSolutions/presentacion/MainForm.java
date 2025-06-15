package sysSolutions.presentacion;
import sysSolutions.dominio.User;
import javax.swing.*;

public class MainForm extends JFrame{
    private User userAuthenticated;

    public User getUserAuthenticated() {
        return userAuthenticated;
    }

    public void setUserAuthenticated(User userAuthenticated) {
        this.userAuthenticated = userAuthenticated;
    }

    public MainForm(){
        setTitle("Sistema en java de escritorio"); // Establece el título de la ventana principal (JFrame).
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Configura la operación por defecto al cerrar la ventana para que la aplicación se termine.
        setLocationRelativeTo(null); // Centra la ventana principal en la pantalla.
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Inicializa la ventana principal en estado maximizado, ocupando toda la pantalla.
        createMenu(); // Llama al método 'createMenu()' para crear y agregar la barra de menú a la ventana principal.
    }

    private void createMenu() {
        // Barra de menú
        JMenuBar menuBar = new JMenuBar(); // Crea una nueva barra de menú.
        setJMenuBar(menuBar); // Establece la barra de menú creada como la barra de menú de este JFrame (MainForm).

        JMenu menuPerfil = new JMenu("Perfil"); // Crea un nuevo menú llamado "Perfil".
        menuBar.add(menuPerfil); // Agrega el menú "Perfil" a la barra de menú.

        JMenuItem itemChangePassword = new JMenuItem("Cambiar contraseña"); // Crea un nuevo elemento de menú llamado "Cambiar contraseña".
        menuPerfil.add(itemChangePassword); // Agrega el elemento "Cambiar contraseña" al menú "Perfil".
        itemChangePassword.addActionListener(e -> { // Agrega un ActionListener al elemento "Cambiar contraseña".
            ChangePasswordForm changePassword = new ChangePasswordForm(this); // Cuando se hace clic, crea una nueva instancia de ChangePasswordForm, pasándole la instancia actual de MainForm como padre.
            changePassword.setVisible(true); // Hace visible la ventana de cambio de contraseña.

        });


        JMenuItem itemChangeUser = new JMenuItem("Cambiar de usuario"); // Crea un nuevo elemento de menú llamado "Cambiar de usuario".
        menuPerfil.add(itemChangeUser); // Agrega el elemento "Cambiar de usuario" al menú "Perfil".
        itemChangeUser.addActionListener(e -> { // Agrega un ActionListener al elemento "Cambiar de usuario".
            LoginForm loginForm = new LoginForm(this); // Cuando se hace clic, crea una nueva instancia de LoginForm (ventana de inicio de sesión), pasándole la instancia actual de MainForm como padre.
            loginForm.setVisible(true); // Hace visible la ventana de inicio de sesión.
        });


        JMenuItem itemSalir = new JMenuItem("Salir"); // Crea un nuevo elemento de menú llamado "Salir".
        menuPerfil.add(itemSalir); // Agrega el elemento "Salir" al menú "Perfil".
        itemSalir.addActionListener(e -> System.exit(0)); // Agrega un ActionListener al elemento "Salir". Cuando se hace clic, termina la ejecución de la aplicación (cierra la JVM).


        // Menú "Matenimiento"
        JMenu menuMantenimiento = new JMenu("Mantenimientos"); // Crea un nuevo menú llamado "Mantenimientos".
        menuBar.add(menuMantenimiento); // Agrega el menú "Mantenimientos" a la barra de menú.

        JMenuItem itemUsers = new JMenuItem("Usuarios"); // Crea un nuevo elemento de menú llamado "Usuarios".
        menuMantenimiento.add(itemUsers); // Agrega el elemento "Usuarios" al menú "Mantenimientos".
        itemUsers.addActionListener(e -> { // Agrega un ActionListener al elemento "Usuarios".
            UserReadingForm userReadingForm=new UserReadingForm(this); // Cuando se hace clic, crea una nueva instancia de UserReadingForm (formulario para leer/listar usuarios), pasándole la instancia actual de MainForm como padre.
            userReadingForm.setVisible(true); // Hace visible el formulario de lectura de usuarios.
        });

        //Menu de "Doctores"
        JMenu menuDoctores = new JMenu("Menú Doctores"); // Crea un nuevo menú llamado "Doctores".
        menuBar.add(menuDoctores); // Agrega el menú "Doctores" a la barra de menú.

        JMenuItem itemDoctores = new JMenuItem("Doctores"); // Crea un nuevo elemento de menú llamado "Doctores".
        menuDoctores.add(itemDoctores); // Agrega el elemento "Doctores" al menú "Doctores".
        itemDoctores.addActionListener(e -> {
            DoctorReadingForm doctorReadingForm = new DoctorReadingForm(this); // Cuando se hace clic, crea una nueva instancia de DoctorReadingForm (formulario para leer/listar doctores), pasándole la instancia actual de MainForm como padre.
            doctorReadingForm.setVisible(true); // Hace visible el formulario de lectura de doctores.
        });

        //Menu de "Citas"
        JMenu menuCitas = new JMenu("Menú Citas"); // Crea un nuevo menú para Citas
        menuBar.add(menuCitas); // Agrega el menú "Citas" a la barra de menú.

        JMenuItem itemCitas = new JMenuItem("Citas"); // Crea un nuevo elemento de menú llamado "Citas".
        menuCitas.add(itemCitas); // Agrega el elemento "Citas" al menú "Citas".
        itemCitas.addActionListener(e -> { // Agrega un Action Listener
            CitaReadingForm citaReadingForm = new CitaReadingForm(this); // Crea una instancia de CitaReadingForm
            citaReadingForm.setVisible(true);  //Hace visible el formulario de lectura de Citas.
        });



    }
}
