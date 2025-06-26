package ma.ensa;

import ma.ensa.db.config.DatabaseConfig;
import ma.ensa.db.manager.DatabaseManager;
import ma.ensa.db.manager.impl.MySQLDatabaseManager;
import ma.ensa.db.manager.impl.PostgreSQLDatabaseManager;
import ma.ensa.db.manager.impl.SQLServerDatabaseManager;

import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        try {
            DatabaseConfig config = DatabaseConfig.loadFromJson("src/main/resources/database-config.json");
            DatabaseManager manager;
            String dbType = config.getType().toUpperCase();
            switch (dbType) {
                case "MYSQL":
                    manager = new MySQLDatabaseManager();
                    System.out.println("Utilisation de MySQL");
                    break;
                case "POSTGRESQL":
                    manager = new PostgreSQLDatabaseManager();
                    System.out.println("Utilisation de PostgreSQL");
                    break;
                case "SQLSERVER":
                    manager = new SQLServerDatabaseManager();
                    System.out.println("Utilisation de SQL Server");
                    break;
                default:
                    throw new Exception("Type de base de données non supporté: " + dbType);
            }
            manager.connect(config.getUrl(), config.getUsername(), config.getPassword());
            String createTable;
            if (dbType.equals("SQLSERVER")) {
                createTable =
                        "IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='etudiants' AND xtype='U') BEGIN " +
                                "CREATE TABLE etudiants (" +
                                "id INT PRIMARY KEY, " +
                                "nom VARCHAR(100), " +
                                "email VARCHAR(100), " +
                                "age INT) END";
            } else {
                createTable =
                        "CREATE TABLE IF NOT EXISTS etudiants (" +
                                "id INT PRIMARY KEY, " +
                                "nom VARCHAR(100), " +
                                "email VARCHAR(100), " +
                                "age INT)";
            }
            manager.executeUpdate(createTable);
            String insertData = "INSERT INTO etudiants (id, nom, email, age) " +
                    "VALUES (1, 'Essebaiy Aya', 'essebaiyaya@gmail.com', 20)";
            manager.executeUpdate(insertData);
            System.out.println("\nListe des étudiants avant les modifications:");
            afficherEtudiants(manager);
            String updateData = "UPDATE etudiants SET age = 21 WHERE id = 1";
            manager.executeUpdate(updateData);
            System.out.println("\nListe des étudiants après mise à jour de l'âge:");
            afficherEtudiants(manager);
            String insertData2 = "INSERT INTO etudiants (id, nom, email, age) " +
                    "VALUES (2, 'Essebaiy Yahya', 'essebaiyyahya@email.com', 25)";
            manager.executeUpdate(insertData2);
            System.out.println("\nListe des étudiants après ajout d'un nouvel étudiant:");
            afficherEtudiants(manager);
            String deleteData = "DELETE FROM etudiants WHERE id = 1";
            manager.executeUpdate(deleteData);
            System.out.println("\nListe des étudiants après suppression:");
            afficherEtudiants(manager);
            manager.close();
            System.out.println("\nOpérations terminées avec succès!");
        } catch (Exception e) {
            System.out.println("Erreur: " + e.getMessage());
        }
    }

    private static void afficherEtudiants(DatabaseManager manager) {
        try {
            String selectData = "SELECT * FROM etudiants ORDER BY id";
            List<Map<String, Object>> resultats = manager.executeQuery(selectData);

            if (resultats.isEmpty()) {
                System.out.println("Aucun étudiant trouvé dans la base de données.");
                return;
            }

            for (Map<String, Object> etudiant : resultats) {
                System.out.println("ID: " + etudiant.get("id")
                        + ", Nom: " + etudiant.get("nom")
                        + ", Email: " + etudiant.get("email")
                        + ", Âge: " + etudiant.get("age"));
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de l'affichage des étudiants: " + e.getMessage());
        }
    }
}
