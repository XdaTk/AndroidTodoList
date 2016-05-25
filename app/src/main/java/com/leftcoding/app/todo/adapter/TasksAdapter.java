package com.leftcoding.app.todo.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.leftcoding.app.todo.R;
import com.leftcoding.app.todo.anim.AnimationEndListener;
import com.leftcoding.app.todo.anim.AnimationEndListenerAdapter;
import com.leftcoding.app.todo.utils.DateUtils;
import com.leftcoding.app.todo.fragment.CurrentTaskFragment;
import com.leftcoding.app.todo.model.TaskModel;


import java.util.Calendar;

public class TasksAdapter extends TaskAdapter {

    private static final int TYPE_TASK = 0;

    public TasksAdapter(CurrentTaskFragment taskFragment) {
        super(taskFragment);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            case TYPE_TASK:
                View v = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.model_task, viewGroup, false);
                return new CurrentTaskViewHolder(v);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        TaskModel item = items.get(position);

        final Resources resources = viewHolder.itemView.getResources();
        viewHolder.itemView.setEnabled(true);
        final TaskModel task = (TaskModel) item;
        final TaskViewHolder taskViewHolder = (TaskViewHolder) viewHolder;
        taskViewHolder.task = task;

        final View itemView = taskViewHolder.itemView;

        taskViewHolder.title.setText(task.getTitle());
        if (task.getDate() != 0) {
            taskViewHolder.date.setText(DateUtils.getFullDate(task.getDate()));
        } else {
            taskViewHolder.date.setText(null);
        }

        itemView.setVisibility(View.VISIBLE);
        taskViewHolder.priority.setEnabled(true);

        if (task.getDate() != 0 && task.getDate() < Calendar.getInstance().getTimeInMillis()) {
            itemView.setBackgroundColor(resources.getColor(R.color.gray_200));
        } else {
            itemView.setBackgroundColor(resources.getColor(R.color.gray_50));
        }

        taskViewHolder.title.setTextColor(resources.getColor(R.color.primary_text_default_material_light));
        taskViewHolder.date.setTextColor(resources.getColor(R.color.secondary_text_default_material_light));
        taskViewHolder.priority.setColorFilter(resources.getColor(task.getPriorityColor()));
        taskViewHolder.priority.setImageResource(R.drawable.ic_checkbox_blank_circle_white_48dp);


    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_TASK;
    }

    private class CurrentTaskViewHolder extends TaskViewHolder implements AnimationEndListener {

        CurrentTaskViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void handleItemClick(View v) {
            getTaskFragment().showTaskEditDialog(task);
        }

        @Override
        protected void handlePriorityClick(View v) {
            Context context = v.getContext();

            task.setStatus(TaskModel.STATUS_DONE);
            getTaskFragment().getActivityForTaskFragment().getDbHelper().update().status(task.getTimeStamp(), TaskModel.STATUS_DONE);

            itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.gray_200));
            title.setTextColor(ContextCompat.getColor(context, R.color.primary_text_disabled_material_light));
            date.setTextColor(ContextCompat.getColor(context, R.color.secondary_text_disabled_material_light));
            priority.setColorFilter(ContextCompat.getColor(context, task.getPriorityColor()));
        }

        @Override
        protected boolean handleItemLongClick(View v) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getTaskFragment().removeTaskDialog(getLayoutPosition());
                }
            }, 1000);
            return true;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (task.getStatus() == TaskModel.STATUS_DONE) {
                priority.setImageResource(R.drawable.ic_check_circle_white_48dp);

                ObjectAnimator translationX = ObjectAnimator.ofFloat(itemView,
                        "translationX", 0f, itemView.getWidth());

                ObjectAnimator translationXBack = ObjectAnimator.ofFloat(itemView,
                        "translationX", itemView.getWidth(), 0f);


                translationX.addListener(new AnimationEndListenerAdapter(new AnimationEndListener() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        itemView.setVisibility(View.GONE);
                        getTaskFragment().moveTask(task);
                        removeItem(getLayoutPosition());
                    }
                }));

                AnimatorSet translationSet = new AnimatorSet();
                translationSet.play(translationX).before(translationXBack);
                translationSet.start();
            }
        }
    }

}
