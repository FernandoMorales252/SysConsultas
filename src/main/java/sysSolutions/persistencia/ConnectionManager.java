package sysSolutions.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String STR_CONNECTION =
            "jdbc:sqlserver://consultas.mssql.somee.com:1433;" +
                    "databaseName=consultas;" +
                    "user=SysA2025_SQLLogin_1;" +
                    "password=3582vnervk;" +
                    "persistSecurityInfo=False;" +
                    "packetSize=4096;" +
                    "trustServerCertificate=True;";

    private Connection connection;
    private static ConnectionManager instance;

    private ConnectionManager() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Error al cargar el driver JDBC de SQL Server", e);
        }
    }

    public synchronized Connection connect() throws SQLException {
        if (this.connection == null || this.connection.isClosed()) {
            try {
                this.connection = DriverManager.getConnection(STR_CONNECTION);
            } catch (SQLException exception) {
                throw new SQLException("Error al conectar a la base de datos: " + exception.getMessage(), exception);
            }
        }
        return this.connection;
    }

    public void disconnect() throws SQLException {
        if (this.connection != null) {
            try {
                this.connection.close();
            } finally {
                this.connection = null;
            }
        }
    }

    public static synchronized ConnectionManager getInstance() {
        if (instance == null) {
            instance = new ConnectionManager();
        }
        return instance;
    }
}

