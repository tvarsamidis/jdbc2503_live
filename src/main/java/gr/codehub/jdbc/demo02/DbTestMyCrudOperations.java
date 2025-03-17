package gr.codehub.jdbc.demo02;

import org.h2.tools.Server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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
        insertData(connection);
        readData(connection);


        for (int time = 120; time >= 0; time--) {
            System.out.println("Stopping in " + time + " seconds");
            Thread.sleep(1000);
        }
        stopH2Server();
    }

    private static void readData(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlCommands.getProperty("select.table.001"));
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
            System.out.println("----- row: " + rowCount);
            System.out.println("Column 1 name is: " + resultSet.getMetaData().getColumnName(1));
            System.out.println("Column 1 data as integer is: " + resultSet.getInt(1));
            System.out.println("Column 2 data as String is: " + resultSet.getString(2));
            System.out.println("Column 'last' data as String is: " + resultSet.getString("last"));
        }
    }

    private static void insertData(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        int total = 0;
        for (int count = 1; count <= 10; count++) {
            String s = "insert.table.0" + String.format("%02d", count);
            System.out.println("Running " + s);
            total += statement.executeUpdate(sqlCommands.getProperty(s));
        }
        System.out.println(total + " updates executed");
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
