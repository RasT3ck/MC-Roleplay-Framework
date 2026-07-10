package org.rast3ck.mcrp.core.data.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private Path databaseFile;
    private Connection connection;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new ExceptionInInitializerError("SQLite JDBC driver not found on classpath");
        }
    }

    /**
     * Debe llamarse una vez que se conoce la carpeta real del mundo
     * (por ejemplo, en ServerStartingEvent usando LevelResource.ROOT).
     */
    public synchronized void connect(Path worldDirectory) {

        try {

            Path directory = worldDirectory.resolve("mcrp");

            Files.createDirectories(directory);

            this.databaseFile = directory.resolve("mcrp.db");

            this.connection = DriverManager.getConnection(getUrl());

            // WAL permite lecturas concurrentes mientras se escribe,
            // reduciendo drásticamente los errores de "database is locked".
            try (Statement statement = connection.createStatement()) {
                statement.execute("PRAGMA journal_mode=WAL;");
                statement.execute("PRAGMA busy_timeout=5000;");
                statement.execute("PRAGMA foreign_keys=ON;");
            }

        } catch (SQLException | IOException e) {

            throw new RuntimeException("Could not connect database", e);

        }

    }

    /**
     * Devuelve la conexión persistente y compartida.
     * IMPORTANTE: quien la use NO debe cerrarla (no usar try-with-resources sobre esta Connection).
     */
    public synchronized Connection getConnection() {

        if (connection == null) {
            throw new IllegalStateException("DatabaseManager.connect(worldDirectory) no ha sido llamado todavía");
        }

        try {
            if (connection.isClosed()) {
                this.connection = DriverManager.getConnection(getUrl());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Could not verify database connection", e);
        }

        return connection;

    }

    private String getUrl() {
        if (databaseFile == null) {
            throw new IllegalStateException("DatabaseManager.connect(worldDirectory) no ha sido llamado todavía");
        }
        return "jdbc:sqlite:" + databaseFile.toAbsolutePath();
    }

    public synchronized void close() {

        if (connection == null) {
            return;
        }

        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Could not close database connection", e);
        } finally {
            connection = null;
        }

    }

}