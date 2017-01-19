package ro.basilescu.bogdan.templateapplication.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * Database name and version.
     */
    private static final String DATABASE_NAME = "templateapp.db";
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor.
     *
     * @param context The context for the database
     */
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Method is called during the creating of the SQLite database.
     *
     * @param sqLiteDatabase The SQLiteDatabase the table is being created into
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        addTodoTable(sqLiteDatabase);
        addPriorityTable(sqLiteDatabase);

        sqLiteDatabase.execSQL("Insert into " + TodoContract.PriorityEntry.TABLE_NAME + " ("
                + TodoContract.PriorityEntry.COLUMN_NAME + ") values "
                + "('Critical')");

        sqLiteDatabase.execSQL("Insert into " + TodoContract.TodoEntry.TABLE_NAME + " ("
                + TodoContract.TodoEntry.COLUMN_SUMMARY + ", "
                + TodoContract.TodoEntry.COLUMN_DESCRIPTION + ", "
                + TodoContract.TodoEntry.COLUMN_PRIORITY + ") values "
                + "('First task', 'Description of first task', 1)");
    }

    /**
     * Method is called during an upgrade of the database.
     *
     * @param sqLiteDatabase The SQLiteDatabase the table is being created into
     * @param oldVersion     The old version of the database
     * @param newVersion     The new version of the database
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        dropTables(sqLiteDatabase, oldVersion, newVersion);
    }

    /**
     * Creates the Todos table into the database.
     *
     * @param db The SQLiteDatabase the table is being created into
     */
    private void addTodoTable(SQLiteDatabase db) {
        db.execSQL("create table "
                + TodoContract.TodoEntry.TABLE_NAME
                + " ("
                + TodoContract.TodoEntry._ID + " integer primary key autoincrement, "
                + TodoContract.TodoEntry.COLUMN_SUMMARY + " text not null, "
                + TodoContract.TodoEntry.COLUMN_DESCRIPTION + " text not null, "
                + TodoContract.TodoEntry.COLUMN_PRIORITY + " integer not null, "
                + "FOREIGN KEY (" + TodoContract.TodoEntry.COLUMN_PRIORITY + ") "
                + "REFERENCES " + TodoContract.PriorityEntry.TABLE_NAME + " (" + TodoContract.PriorityEntry._ID
                + ")"
                + ");");
    }

    /**
     * Creates the Priority table into the database.
     *
     * @param db The SQLiteDatabase the table is being created into
     */
    private void addPriorityTable(SQLiteDatabase db) {
        db.execSQL("create table "
                + TodoContract.PriorityEntry.TABLE_NAME
                + " ("
                + TodoContract.PriorityEntry._ID + " integer primary key autoincrement, "
                + TodoContract.PriorityEntry.COLUMN_NAME + " text unique not null"
                + ");");
    }

    /**
     * Drops tables.
     *
     * @param db         The SQLiteDatabase the table is being created into
     * @param oldVersion The old version of the SQLiteDatabase
     * @param newVersion The new version of the SQLiteDatabase
     */
    private void dropTables(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TodoContract.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        // Dropping tables and creating new ones
        db.execSQL("DROP TABLE IF EXISTS " + TodoContract.TodoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TodoContract.PriorityEntry.TABLE_NAME);
        onCreate(db);
    }

}
