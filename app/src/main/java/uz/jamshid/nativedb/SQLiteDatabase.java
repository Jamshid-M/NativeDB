package uz.jamshid.nativedb;

public class SQLiteDatabase {

    private final long sqliteHandle;

    public long getSQLiteHandle() {
        return sqliteHandle;
    }


    public SQLiteDatabase(String fileName) throws SQLiteException {
        sqliteHandle = opendb(fileName);
    }

    public SQLitePreparedStatement execute(String sql) throws SQLiteException {
        return new SQLitePreparedStatement(this, sql);
    }

    public SQLiteCursor query(String sql) throws SQLiteException {
        return new SQLitePreparedStatement(this, sql).query();
    }

    public void close() throws SQLiteException {
        closedb(sqliteHandle);
    }

    //opens database with the given database path and name
    native long opendb(String fileName) throws SQLiteException;
    //close database
    native void closedb(long sqliteHandle) throws SQLiteException;
}
