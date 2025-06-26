package ma.ensa.db.manager;
import lombok.Getter;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDatabaseManager implements DatabaseManager {
    @Getter
    protected Connection connection;
    @Override
    public List<Map<String, Object>> executeQuery(String query) throws Exception {
        return executeQuery(query, new Object[0]);
    }
    @Override
    public List<Map<String, Object>> executeQuery(String query, Object... parameters) throws Exception {
        List<Map<String, Object>> results = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }
            ResultSet rs = stmt.executeQuery();
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'exécution de la requête: " + e.getMessage(), e);
        }
        
        return results;
    }
    @Override
    public int executeUpdate(String query) throws Exception {
        return executeUpdate(query, new Object[0]);
    }
    @Override
    public int executeUpdate(String query, Object... parameters) throws Exception {
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (int i = 0; i < parameters.length; i++) {
                stmt.setObject(i + 1, parameters[i]);
            }
            return stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'exécution de la mise à jour: " + e.getMessage(), e);
        }
    }
    @Override
    public void close() throws Exception {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new Exception("Erreur lors de la fermeture de la connexion: " + e.getMessage(), e);
            }
        }
    }
}
