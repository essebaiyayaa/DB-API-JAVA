package ma.ensa.db.manager;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface DatabaseManager {
    Connection connect(String url, String username, String password) throws Exception;
    List<Map<String, Object>> executeQuery(String query) throws Exception;
    List<Map<String, Object>> executeQuery(String query, Object... parameters) throws Exception;
    int executeUpdate(String query) throws Exception;
    int executeUpdate(String query, Object... parameters) throws Exception;
    void close() throws Exception;
}

