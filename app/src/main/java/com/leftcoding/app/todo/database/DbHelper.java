package com.leftcoding.app.todo.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import com.leftcoding.app.todo.model.TaskModel;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "todo_database";
    static final String TASKS_TABLE = "tasks_table";
    static final String TASK_TITLE_COLUMN = "task_title";
    public static final String TASK_DATE_COLUMN = "task_date";
    static final String TASK_PRIORITY_COLUMN = "task_priority";
    static final String TASK_STATUS_COLUMN = "task_status";
    static final String TASK_TIME_STAMP_COLUMN = "task_time_stamp";

    // creates table
    private static final String TASKS_TABLE_CREATE_SCRIPT = "CREATE TABLE "
            + TASKS_TABLE + " (" + BaseColumns._ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK_TITLE_COLUMN + " TEXT NOT NULL, "
            + TASK_DATE_COLUMN + " LONG, " + TASK_PRIORITY_COLUMN + " INTEGER, "
            + TASK_STATUS_COLUMN + " INTEGER, " + TASK_TIME_STAMP_COLUMN + " LONG);";


    public static final String SELECTION_STATUS = DbHelper.TASK_STATUS_COLUMN + " = ?";
    public static final String SELECTION_TIME_STAMP = TASK_TIME_STAMP_COLUMN + " = ?";
    public static final String SELECTION_LIKE_TITLE = TASK_TITLE_COLUMN + " LIKE ?";

    private DbQueryManager mQueryManager;
    private DbUpdateManager mUpdateManager;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mQueryManager = new DbQueryManager(getReadableDatabase());
        mUpdateManager = new DbUpdateManager(getWritableDatabase());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TASKS_TABLE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TASKS_TABLE);
        onCreate(db);
    }

    public void saveTask(TaskModel task) {
        ContentValues newValues = new ContentValues();

        newValues.put(TASK_TITLE_COLUMN, task.getTitle());
        newValues.put(TASK_DATE_COLUMN, task.getDate());
        newValues.put(TASK_STATUS_COLUMN, task.getStatus());
        newValues.put(TASK_PRIORITY_COLUMN, task.getPriority());
        newValues.put(TASK_TIME_STAMP_COLUMN, task.getTimeStamp());

        getWritableDatabase().insert(TASKS_TABLE, null, newValues);
    }

    public DbQueryManager query() {
        return mQueryManager;
    }

    public DbUpdateManager update() {
        return mUpdateManager;
    }

    public void removeTask(long timeStamp){
        getWritableDatabase().delete(TASKS_TABLE, SELECTION_TIME_STAMP,
                new String[]{Long.toString(timeStamp)});
    }
}
