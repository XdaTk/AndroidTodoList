package com.leftcoding.app.todo.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.leftcoding.app.todo.R;
import com.leftcoding.app.todo.fragment.TaskFragment;
import com.leftcoding.app.todo.model.TaskModel;
import de.hdodenhof.circleimageview.CircleImageView;

public abstract class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<TaskModel> items;

    private TaskFragment mTaskFragment;

    TaskAdapter(TaskFragment taskFragment) {
        this.mTaskFragment = taskFragment;
        items = new ArrayList<>();
    }

    public TaskModel getItem(int position) {
        return items.get(position);
    }

    public void addItem(TaskModel item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public void addItem(int location, TaskModel item) {
        items.add(location, item);
        notifyItemInserted(location);
    }

    public void updateTask(TaskModel newTask) {
        for (int i = 0; i < getItemCount(); i++) {
            TaskModel task = getItem(i);
            if (newTask.getTimeStamp() == task.getTimeStamp()) {
                removeItem(i);
                getTaskFragment().addTask(newTask, false);
            }
        }
    }

    // removes Item
    public void removeItem(int location) {
        if (location >= 0 && location <= getItemCount() - 1) {
            items.remove(location);
            notifyItemRemoved(location);
        }
    }


    public void removeAllItems() {
        if (getItemCount() != 0) {
            items = new ArrayList<>();
            notifyDataSetChanged();
        }
    }

    //returns size of List
    @Override
    public int getItemCount() {
        return items.size();
    }

    static abstract class TaskViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener, View.OnLongClickListener {
        protected TaskModel task;

        protected TextView title;
        protected TextView date;
        CircleImageView priority;

        TaskViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.tvTaskTitle);
            date = (TextView) itemView.findViewById(R.id.tvTaskDate);
            priority = (CircleImageView) itemView.findViewById(R.id.cvTaskPriority);

            itemView.setOnClickListener(this);
            priority.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cvTaskPriority:
                    handlePriorityClick(v);
                    break;
                default:
                    handleItemClick(v);
                    break;
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return handleItemLongClick(v);
        }

        protected abstract void handlePriorityClick(View v);

        protected abstract boolean handleItemLongClick(View v);

        protected void handleItemClick(View v) {
        }
    }

    protected class SeparatorViewHolder extends RecyclerView.ViewHolder {

        TextView type;

        public SeparatorViewHolder(View itemView, TextView type) {
            super(itemView);
            this.type = type;
        }
    }

    TaskFragment getTaskFragment() {
        return mTaskFragment;
    }
}
