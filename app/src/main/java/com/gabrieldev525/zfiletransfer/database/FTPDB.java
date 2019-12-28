package com.gabrieldev525.zfiletransfer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class FTPDB extends SQLiteOpenHelper {

    public static final String DB_NAME = "ftp.db";
    public static final String TABLE = "connections";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String PORT = "port";
    public static final String USERNAME = "username";
    public static final String HOST = "host";
    public static final String PASSWORD = "password";
    public static final int VERSION = 1;

    Context context;

    public FTPDB(Context context) {
        super(context, DB_NAME, null, VERSION);

        this.context = context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("data base ftp", "creating ...");

        String sql = "CREATE TABLE " + TABLE + "(" +
                      ID + " integer primary key autoincrement," +
                      NAME + " text," +
                      HOST + " text," +
                      PORT + " integer," +
                      USERNAME + " text," +
                      PASSWORD + " text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
