
# Database Manager API

A Java API for unified access management to different DBMSs (MySQL, PostgreSQL, SQL Server).

## Features

- Unified connection to multiple DBMSs (MySQL, PostgreSQL, SQL Server)
- Execution of SQL queries (SELECT, INSERT, UPDATE, DELETE)
- Result management as `List<Map<String, Object>>`
- Configuration via JSON file
- Error handling with custom messages
- Automatic resource cleanup
- Modular and extensible design

## Prerequisites

- Java 8 or higher
- Maven 3.6 or higher
- One of the supported DBMSs (MySQL, PostgreSQL, SQL Server)

## Installation

1. Clone the repository:
```bash
git clone https://github.com/your-username/db-manager-api.git
```

2. Build the project:
```bash
mvn clean install
```

3. The JAR file will be generated in the `target/` directory.

## Configuration

Create a `database-config.json` file in the `src/main/resources` directory with the following structure:

```json
{
  "type": "MYSQL",
  "host": "localhost",
  "port": 3306,
  "database": "your_database",
  "username": "your_username",
  "password": "your_password"
}
```

Supported database types:
- MYSQL
- POSTGRESQL
- SQLSERVER

## Usage

### Connection Example

```java
import ma.ensa.db.config.DatabaseConfig;
import ma.ensa.db.manager.DatabaseManager;
import ma.ensa.db.manager.DatabaseManagerFactory;

// Load configuration
DatabaseConfig config = DatabaseConfig.loadFromJson("src/main/resources/database-config.json");

// Create a database manager instance
DatabaseManager manager = DatabaseManagerFactory.createDatabaseManager(
    DatabaseManagerFactory.DatabaseType.MYSQL
);

// Connect to the database
manager.connect(config);
```

### Executing Queries

```java
// SELECT query
String selectSql = "SELECT * FROM users WHERE age > 18";
List<Map<String, Object>> results = manager.executeQuery(selectSql);

// INSERT query
String insertSql = "INSERT INTO users (name, email) VALUES (?, ?)";
int affectedRows = manager.executeUpdate(insertSql, "John Doe", "john@example.com");

// UPDATE query
String updateSql = "UPDATE users SET age = ? WHERE name = ?";
int updatedRows = manager.executeUpdate(updateSql, 25, "John Doe");

// DELETE query
String deleteSql = "DELETE FROM users WHERE name = ?";
int deletedRows = manager.executeUpdate(deleteSql, "John Doe");
```

### Error Handling

```java
try {
    manager.executeQuery("SELECT * FROM non_existent_table");
} catch (DatabaseException e) {
    System.err.println("Database error: " + e.getMessage());
}
```

## Testing

The project includes unit tests using JUnit and test data in CSV format.

To run the tests:

```bash
mvn test
```

## Project Structure

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

## Contributing

Contributions are welcome! Feel free to:

1. Fork the repository  
2. Create a feature branch  
3. Commit your changes  
4. Push the branch to your fork  
5. Open a Pull Request  

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.
