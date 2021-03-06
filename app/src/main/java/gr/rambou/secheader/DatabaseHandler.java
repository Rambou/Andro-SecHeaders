/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Nikos Bousios
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package gr.rambou.secheader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "headers";

    // Contacts table name
    private static final String TABLE = "result";

    // Contacts Table Columns names
    private static final String KEY_WEBSITE = "website";
    private static final String KEY_HEADER = "header";
    private static final String KEY_SECURE = "secure";

    public DatabaseHandler(Context context) {
        //We open or create our database
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        //We create our table if doesn't exists
        String CREATE_CONTACTS_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE + "("
                + KEY_WEBSITE + " VARCHAR," + KEY_HEADER + " VARCHAR,"
                + KEY_SECURE + " INTEGER, PRIMARY KEY (" + KEY_WEBSITE + ", " + KEY_HEADER + "))";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);

        // Create tables again
        onCreate(db);
    }

    // Adding new result
    public void addResult(String website, String header, int secure) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Gather all values
        ContentValues values = new ContentValues();
        values.put(KEY_WEBSITE, website);
        values.put(KEY_HEADER, header);
        values.put(KEY_SECURE, secure);

        // Inserting Row
        db.insert(TABLE, null, values);

        // Close database connection
        db.close();
    }

    //Return all unique headers or Website names
    public String[] getColumnValues(Type type) {
        // Select All Query
        String selectQuery;
        if (type.equals(Type.KEY_HEADER))
            selectQuery = "SELECT  distinct " + KEY_HEADER + " FROM " + TABLE;
        else
            selectQuery = "SELECT  distinct " + KEY_WEBSITE + " FROM " + TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String[] results = new String[cursor.getCount()];
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                results[cursor.getPosition()] = cursor.getString(0);
            } while (cursor.moveToNext());
        }

        return results;
    }

    //Return an array of how many headers where secure, and the amount of time the header appeared
    public HashMap<String, String[]> getAllHeadersStats() {
        HashMap<String, String[]> results = new HashMap<>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_HEADER + "," + "sum(" + KEY_SECURE + ")," + "count(" + KEY_SECURE + ") FROM "
                + TABLE + " GROUP BY " + KEY_HEADER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                results.put(cursor.getString(0), new String[]{cursor.getString(1), cursor.getString(2)});
            } while (cursor.moveToNext());
        }

        return results;
    }

    //Return an array of how many headers where secure, and the amount of times header appeared
    public HashMap<String, String[]> getWebHeadersStats(String Domain) {
        HashMap<String, String[]> results = new HashMap<>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_HEADER + "," + "sum(" + KEY_SECURE + ")," + "count(" + KEY_SECURE + ") FROM "
                + TABLE + "WHERE " + KEY_WEBSITE + "='" + Domain + "' GROUP BY " + KEY_HEADER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                results.put(cursor.getString(0), new String[]{cursor.getString(1), cursor.getString(2)});
            } while (cursor.moveToNext());
        }

        return results;
    }

    //Return Headers from a specific website
    public HashMap<String, String> getHeader(String Domain) {
        HashMap<String, String> results = new HashMap<>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_HEADER + "," + KEY_SECURE + " FROM "
                + TABLE + " WHERE " + KEY_WEBSITE + "='" + Domain + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                results.put(cursor.getString(0), cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return results;
    }

    public enum Type {
        KEY_HEADER, KEY_WEBSITE
    }

}
