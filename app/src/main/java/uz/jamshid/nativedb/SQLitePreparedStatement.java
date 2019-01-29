package uz.jamshid.nativedb;

class SQLitePreparedStatement {
    private long sqliteStatementHandle;

    public long getStatementHandle() {
        return sqliteStatementHandle;
    }

    public SQLitePreparedStatement(SQLiteDatabase db, String sql) throws SQLiteException {
        sqliteStatementHandle = prepare(db.getSQLiteHandle(), sql);
    }

    public SQLitePreparedStatement step() throws SQLiteException {
        step(sqliteStatementHandle);
        return this;
    }

    public SQLiteCursor query(){
        return new SQLiteCursor(this);
    }

    public void dispose() throws SQLiteException {
        finalize(sqliteStatementHandle);
    }

    //prepares a SQL statement ready for execution.
    native long prepare(long sqliteHandle, String sql) throws SQLiteException;

    //deletes a previously prepared SQL statement from memory.
    native void finalize(long statementHandle) throws SQLiteException;

    //executes a SQL statement previously prepared by the prepare function.
    native int step(long statementHandle) throws SQLiteException;

}
