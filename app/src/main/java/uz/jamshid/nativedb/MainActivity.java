package uz.jamshid.nativedb;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    EditText et_text;
    SQLiteDatabase database;
    SQLiteCursor cursor;

    /**
     *
     * Begin transaction is implemented in sql.cpp
     * and you can use it when you alter table in your sqlite
     */

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_text = findViewById(R.id.et_text);

        String sql_create = "CREATE TABLE IF NOT EXISTS Users (Id INTEGER PRIMARY KEY, Name TEXT)";

        try {
            database = new SQLiteDatabase(getFilesDir().getPath() + "test.db");
            database.execute("Drop table Users").step().dispose();
            database.execute(sql_create).step().dispose();
        }catch (Exception ignored){

        }

    }

    @Override
    protected void onDestroy() {
        try {
            database.close();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    public void save(View view) throws SQLiteException {
        String input;
        long last = System.currentTimeMillis();

        for (int i = 0; i<100; i++) {
            input = "INSERT INTO Users(Name) VALUES ('"+i+"');";
            database.execute(input).step().dispose();
//            db.insertData(i+"");
        }
        Log.d("MYTAG", "Insert time: " + (System.currentTimeMillis() - last));
    }

    public void read(View view) throws SQLiteException {
        String sql;
        if(et_text.getText().toString().isEmpty())
            sql = "SELECT * FROM Users";
        else
            sql = "SELECT * FROM Users WHERE Id = " + et_text.getText().toString();

        cursor = database.query(sql);

        while (cursor.next()){
            Log.d("MYTAG", cursor.stringValue(1));
        }
    }


    static{
        System.loadLibrary("native-lib");
    }
}
