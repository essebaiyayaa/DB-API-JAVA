package ma.ensa.db.manager.database;

import ma.ensa.db.manager.DatabaseManager;
import ma.ensa.db.manager.impl.MySQLDatabaseManager;
import ma.ensa.db.manager.base.AbstractDatabaseOperationsTest;

public class MySQLOperationsTest extends AbstractDatabaseOperationsTest {
    
    @Override
    protected DatabaseManager createDatabaseManager() {
        return new MySQLDatabaseManager();
    }

    @Override
    protected String getBeginTransactionSQL() {
        return "START TRANSACTION";
    }

    @Override
    protected String getRollbackTransactionSQL() {
        return "ROLLBACK";
    }
} 