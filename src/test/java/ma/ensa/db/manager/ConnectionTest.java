package ma.ensa.db.manager;

import ma.ensa.db.config.DatabaseConfig;
import ma.ensa.db.manager.impl.MySQLDatabaseManager;
import ma.ensa.db.manager.impl.PostgreSQLDatabaseManager;
import ma.ensa.db.manager.impl.SQLServerDatabaseManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConnectionTest {
    
    @Test
    void testMySQLConnection() {
        testConnection(new MySQLDatabaseManager());
    }
    
    @Test
    void testPostgreSQLConnection() {
        testConnection(new PostgreSQLDatabaseManager());
    }
    
    @Test
    void testSQLServerConnection() {
        testConnection(new SQLServerDatabaseManager());
    }
    
    private void testConnection(DatabaseManager manager) {
        try {
            DatabaseConfig config = DatabaseConfig.loadFromJson("src/main/resources/database-config.json");
            manager.connect(config.getUrl(), config.getUsername(), config.getPassword());
            
            // Test d'une requête simple pour vérifier la connexion
            manager.executeQuery("SELECT 1");
            
            manager.close();
            
        } catch (Exception e) {
            fail("Test de connexion échoué pour " + manager.getClass().getSimpleName() + ": " + e.getMessage());
        }
    }
} 