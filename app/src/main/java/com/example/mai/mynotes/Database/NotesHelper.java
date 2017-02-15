package com.example.mai.mynotes.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mai.mynotes.Note;

import java.util.ArrayList;

public class NotesHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MyNotes.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + NotesContract.NotesEntry.TABLE_NAME + " (" +
                    NotesContract.NotesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    NotesContract.NotesEntry.COLUMN_NAME_TITLE + " TEXT," +
                    NotesContract.NotesEntry.COLUMN_NAME_DESCRIPTION + " TEXT," +
                    NotesContract.NotesEntry.COLUMN_NAME_TIME + " TEXT)";


    public NotesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertNotes(ArrayList<Note> notes) {
        // Gets the data repository in write mode
        SQLiteDatabase db = getWritableDatabase();

        // Reset table before the insert operations
        db.delete(NotesContract.NotesEntry.TABLE_NAME, null, null);

        for (int i = 0; i < notes.size(); i++) {
            Note noteObj = notes.get(i);
            // Create a new map of values, where column names are the keys
            ContentValues values = new ContentValues();
            values.put(NotesContract.NotesEntry.COLUMN_NAME_TITLE, noteObj.getTitle());
            values.put(NotesContract.NotesEntry.COLUMN_NAME_DESCRIPTION, noteObj.getDescription());
            values.put(NotesContract.NotesEntry.COLUMN_NAME_TIME, noteObj.getTime());

            // Insert the new row, returning the primary key value of the new row
            db.insert(NotesContract.NotesEntry.TABLE_NAME, null, values);
        }
        db.close();
    }

    public ArrayList<Note> readNotes() {
        SQLiteDatabase db = getReadableDatabase();

        // * means every column
        // 1 means every row
        String DISPLAY_QUERY = "SELECT * FROM " + NotesContract.NotesEntry.TABLE_NAME + " WHERE 1";

        Cursor cursor = db.rawQuery(DISPLAY_QUERY, null);

        ArrayList<Note> notes = new ArrayList<>();
        while (cursor.moveToNext()) {
            Note noteObj = new Note();
            noteObj.setTitle(cursor.getString(1));
            noteObj.setDescription(cursor.getString(2));
            noteObj.setTime(cursor.getString(3));
            notes.add(noteObj);
        }
        cursor.close();
        db.close();
        return notes;
    }

}
