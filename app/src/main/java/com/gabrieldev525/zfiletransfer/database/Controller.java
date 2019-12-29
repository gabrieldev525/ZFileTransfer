package com.gabrieldev525.zfiletransfer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Controller {
    private SQLiteDatabase db;
    private FTPDB database;

    public Controller(Context context) {
        database = new FTPDB(context);
    }

    /**
     *
     * @param name
     * @param host
     * @param port
     * @param username
     * @param password
     * @return
     */
    public long createConnection(String name, String host, int port, String username, String password) {
        ContentValues values;
        long result;

        db = database.getWritableDatabase();
        values = new ContentValues();
        values.put(FTPDB.NAME, name);
        values.put(FTPDB.PORT, port);
        values.put(FTPDB.USERNAME, username);
        values.put(FTPDB.HOST, host);
        values.put(FTPDB.PASSWORD, password);

        result = db.insert(FTPDB.TABLE, null, values);
        db.close();

        return result;
    }

    /**
     *
     * @return
     */
    public Cursor getConnections() {
        Cursor cursor;

        String[] fields = {
            FTPDB.ID,
            FTPDB.NAME,
            FTPDB.USERNAME,
            FTPDB.HOST,
            FTPDB.PORT,
        };

        db = database.getWritableDatabase();
        cursor = db.query(FTPDB.TABLE, fields, null, null, null, null, null, null);

        if(cursor != null) {
            cursor.moveToFirst();
        }

        db.close();
        return cursor;
    }


    /**
     *
     * @param connectionId
     */
    public void deleteConnection(int connectionId) {
        String where = FTPDB.ID + "=" + connectionId;
        db = database.getReadableDatabase();
        db.delete(FTPDB.TABLE, where, null);
        db.close();
    }

    /**
     *
     * @param id
     * @param name
     * @param host
     * @param port
     * @param username
     * @param password
     */
    public void editConnection(int id, String name, String host, int port, String username, String password) {
        ContentValues values = new ContentValues();
        String where = FTPDB.ID + "=" + id;

        db = database.getWritableDatabase();

        // set the data in the values
        values.put(FTPDB.NAME, name);
        values.put(FTPDB.HOST, host);
        values.put(FTPDB.PORT, port);
        values.put(FTPDB.USERNAME, username);

        // if the password was altered
        // in the edit page, it start with a empty field
        if(!password.trim().isEmpty())
            values.put(FTPDB.PASSWORD, password);

        // update the database
        db.update(FTPDB.TABLE, values, where, null);
        db.close();
    }
}
