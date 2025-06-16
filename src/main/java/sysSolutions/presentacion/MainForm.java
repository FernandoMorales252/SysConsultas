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
        JMenu menuMantenimiento = new JMenu("Usuarios");
        menuBar.add(menuMantenimiento);

        JMenuItem itemUsers = new JMenuItem("Usuarios");
        menuMantenimiento.add(itemUsers);
        itemUsers.addActionListener(e -> {
            UserReadingForm userReadingForm = new UserReadingForm(this);
            userReadingForm.setVisible(true);
        });

        //Menu de "Doctores"
        JMenu menuDoctores = new JMenu("Doctores"); // Crea un nuevo menú llamado "Doctores".
        menuBar.add(menuDoctores); // Agrega el menú "Doctores" a la barra de menú.

        JMenuItem itemDoctores = new JMenuItem("Doctores"); // Crea un nuevo elemento de menú llamado "Doctores".
        menuDoctores.add(itemDoctores); // Agrega el elemento "Doctores" al menú "Doctores".
        itemDoctores.addActionListener(e -> {
            DoctorReadingForm doctorReadingForm = new DoctorReadingForm(this); // Cuando se hace clic, crea una nueva instancia de DoctorReadingForm (formulario para leer/listar doctores), pasándole la instancia actual de MainForm como padre.
            doctorReadingForm.setVisible(true); // Hace visible el formulario de lectura de doctores.
        });

        // Menú "Pacientes"
        JMenu menuPacientes = new JMenu(" Pacientes");
        menuBar.add(menuPacientes);
        JMenuItem itemPacientes = new JMenuItem(" Pacientes");
        menuPacientes.add(itemPacientes);
        itemPacientes.addActionListener(e -> {
            PacienteReadingForm pacienteReadingForm = new PacienteReadingForm(this);
            pacienteReadingForm.setVisible(true);
        });

        // Menú "Citas"
        JMenu menuCitas = new JMenu("Citas");
        menuBar.add(menuCitas);
        JMenuItem itemCitas = new JMenuItem("Citas");
        menuCitas.add(itemCitas);
        itemCitas.addActionListener(e -> {
            CitaReadingForm citaReadingForm = new CitaReadingForm(this);
            citaReadingForm.setVisible(true);
        });

        //Menú de recetas
        JMenu menuRecetas = new JMenu("Recetas");
        menuBar.add(menuRecetas);
        JMenuItem itemRecetas = new JMenuItem("Recetas");
        menuRecetas.add(itemRecetas);
        itemRecetas.addActionListener(e -> {
            RecetasReadingForm recetaReadingForm = new RecetasReadingForm(this);
            recetaReadingForm.setVisible(true);
        });

        // Menú "Especialidades"
        JMenu menuEspecialidades = new JMenu("Especialidades");
        menuBar.add(menuEspecialidades);
        JMenuItem itemEspecialidades = new JMenuItem("Especialidades");
        menuEspecialidades.add(itemEspecialidades);
        itemEspecialidades.addActionListener(e -> {
            EspecialidadReadingForm especialidadReadingForm = new EspecialidadReadingForm(this);
            especialidadReadingForm.setVisible(true);
        });

        // Menú "Medicamentos"
        JMenu menuMedicamentos = new JMenu("Medicamentos");
        menuBar.add(menuMedicamentos);
        JMenuItem itemMedicamentos = new JMenuItem("Medicamentos");
        menuMedicamentos.add(itemMedicamentos);
        itemMedicamentos.addActionListener(e -> {
            MedicamentoReadingForm medicamentoReadingForm = new MedicamentoReadingForm(this);
            medicamentoReadingForm.setVisible(true);
        });


    }
}
