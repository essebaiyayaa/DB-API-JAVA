package ma.ensa.db.manager.impl;

import ma.ensa.db.exception.DatabaseException;
import ma.ensa.db.manager.AbstractDatabaseManager;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLServerDatabaseManager extends AbstractDatabaseManager {
    private static final String DRIVER_CLASS = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    @Override
    public Connection connect(String url, String username, String password) throws Exception {
        try {
            Class.forName(DRIVER_CLASS);
            connection = DriverManager.getConnection(url, username, password);
            return connection;
        } catch (Exception e) {
            throw new DatabaseException("Erreur lors de la connexion à SQL Server: " + e.getMessage(), e);
        }
    }
}
