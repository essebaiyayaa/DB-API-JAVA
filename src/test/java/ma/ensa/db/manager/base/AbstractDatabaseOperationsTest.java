package ma.ensa.db.manager.base;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import ma.ensa.db.config.DatabaseConfig;
import ma.ensa.db.manager.DatabaseManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractDatabaseOperationsTest {
    protected DatabaseManager dbManager;
    protected static final String CREATE_TABLE_SQL = 
        "CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(100), email VARCHAR(100), age INT)";
    protected static final String INSERT_SQL = 
        "INSERT INTO test_table (id, name, email, age) VALUES (%d, '%s', '%s', %d)";
    protected static final String SELECT_SQL = 
        "SELECT * FROM test_table WHERE id = %d";
    protected static final String UPDATE_SQL = 
        "UPDATE test_table SET name = '%s', email = '%s', age = %d WHERE id = %d";
    protected static final String DELETE_SQL = 
        "DELETE FROM test_table WHERE id = %d";
    protected static final String TEST_DATA_FILE = "src/test/resources/test-data.csv";

    protected abstract DatabaseManager createDatabaseManager();
    protected abstract String getBeginTransactionSQL();
    protected abstract String getRollbackTransactionSQL();

    @BeforeEach
    void setUp() throws Exception {
        dbManager = createDatabaseManager();
        DatabaseConfig config = DatabaseConfig.loadFromJson("src/main/resources/database-config.json");
        dbManager.connect(config.getUrl(), config.getUsername(), config.getPassword());
        
        try {
            dbManager.executeUpdate("DROP TABLE IF EXISTS test_table");
            dbManager.executeUpdate(CREATE_TABLE_SQL);
            loadDataFromCSV();
        } catch (Exception e) {
            fail("Erreur lors de la création de la table de test: " + e.getMessage());
        }
    }

    private void loadDataFromCSV() throws IOException, CsvValidationException, Exception {
        try (CSVReader reader = new CSVReader(new FileReader(TEST_DATA_FILE))) {
            reader.readNext(); // Skip header
            String[] line;
            while ((line = reader.readNext()) != null) {
                String insertQuery = String.format(INSERT_SQL,
                    Integer.parseInt(line[0].trim()),
                    line[1].replace("'", "''"),
                    line[2].replace("'", "''"),
                    Integer.parseInt(line[3].trim())
                );
                dbManager.executeUpdate(insertQuery);
            }
        }
    }

    @Test
    void testSelectFromCSV() throws Exception {
        String selectQuery = String.format(SELECT_SQL, 1);
        List<Map<String, Object>> results = dbManager.executeQuery(selectQuery);
        assertFalse(results.isEmpty());
        assertEquals("Essebaiy Aya", results.get(0).get("name"));
        assertEquals("essebaiyaya@gmail.com", results.get(0).get("email"));
        assertEquals(20, results.get(0).get("age"));
    }

    @Test
    void testUpdateFromCSV() throws Exception {
        String updateQuery = String.format(UPDATE_SQL,
            "Updated Name".replace("'", "''"),
            "updated@email.com".replace("'", "''"),
            30,
            1
        );
        assertEquals(1, dbManager.executeUpdate(updateQuery));

        String selectQuery = String.format(SELECT_SQL, 1);
        List<Map<String, Object>> results = dbManager.executeQuery(selectQuery);
        assertEquals("Updated Name", results.get(0).get("name"));
        assertEquals("updated@email.com", results.get(0).get("email"));
        assertEquals(30, results.get(0).get("age"));
    }

    @Test
    void testDeleteFromCSV() throws Exception {
        String deleteQuery = String.format(DELETE_SQL, 1);
        assertEquals(1, dbManager.executeUpdate(deleteQuery));

        String selectQuery = String.format(SELECT_SQL, 1);
        List<Map<String, Object>> results = dbManager.executeQuery(selectQuery);
        assertTrue(results.isEmpty());
    }

    @Test
    void testTransactionRollback() throws Exception {
        try {
            dbManager.executeUpdate(getBeginTransactionSQL());
            dbManager.executeUpdate("INSERT INTO test_table (id, name, email, age) VALUES (999, 'Test', 'test@test.com', 25)");
            dbManager.executeUpdate(getRollbackTransactionSQL());
            
            List<Map<String, Object>> results = dbManager.executeQuery("SELECT * FROM test_table WHERE id = 999");
            assertTrue(results.isEmpty());
        } catch (Exception e) {
            dbManager.executeUpdate(getRollbackTransactionSQL());
            throw e;
        }
    }

    @Test
    void testSQLInjection() throws Exception {
        // Insérer des données de test avec des requêtes paramétrées
        dbManager.executeUpdate(
            "INSERT INTO test_table (id, name, email, age) VALUES (?, ?, ?, ?)",
            998, "Test1", "test1@test.com", 25
        );
        dbManager.executeUpdate(
            "INSERT INTO test_table (id, name, email, age) VALUES (?, ?, ?, ?)",
            999, "Test2", "test2@test.com", 30
        );
        
        // Tentative d'injection SQL qui devrait retourner toutes les lignes si elle réussit
        String maliciousInput = "Test1' OR '1'='1";
        
        // Test avec requête non paramétrée (devrait être vulnérable)
        String unsafeQuery = "SELECT * FROM test_table WHERE name = '" + maliciousInput + "'";
        List<Map<String, Object>> unsafeResults = dbManager.executeQuery(unsafeQuery);
        assertTrue(unsafeResults.size() > 1, "La requête non sécurisée devrait être vulnérable à l'injection SQL");
        
        // Test avec requête paramétrée (devrait être sécurisée)
        List<Map<String, Object>> safeResults = dbManager.executeQuery(
            "SELECT * FROM test_table WHERE name = ?",
            maliciousInput
        );
        assertEquals(0, safeResults.size(), "La requête paramétrée devrait être sécurisée contre l'injection SQL");
        
        // Nettoyer
        dbManager.executeUpdate("DELETE FROM test_table WHERE id IN (?, ?)", 998, 999);
    }

    @Test
    void testDoubleClose() throws Exception {
        dbManager.close();
        assertDoesNotThrow(() -> dbManager.close());
    }

    @Test
    void testPerformance() throws Exception {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 100; i++) {
            dbManager.executeQuery("SELECT * FROM test_table");
        }
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;
        assertTrue(duration < 5000, "Les requêtes ont pris trop de temps: " + duration + "ms");
    }
} 