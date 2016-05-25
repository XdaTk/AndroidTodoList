package com.leftcoding.app.todo.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.leftcoding.app.todo.R;
import com.leftcoding.app.todo.adapter.TasksAdapter;
import com.leftcoding.app.todo.database.DbHelper;
import com.leftcoding.app.todo.model.TaskModel;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class CurrentTaskFragment extends TaskFragment {



    private OnTaskDoneListener mOnTaskDoneListener;

    public CurrentTaskFragment() {
        // Required empty public constructor
    }

    public interface OnTaskDoneListener {
        void onTaskDone(TaskModel task);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mOnTaskDoneListener = (OnTaskDoneListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnTaskDoneListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_current_task, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvCurrentTasks);

        layoutManager = new LinearLayoutManager(getActivityForTaskFragment());

        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new TasksAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


    @Override
    public void findTasks(String title) {
        mAdapter.removeAllItems();
        List<TaskModel> tasks = new ArrayList<>();
        tasks.addAll(activity.getDbHelper().query().getTasks(DbHelper.SELECTION_LIKE_TITLE + " AND "
                        + DbHelper.SELECTION_STATUS + " OR " + DbHelper.SELECTION_STATUS,
                new String[]{"%" + title + "%", Integer.toString(TaskModel.STATUS_CURRENT),
                        Integer.toString(TaskModel.STATUS_OVERDUE)}, DbHelper.TASK_DATE_COLUMN));
        for (TaskModel task : tasks) {
            addTask(task, false);
        }
    }

    @Override
    public void checkAdapter() {
        if (mAdapter == null) {
            mAdapter = new TasksAdapter(this);
            addTaskFromDB();
        }
    }

    @Override
    public void addTaskFromDB() {
        checkAdapter();
        mAdapter.removeAllItems();

        List<TaskModel> tasks = new ArrayList<>();
        tasks.addAll(activity.getDbHelper().query().getTasks(DbHelper.SELECTION_STATUS + " OR "
                + DbHelper.SELECTION_STATUS, new String[]{Integer.toString(TaskModel.STATUS_CURRENT),
                Integer.toString(TaskModel.STATUS_OVERDUE)}, DbHelper.TASK_DATE_COLUMN));
        for (TaskModel task : tasks) {
            addTask(task, false);
        }
    }


    @Override
    public void addTask(TaskModel newTask, boolean saveToDB) {
        int position = -1;

        for (int i = 0; i < mAdapter.getItemCount(); i++) {
                TaskModel task = mAdapter.getItem(i);
                if (newTask.getDate() < task.getDate()) {
                    position = i;
                    break;
                }
        }

        if (newTask.getDate() != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(newTask.getDate());

        }

        if (position != -1) {
            mAdapter.addItem(position, newTask);
        } else {
            mAdapter.addItem(newTask);
        }

        if (saveToDB) {
            activity.getDbHelper().saveTask(newTask);
        }
    }


    @Override
    public void moveTask(TaskModel task) {
        mOnTaskDoneListener.onTaskDone(task);
    }
}
