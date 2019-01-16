package uz.jamshid.nativedb;

public class SQLiteCursor {

    private SQLitePreparedStatement preparedStatement;

    public SQLiteCursor(SQLitePreparedStatement stmt) {
        preparedStatement = stmt;
    }


    public boolean next() throws SQLiteException {
        int res = preparedStatement.step(preparedStatement.getStatementHandle());
        if (res == -1) {
            throw new SQLiteException("sqlite busy");
        }
        return (res == 0);
    }

    public int intValue(int columnIndex) throws SQLiteException {
        return columnIntValue(preparedStatement.getStatementHandle(), columnIndex);
    }

    public String stringValue(int columnIndex) throws SQLiteException {
        return columnStringValue(preparedStatement.getStatementHandle(), columnIndex);
    }


    public void dispose() throws SQLiteException{
        preparedStatement.dispose();
    }

    public long getStatementHandle() {
        return preparedStatement.getStatementHandle();
    }

    native int columnIntValue(long statementHandle, int columnIndex);
    native String columnStringValue(long statementHandle, int columnIndex);

}
