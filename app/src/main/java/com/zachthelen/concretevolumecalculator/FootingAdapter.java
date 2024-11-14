package com.zachthelen.concretevolumecalculator;
// FootingAdapter.java

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

public class FootingAdapter extends RecyclerView.Adapter<FootingAdapter.ViewHolder> {
    private ArrayList<String> measurements;
    private Context context;

    public FootingAdapter(ArrayList<String> measurements) {
            //, Context context) {
        this.measurements = measurements;
        //this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_measurement, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String measurement = measurements.get(position);
        holder.textViewMeasurement.setText(measurement);

        holder.buttonDelete.setOnClickListener(v -> {
            showDeleteDialog(position);
        });
    }

    @Override
    public int getItemCount() {
        return measurements.size();
    }

    private void showDeleteDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Delete this entry?")
                .setPositiveButton("Yes", (dialog, id) -> {
                    measurements.remove(position);
                    notifyDataSetChanged();
                })
                .setNegativeButton("No", (dialog, id) -> dialog.dismiss());
        builder.create().show();
    }

    public void deleteAllItems() {
        measurements.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewMeasurement;
        Button buttonDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMeasurement = itemView.findViewById(R.id.textViewMeasurement);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
