package gr.codehub.jdbc.demo02;

import org.h2.tools.Server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Properties;

public class DbTestMyCrudOperations {
    // H2
    private static Server server;
    private static Properties sqlCommands = new Properties();

    public static void main(String[] args) throws Exception {
        loadProperties();
        startH2Server();
        stopH2Server();
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
