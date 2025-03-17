package gr.codehub.jdbc.demo02;

import org.h2.Driver;
import org.h2.tools.Server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Properties;

public class DbTestMyCrudOperations {
    // H2
    private static Server server;
    private static Properties sqlCommands = new Properties();

    public static void main(String[] args) throws Exception {
        loadProperties();
        startH2Server();
        loadDatabaseDriver();
        Connection connection = getConnection();
        createTable(connection);


        for (int time = 60; time >= 0; time--) {
            System.out.println("Stopping in " + time + " seconds");
            Thread.sleep(1000);
        }
        stopH2Server();
    }

    private static void createTable(Connection connection) throws Exception {
        String sql = sqlCommands.getProperty("create.table");
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        System.out.println("Created table in database");
    }

    // jdbc:h2:file:C:/Users/Thomas/IdeaProjects/jdbc_demos/jdbc2503_live/jdbc2503_live/data/demo02
    private static Connection getConnection() throws Exception {
        String databaseUrl = sqlCommands.getProperty("database.url.file");
        Connection c = DriverManager.getConnection(databaseUrl, "sa", "");
        System.out.println("Established connection with database");
        return c;
    }

    private static void loadDatabaseDriver() throws Exception {
        Class.forName("org.h2.Driver"); // standard Java
        // Driver.load(); // provided by H2
        System.out.println("H2 JDBC driver loaded");
    }

    private static void stopH2Server() {
        if (server.isRunning(true)) {
            server.stop();
            server.shutdown();
            System.out.println("H2 server has shut down");
        }
    }

    private static void startH2Server() throws Exception {
        System.out.println("Starting H2 server");
        server = Server.createWebServer("-web", "-webAllowOthers");
        server.start();
        System.out.println("H2 server has started with status: " + server.getStatus());
    }

    private static void loadProperties() throws Exception {
        InputStream inputStream = new FileInputStream("data/demo02.properties");
        sqlCommands.load(inputStream);
    }


}
