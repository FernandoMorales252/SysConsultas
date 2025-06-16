package sysSolutions.presentacion;
import sysSolutions.dominio.User;
import javax.swing.*;
import java.awt.*;



public class MainForm extends JFrame{

    private User userAuthenticated;
    private JPanel mainPanel;
    private JLabel bannerLabel;



    public User getUserAuthenticated() {
        return userAuthenticated;
    }

    public void setUserAuthenticated(User userAuthenticated) {
        this.userAuthenticated = userAuthenticated;
    }

    public MainForm() {
        setTitle("Sistema en Java de Escritorio");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        initUI(); // Crear contenido visual
        createMenu(); // Crear menú
    }

    private void initUI() {
        // Crear panel principal
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);

        // Banner superior
        bannerLabel = new JLabel("<html><center>"
                + "<h1 style='color:#2c3e50;'>BIENVENIDOS A SYSCONSULTAS</h1>"
                + "<h3 style='color:#16a085;'>Desarrollado por SysSolutions</h3>"
                + "<h4 style='color:#7f8c8d;'>Creado por: Fernando, Elias, Fatima y Paola</h4>"
                + "</center></html>");
        bannerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bannerLabel.setVerticalAlignment(SwingConstants.CENTER);
        bannerLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));
        bannerLabel.setBackground(new Color(240, 248, 255));
        bannerLabel.setOpaque(true);

        // Estilo fuente alternativo si deseas más personalización:
        // bannerLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));

        // Agregar componentes al panel principal
        mainPanel.add(bannerLabel, BorderLayout.NORTH);
        setContentPane(mainPanel); // Establece el panel como contenido de la ventana
        setLocationRelativeTo(null); // Centrado
    }

    private void createMenu() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        // Menú Perfil
        JMenu menuPerfil = new JMenu("Perfil");
        menuBar.add(menuPerfil);

        JMenuItem itemChangePassword = new JMenuItem("Cambiar contraseña");
        menuPerfil.add(itemChangePassword);
        itemChangePassword.addActionListener(e -> {
            ChangePasswordForm changePassword = new ChangePasswordForm(this);
            changePassword.setVisible(true);
        });

        JMenuItem itemChangeUser = new JMenuItem("Cambiar de usuario");
        menuPerfil.add(itemChangeUser);
        itemChangeUser.addActionListener(e -> {
            LoginForm loginForm = new LoginForm(this);
            loginForm.setVisible(true);
        });

        JMenuItem itemSalir = new JMenuItem("Salir");
        menuPerfil.add(itemSalir);
        itemSalir.addActionListener(e -> System.exit(0));

        // Menú Mantenimientos
        JMenu menuMantenimiento = new JMenu("Menú Usuarios");
        menuBar.add(menuMantenimiento);

        JMenuItem itemUsers = new JMenuItem("Usuarios");
        menuMantenimiento.add(itemUsers);
        itemUsers.addActionListener(e -> {
            UserReadingForm userReadingForm = new UserReadingForm(this);
            userReadingForm.setVisible(true);
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

        // Menú "Pacientes"
        JMenu menuPacientes = new JMenu(" Menú Pacientes"); // Crea un nuevo menú llamado "Pacientes".
        menuBar.add(menuPacientes); // Agrega el menú "Pacientes" a la barra de menú.

        JMenuItem itemPacientes = new JMenuItem(" Menú Pacientes"); // Crea un nuevo elemento de menú llamado "Pacientes".
        menuPacientes.add(itemPacientes); // Agrega el elemento "Pacientes" al menú "Pacientes".
        itemPacientes.addActionListener(e -> { // Agrega un ActionListener al elemento "Pacientes".
            PacienteReadingForm pacienteReadingForm = new PacienteReadingForm(this); // Cuando se hace clic, crea una nueva instancia de PacienteReadingForm (formulario para leer/listar pacientes), pasándole la instancia actual de MainForm como padre.
            pacienteReadingForm.setVisible(true); // Hace visible el formulario de lectura de pacientes.
        });

        // Menú "Citas"
        JMenu menuCitas = new JMenu("Menú Citas"); // Crea un nuevo menú llamado "Citas".
        menuBar.add(menuCitas); // Agrega el menú "Citas" a la barra de menú.

        JMenuItem itemCitas = new JMenuItem("Citas"); // Crea un nuevo elemento de menú llamado "Citas".
        menuCitas.add(itemCitas); // Agrega el elemento "Citas" al menú "Citas".
        itemCitas.addActionListener(e -> { // Agrega un ActionListener al elemento "Citas".
            CitaReadingForm citaReadingForm = new CitaReadingForm(this); // Cuando se hace clic, crea una nueva instancia de CitaReadingForm (formulario para leer/listar citas), pasándole la instancia actual de MainForm como padre.
            citaReadingForm.setVisible(true); // Hace visible el formulario de lectura de citas.
        });

        //Menú de recetas
        JMenu menuRecetas = new JMenu("Menú Recetas"); // Crea un nuevo menú llamado "Recetas".
        menuBar.add(menuRecetas); // Agrega el menú "Recetas" a la barra de menú.

        JMenuItem itemRecetas = new JMenuItem("Recetas"); // Crea un nuevo elemento de menú llamado "Recetas".
        menuRecetas.add(itemRecetas); // Agrega el elemento "Recetas" al menú "Recetas".
        itemRecetas.addActionListener(e -> { // Agrega un ActionListener al elemento "Recetas".
            RecetasReadingForm recetaReadingForm = new RecetasReadingForm(this); // Cuando se hace clic, crea una nueva instancia de RecetaReadingForm (formulario para leer/listar recetas), pasándole la instancia actual de MainForm como padre.
            recetaReadingForm.setVisible(true); // Hace visible el formulario de lectura de recetas.
        });

        // Menú "Especialidades"
        JMenu menuEspecialidades = new JMenu("Menú Especialidades"); // Crea un nuevo menú llamado "Especialidades".
        menuBar.add(menuEspecialidades); // Agrega el menú "Especialidades" a la barra de menú.

        JMenuItem itemEspecialidades = new JMenuItem("Especialidades"); // Crea un nuevo elemento de menú llamado "Especialidades".
        menuEspecialidades.add(itemEspecialidades); // Agrega el elemento "Especialidades" al menú "Especialidades".
        itemEspecialidades.addActionListener(e -> { // Agrega un ActionListener al elemento "Especialidades".
            EspecialidadReadingForm especialidadReadingForm = new EspecialidadReadingForm(this); // Cuando se hace clic, crea una nueva instancia de EspecialidadReadingForm (formulario para leer/listar especialidades), pasándole la instancia actual de MainForm como padre.
            especialidadReadingForm.setVisible(true); // Hace visible el formulario de lectura de especialidades.
        });

        // Menú "Medicamentos"
        JMenu menuMedicamentos = new JMenu("Menú Medicamentos"); // Crea un nuevo menú llamado "Medicamentos".
        menuBar.add(menuMedicamentos); // Agrega el menú "Medicamentos" a la barra de menú.
        JMenuItem itemMedicamentos = new JMenuItem("Medicamentos"); // Crea un nuevo elemento de menú llamado "Medicamentos".
        menuMedicamentos.add(itemMedicamentos); // Agrega el elemento "Medicamentos" al menú "Medicamentos".
        itemMedicamentos.addActionListener(e -> { // Agrega un ActionListener al elemento "Medicamentos".
            MedicamentoReadingForm medicamentoReadingForm = new MedicamentoReadingForm(this); // Cuando se hace clic, crea una nueva instancia de MedicamentoReadingForm (formulario para leer/listar medicamentos), pasándole la instancia actual de MainForm como padre.
            medicamentoReadingForm.setVisible(true); // Hace visible el formulario de lectura de medicamentos.
        });


    }
}
