package ma.ensa.db.manager.database;

import ma.ensa.db.manager.DatabaseManager;
import ma.ensa.db.manager.impl.PostgreSQLDatabaseManager;
import ma.ensa.db.manager.base.AbstractDatabaseOperationsTest;

public class PostgreSQLOperationsTest extends AbstractDatabaseOperationsTest {
    
    @Override
    protected DatabaseManager createDatabaseManager() {
        return new PostgreSQLDatabaseManager();
    }

    @Override
    protected String getBeginTransactionSQL() {
        return "BEGIN";
    }

    @Override
    protected String getRollbackTransactionSQL() {
        return "ROLLBACK";
    }
} 