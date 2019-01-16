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

    native long opendb(String fileName) throws SQLiteException;
    native void closedb(long sqliteHandle) throws SQLiteException;
}
