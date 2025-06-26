package ma.ensa.db.config;

import lombok.Data;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Data
public class DatabaseConfig {
    private String type;
    private String url;
    private String username;
    private String password;
    private String driverClass;

    public static DatabaseConfig loadFromJson(String filePath) throws IOException {
        String content = new String(Files.readAllBytes(Paths.get(filePath)));
        JSONObject json = new JSONObject(content);
        String dbType = json.getString("type").toUpperCase();
        JSONObject dbConfig = json.getJSONObject(dbType.toLowerCase());
        
        DatabaseConfig config = new DatabaseConfig();
        config.setType(dbType);
        config.setUrl(dbConfig.getString("url"));
        config.setUsername(dbConfig.getString("username"));
        config.setPassword(dbConfig.getString("password"));
        config.setDriverClass(dbConfig.getString("driverClass"));
        return config;
    }
}
