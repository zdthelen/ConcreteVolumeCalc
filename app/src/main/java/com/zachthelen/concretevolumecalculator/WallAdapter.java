package com.zachthelen.concretevolumecalculator;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class WallAdapter extends RecyclerView.Adapter<WallAdapter.WallViewHolder> {
    private ArrayList<WallGroup> wallGroups;
    private Context context;
    private boolean isDeletingEnabled = false;

    public WallAdapter(ArrayList<WallGroup> wallGroups, Context context) {
        this.wallGroups = wallGroups;
        this.context = context;
    }

    @NonNull
    @Override
    public WallViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_wall_volume, parent, false);
        return new WallViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WallViewHolder holder, int position) {
        WallGroup wallGroup = wallGroups.get(position);
        holder.textViewHeight.setText("Height: " + wallGroup.getHeight() + " ft");
        holder.textViewWidth.setText("Width: " + wallGroup.getWidth() + " in");
        holder.textViewCumulativeLength.setText("Length: " + wallGroup.getCumulativeLengthFeet() + " ft");
        holder.textViewVolume.setText("Volume: " + String.valueOf(wallGroup.getVolumeCubicYards()) + " cubic yards");


        // Show or hide the delete button based on isDeletingEnabled
        holder.buttonDelete.setVisibility(isDeletingEnabled ? View.VISIBLE : View.GONE);

        // Handle delete button click
        holder.buttonDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        wallGroups.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return wallGroups.size();
    }

    // Toggle delete mode
    public void enableItemDeletion() {
        isDeletingEnabled = !isDeletingEnabled;
        notifyDataSetChanged();
    }

    // Delete all items from the list and notify RecyclerView
    public void deleteAllItems() {
        wallGroups.clear();
        notifyDataSetChanged();
    }

    public static class WallViewHolder extends RecyclerView.ViewHolder {
        TextView textViewHeight, textViewWidth, textViewCumulativeLength, textViewVolume;
        Button buttonDelete;

        public WallViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHeight = itemView.findViewById(R.id.textViewHeight);
            textViewWidth = itemView.findViewById(R.id.textViewWidth);
            textViewCumulativeLength = itemView.findViewById(R.id.textViewCumulativeLength);
            textViewVolume = itemView.findViewById(R.id.textViewVolume);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
