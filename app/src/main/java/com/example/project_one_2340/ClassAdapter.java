package com.example.project_one_2340;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.ViewHolder> {

    private List<ClassItem> classItems;
    // Assuming you have interfaces for click listeners
    private OnItemClickListener onEditClickListener;
    private OnItemClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnEditClickListener(OnItemClickListener listener) {
        onEditClickListener = listener;
    }

    public void setOnDeleteClickListener(OnItemClickListener listener) {
        onDeleteClickListener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView className;
        final TextView professorName;
        final TextView meetingTime;
        final Button editButton;
        final Button deleteButton;

        public ViewHolder(View view, final OnItemClickListener editListener, final OnItemClickListener deleteListener) {
            super(view);
            className = view.findViewById(R.id.title_1);
            professorName = view.findViewById(R.id.subtitle_2);
            meetingTime = view.findViewById(R.id.subtitle_1);
            editButton = view.findViewById(R.id.btn_edit);
            deleteButton = view.findViewById(R.id.btn_delete);

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            editListener.onItemClick(position);
                        }
                    }
                }
            });
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (deleteListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            deleteListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public ClassAdapter(List<ClassItem> items) {
        classItems = items;
    }

    @Override
    public ClassAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gen_item, parent, false);
        return new ViewHolder(view, onEditClickListener, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ClassItem item = classItems.get(position);
        holder.className.setText(item.getName());
        holder.professorName.setText(item.getProf());
        holder.meetingTime.setText(item.getMeetingTime());
        // No need to set click listeners here since it's done in ViewHolder constructor
    }

    @Override
    public int getItemCount() {
        return classItems.size();
    }
}