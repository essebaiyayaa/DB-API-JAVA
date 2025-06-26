package ma.ensa.db.manager.database;

import ma.ensa.db.manager.DatabaseManager;
import ma.ensa.db.manager.impl.SQLServerDatabaseManager;
import ma.ensa.db.manager.base.AbstractDatabaseOperationsTest;

public class SQLServerOperationsTest extends AbstractDatabaseOperationsTest {
    
    @Override
    protected DatabaseManager createDatabaseManager() {
        return new SQLServerDatabaseManager();
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