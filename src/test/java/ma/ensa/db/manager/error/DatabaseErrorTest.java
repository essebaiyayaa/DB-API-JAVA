package ma.ensa.db.manager.error;

import ma.ensa.db.config.DatabaseConfig;
import ma.ensa.db.manager.DatabaseManager;
import ma.ensa.db.manager.impl.MySQLDatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseErrorTest {
    private DatabaseManager dbManager;

    @BeforeEach
    void setUp() throws Exception {
        dbManager = new MySQLDatabaseManager();
        DatabaseConfig config = DatabaseConfig.loadFromJson("src/main/resources/database-config.json");
        dbManager.connect(config.getUrl(), config.getUsername(), config.getPassword());
    }

    @Test
    void testInvalidQuery() {
        assertThrows(Exception.class, () -> 
            dbManager.executeQuery("SELECT * FROM table_inexistante")
        );
    }

    @Test
    void testInvalidUpdate() {
        assertThrows(Exception.class, () -> 
            dbManager.executeUpdate("UPDATE table_inexistante SET colonne = 'valeur'")
        );
    }

    @Test
    void testInvalidConnection() {
        assertThrows(Exception.class, () -> 
            dbManager.connect("jdbc:mysql://localhost:3306/base_inexistante", "user", "password")
        );
    }
} 