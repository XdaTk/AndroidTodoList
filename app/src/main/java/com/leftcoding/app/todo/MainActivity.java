package com.leftcoding.app.todo;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;
import com.leftcoding.app.todo.adapter.TabAdapter;
import com.leftcoding.app.todo.database.DbHelper;
import com.leftcoding.app.todo.dialog.AddingTaskDialogFragment;
import com.leftcoding.app.todo.dialog.EditTaskDialogFragment;
import com.leftcoding.app.todo.fragment.CurrentTaskFragment;
import com.leftcoding.app.todo.fragment.TaskFragment;
import com.leftcoding.app.todo.model.TaskModel;

public class MainActivity extends AppCompatActivity
        implements AddingTaskDialogFragment.AddingTaskListener,
        CurrentTaskFragment.OnTaskDoneListener,
        EditTaskDialogFragment.EditingTaskListener {

    private FragmentManager mFragmentManager;
    private TaskFragment mCurrentTaskFragment;
    private DbHelper mDbHelper;
    private SearchView mSearchView;
    private Toolbar mToolbar;

    public DbHelper getDbHelper() {
        return mDbHelper;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PreferenceHelper.getInstance().init(getApplicationContext());
        mDbHelper = new DbHelper(getApplicationContext());
        mFragmentManager = getFragmentManager();
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setUI(getApplicationContext());
    }


    private void setUI(Context context) {
        if (mToolbar != null) {
            mToolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));
            setSupportActionBar(mToolbar);
        }

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        TabAdapter mTabAdapter = new TabAdapter(mFragmentManager);
        viewPager.setAdapter(mTabAdapter);

        mCurrentTaskFragment = (CurrentTaskFragment) mTabAdapter.getItem(TabAdapter.CURRENT_TASK_FRAGMENT_POSITION);
        mSearchView = (SearchView) findViewById(R.id.search_view);

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mCurrentTaskFragment.findTasks(newText);
                return false;
            }
        });

        FloatingActionButton mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment addingTaskDialogFragment = new AddingTaskDialogFragment();
                addingTaskDialogFragment.show(mFragmentManager, "AddingTaskDialogFragment");
            }
        });
    }


    @Override
    public void onTaskAdded(TaskModel newTask) {
        mCurrentTaskFragment.addTask(newTask, true);
    }

    @Override
    public void onTaskAddingCancel() {
        Toast.makeText(this, R.string.cache_add, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onTaskDone(TaskModel task) {
    }

    @Override
    public void onTaskEdited(TaskModel updatedTask) {
        mCurrentTaskFragment.updateTask(updatedTask);
        mDbHelper.update().task(updatedTask);
    }
}
