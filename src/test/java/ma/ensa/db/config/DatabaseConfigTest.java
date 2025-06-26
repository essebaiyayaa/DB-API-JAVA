package ma.ensa.db.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;

public class DatabaseConfigTest {
    
    @Test
    void testLoadFromJson() throws IOException {
        DatabaseConfig config = DatabaseConfig.loadFromJson("src/main/resources/database-config.json");
        assertNotNull(config);
        assertNotNull(config.getUrl());
        assertNotNull(config.getUsername());
        assertNotNull(config.getPassword());
    }

    @Test
    void testLoadFromInvalidJson() {
        assertThrows(IOException.class, () -> 
            DatabaseConfig.loadFromJson("invalid-path.json")
        );
    }

    @Test
    void testLoadFromInvalidConfig() {
        assertThrows(RuntimeException.class, () -> 
            DatabaseConfig.loadFromJson("src/test/resources/invalid-config.json")
        );
    }
} 