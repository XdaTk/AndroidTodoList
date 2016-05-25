package com.leftcoding.app.todo.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import com.leftcoding.app.todo.fragment.CurrentTaskFragment;


public class TabAdapter extends FragmentStatePagerAdapter {

    public static final int CURRENT_TASK_FRAGMENT_POSITION = 0;

    private Fragment[] mFragments = {
            new CurrentTaskFragment(),
    };

    public TabAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments[position];
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }

}
