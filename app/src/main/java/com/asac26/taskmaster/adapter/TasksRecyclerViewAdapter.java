package com.asac26.taskmaster.adapter;

import static com.asac26.taskmaster.activities.MainActivity.TASK_ID_TAG;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.datastore.generated.model.Task;
import com.asac26.taskmaster.R;
import com.asac26.taskmaster.activities.TaskDetailsActivity;

import java.util.List;


public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskViewHolder> {
    List<Task> taskList;
    Context context;
    public TasksRecyclerViewAdapter(List<Task> taskList, Context context) {
        this.taskList = taskList;
        this.context=context;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        if (taskList != null && position >= 0 && position < taskList.size()) {
            Task task = taskList.get(position);
            holder.bindTask(task);
            holder.itemView.setOnClickListener(view -> {
                Intent sendTitle = new Intent(context, TaskDetailsActivity.class);
                sendTitle.putExtra("taskTitle", task.getName());
                sendTitle.putExtra("taskBody", task.getBody());
                sendTitle.putExtra("taskStatus", task.getState().toString());
                sendTitle.putExtra("taskTeam", task.getTeamTask().getName());
                sendTitle.putExtra("taskImage", task.getTaskImageS3Key());
                sendTitle.putExtra("taskLongitude", task.getTaskLongitude());
                sendTitle.putExtra("taskLatitude", task.getTaskLatitude());
                sendTitle.putExtra(TASK_ID_TAG, task.getId());
                context.startActivity(sendTitle);
            });
        }
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
    public static class TaskViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView bodyTextView;
        private TextView stateTextView;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.textViewTitle);
            bodyTextView = itemView.findViewById(R.id.textViewDescription);
            stateTextView = itemView.findViewById(R.id.textViewStatus);
        }

        public void bindTask(Task task) {
            titleTextView.setText(task.getName());
            bodyTextView.setText(task.getBody());
            stateTextView.setText(task.getState().toString());
        }
    }
}
