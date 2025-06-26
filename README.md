# Database Manager API

Une API Java pour la gestion unifiée de l'accès à différents SGBD (MySQL, PostgreSQL, SQL Server).

## Fonctionnalités

- Connexion unifiée à différents SGBD (MySQL, PostgreSQL, SQL Server)
- Exécution de requêtes SQL (SELECT, INSERT, UPDATE, DELETE)
- Gestion des résultats sous forme de List<Map<String, Object>>
- Configuration via fichier JSON
- Gestion des erreurs avec messages personnalisés
- Fermeture automatique des ressources
- Design modulaire et extensible

## Prérequis

- Java 8 ou supérieur
- Maven 3.6 ou supérieur
- Un des SGBD supportés (MySQL, PostgreSQL, SQL Server)

## Installation

1. Clonez le repository :
```bash
git clone https://github.com/votre-username/db-manager-api.git
```

2. Compilez le projet :
```bash
mvn clean install
```

3. Le fichier JAR sera généré dans le dossier `target/`

## Configuration

Créez un fichier `database-config.json` dans le répertoire `src/main/resources` avec la structure suivante :

```json
{
  "type": "MYSQL",
  "host": "localhost",
  "port": 3306,
  "database": "votre_base",
  "username": "votre_utilisateur",
  "password": "votre_mot_de_passe"
}
```

Types de base de données supportés :
- MYSQL
- POSTGRESQL
- SQLSERVER

## Utilisation

### Exemple de connexion

```java
import ma.ensa.db.config.DatabaseConfig;
import ma.ensa.db.manager.DatabaseManager;
import ma.ensa.db.manager.DatabaseManagerFactory;

// Charger la configuration
DatabaseConfig config = DatabaseConfig.loadFromJson("src/main/resources/database-config.json");

// Créer une instance du gestionnaire de base de données
DatabaseManager manager = DatabaseManagerFactory.createDatabaseManager(
    DatabaseManagerFactory.DatabaseType.MYSQL
);

// Se connecter à la base de données
manager.connect(config);
```

### Exécution de requêtes

```java
// Requête SELECT
String selectSql = "SELECT * FROM users WHERE age > 18";
List<Map<String, Object>> results = manager.executeQuery(selectSql);

// Requête INSERT
String insertSql = "INSERT INTO users (name, email) VALUES (?, ?)";
int affectedRows = manager.executeUpdate(insertSql, "John Doe", "john@example.com");

// Requête UPDATE
String updateSql = "UPDATE users SET age = ? WHERE name = ?";
int updatedRows = manager.executeUpdate(updateSql, 25, "John Doe");

// Requête DELETE
String deleteSql = "DELETE FROM users WHERE name = ?";
int deletedRows = manager.executeUpdate(deleteSql, "John Doe");
```

### Gestion des erreurs

```java
try {
    manager.executeQuery("SELECT * FROM non_existent_table");
} catch (DatabaseException e) {
    System.err.println("Erreur de base de données : " + e.getMessage());
}
```

## Tests

Le projet inclut des tests unitaires utilisant JUnit et des données de test au format CSV.

Pour exécuter les tests :

```bash
mvn test
```

## Structure du projet

```
ma/
└── ensa/
    ├── db/
    │   ├── config/
    │   │   └── DatabaseConfig.java
    │   ├── exception/
    │   │   └── DatabaseException.java
    │   └── manager/
    │       ├── DatabaseManager.java
    │       ├── AbstractDatabaseManager.java
    │       └── impl/
    │           ├── MySQLDatabaseManager.java
    │           ├── PostgreSQLDatabaseManager.java
    │           └── SQLServerDatabaseManager.java
    └── example/
        └── DatabaseManagerExample.java
```

## Contribution

Les contributions sont les bienvenues ! N'hésitez pas à :
1. Fork le projet
2. Créer une branche pour votre fonctionnalité
3. Commiter vos changements
4. Pousser vers la branche
5. Ouvrir une Pull Request

## Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails. 