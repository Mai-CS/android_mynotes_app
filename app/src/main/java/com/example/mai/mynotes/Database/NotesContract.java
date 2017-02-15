package com.example.mai.mynotes.Database;

import android.provider.BaseColumns;

public class NotesContract {
    public static class NotesEntry implements BaseColumns {

        // Define database schema
        public static final String TABLE_NAME = "notes";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_TIME = "time";

    }
}
