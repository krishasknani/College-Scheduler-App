package com.example.project_one_2340;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class GenericAdapter extends RecyclerView.Adapter<GenericAdapter.ViewHolder> {

    private List<GenericItem> items;
    private OnItemClickListener onEditClickListener;
    private OnItemClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public GenericAdapter(List<GenericItem> items) {
        this.items = items;
    }

    public void setOnEditClickListener(OnItemClickListener listener) {
        this.onEditClickListener = listener;
    }

    public void setOnDeleteClickListener(OnItemClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gen_item, parent, false);
        return new ViewHolder(view, onEditClickListener, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GenericItem item = items.get(position);
        holder.title.setText(item.getTitle());
        holder.subtitle1.setText(item.getSubtitle1());
        holder.subtitle2.setText(item.getSubtitle2());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle1, subtitle2;
        Button editButton, deleteButton;

        ViewHolder(View view, final OnItemClickListener editListener, final OnItemClickListener deleteListener) {
            super(view);
            title = view.findViewById(R.id.title_1);
            subtitle1 = view.findViewById(R.id.subtitle_1);
            subtitle2 = view.findViewById(R.id.subtitle_2);
            editButton = view.findViewById(R.id.btn_edit);
            deleteButton = view.findViewById(R.id.btn_delete);

            editButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && editListener != null) {
                    editListener.onItemClick(position);
                }
            });

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && deleteListener != null) {
                    deleteListener.onItemClick(position);
                }
            });
        }
    }
}
