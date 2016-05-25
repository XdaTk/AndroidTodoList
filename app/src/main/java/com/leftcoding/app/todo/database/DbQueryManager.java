package com.leftcoding.app.todo.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.leftcoding.app.todo.model.TaskModel;

import java.util.List;
import java.util.ArrayList;

public class DbQueryManager {

    private SQLiteDatabase mDatabase;

    DbQueryManager(SQLiteDatabase database) {
        this.mDatabase = database;
    }

    public TaskModel getTask(long timeStapm) {
        TaskModel taskModel = null;
        Cursor cursor = mDatabase.query(DbHelper.TASKS_TABLE, null, DbHelper.SELECTION_TIME_STAMP,
                new String[]{Long.toString(timeStapm)}, null, null, null);
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(DbHelper.TASK_TITLE_COLUMN));
            long date = cursor.getLong(cursor.getColumnIndex(DbHelper.TASK_DATE_COLUMN));
            int priority = cursor.getInt(cursor.getColumnIndex(DbHelper.TASK_PRIORITY_COLUMN));
            int status = cursor.getInt(cursor.getColumnIndex(DbHelper.TASK_STATUS_COLUMN));

            taskModel = new TaskModel(title, date, priority, status, timeStapm);
        }
        cursor.close();

        return taskModel;
    }


    public List<TaskModel> getTasks(String selection, String[] selectionArgs, String orderBy) {
        List<TaskModel> tasks = new ArrayList<>();

        Cursor c = mDatabase.query(DbHelper.TASKS_TABLE, null, selection, selectionArgs, null, null, orderBy);
        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndex(DbHelper.TASK_TITLE_COLUMN));
                long date = c.getLong(c.getColumnIndex(DbHelper.TASK_DATE_COLUMN));
                int priority = c.getInt(c.getColumnIndex(DbHelper.TASK_PRIORITY_COLUMN));
                int status = c.getInt(c.getColumnIndex(DbHelper.TASK_STATUS_COLUMN));
                long timeStamp = c.getLong(c.getColumnIndex(DbHelper.TASK_TIME_STAMP_COLUMN));

                TaskModel taskModel = new TaskModel(title, date, priority, status, timeStamp);
                tasks.add(taskModel);
            } while (c.moveToNext());
        }
        c.close();

        return tasks;
    }
}
