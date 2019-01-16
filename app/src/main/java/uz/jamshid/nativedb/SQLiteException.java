package uz.jamshid.nativedb;

public class SQLiteException extends Exception {
    public final int errorCode;
    public SQLiteException(int errcode, String msg) {
        super(msg);
        errorCode = errcode;
    }

    public SQLiteException(String msg) {
        this(0, msg);
    }

    public SQLiteException() {
        errorCode = 0;
    }
}
