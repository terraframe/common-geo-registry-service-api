package org.commongeoregistry.adapter.android.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.commongeoregistry.adapter.android.sql.LocalCacheContract.GeoObjectEntry;
import org.commongeoregistry.adapter.android.sql.LocalCacheContract.TreeNodeEntry;
import org.commongeoregistry.adapter.android.sql.LocalCacheContract.ActionEntry;

public class LocalCacheDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LocalCache.db";

    private static final String SQL_CREATE_OBJECT_ENTRY =
            "CREATE TABLE " + GeoObjectEntry.TABLE_NAME + " ( " +
                    GeoObjectEntry._ID + " INTEGER PRIMARY KEY, " +
                    GeoObjectEntry.COLUMN_NAME_UID + " TEXT NOT NULL UNIQUE, " +
                    GeoObjectEntry.COLUMN_NAME_OBJECT + " TEXT )";

    private static final String SQL_CREATE_NODE_ENTRY =
            "CREATE TABLE " + TreeNodeEntry.TABLE_NAME + " ( " +
                    TreeNodeEntry._ID + " INTEGER PRIMARY KEY," +
                    TreeNodeEntry.COLUMN_NAME_PARENT + " TEXT NOT NULL, " +
                    TreeNodeEntry.COLUMN_NAME_CHILD + " TEXT NOT NULL, " +
                    TreeNodeEntry.COLUMN_NAME_HIERARCHY + " TEXT NOT NULL )";

    private static final String SQL_CREATE_ACTION_ENTRY =
            "CREATE TABLE " + ActionEntry.TABLE_NAME + " ( " +
                    ActionEntry.COLUMN_NAME_TYPE + " TEXT NOT NULL, " +
                    ActionEntry.COLUMN_NAME_JSON + " TEXT NOT NULL, " +
                    ActionEntry.COLUMN_NAME_ID + " SERIAL PRIMARY KEY )";

    private static final String SQL_DELETE_OBJECT_ENTRY =
            "DROP TABLE IF EXISTS " + GeoObjectEntry.TABLE_NAME;

    private static final String SQL_DELETE_NODE_ENTRY =
            "DROP TABLE IF EXISTS " + TreeNodeEntry.TABLE_NAME;

    private static final String SQL_DELETE_ACTION_ENTRY =
            "DROP TABLE IF EXISTS " + ActionEntry.TABLE_NAME;


    public LocalCacheDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_OBJECT_ENTRY);
        db.execSQL(SQL_CREATE_NODE_ENTRY);
        db.execSQL(SQL_CREATE_ACTION_ENTRY);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        recreate(db);
    }

    public void recreate(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_NODE_ENTRY);
        db.execSQL(SQL_DELETE_OBJECT_ENTRY);
        db.execSQL(SQL_DELETE_ACTION_ENTRY);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}