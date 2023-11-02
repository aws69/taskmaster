package com.asac26.taskmaster.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.asac26.taskmaster.R;
import com.asac26.taskmaster.TaskDetailsActivity;
import com.asac26.taskmaster.models.Task;

import java.util.List;


public class TasksRecyclerViewAdapter extends RecyclerView.Adapter<TasksRecyclerViewAdapter.TaskViewHolder> {
    private List<Task> taskList;
    private Context context;
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
        Task task = taskList.get(position);
        holder.bindTask(task);
        holder.itemView.setOnClickListener(view -> {
            Intent sendTitle = new Intent(context, TaskDetailsActivity.class);
            sendTitle.putExtra("taskTitle", task.getTitle());
            sendTitle.putExtra("taskBody", task.getBody());
            sendTitle.putExtra("taskStatus", task.getState());
            context.startActivity(sendTitle);
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
    public class TaskViewHolder extends RecyclerView.ViewHolder {
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
            titleTextView.setText(task.getTitle());
            bodyTextView.setText(task.getBody());
            stateTextView.setText(task.getState());
        }
    }
}
