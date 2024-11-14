package com.zachthelen.concretevolumecalculator;
// FootingActivity.java

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class FootingActivity extends AppCompatActivity {
    private ArrayList<String> lengthMeasurements = new ArrayList<>();
    private RecyclerView recyclerView;
    private FootingAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_footing);

        Button buttonNewFooting = findViewById(R.id.buttonNewFooting);
        recyclerView = findViewById(R.id.recyclerViewFooting);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FootingAdapter(lengthMeasurements);
        recyclerView.setAdapter(adapter);

        buttonNewFooting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openWidthDepthDialog();
            }
        });

        // Inside FootingActivity.java

            Button buttonDeleteOptions = findViewById(R.id.buttonDeleteOptions);
            buttonDeleteOptions.setOnClickListener(v -> showDeleteOptionsDialog());
        }

        private void showDeleteOptionsDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Delete Options")
                    .setItems(new String[]{"Delete All", "Select Items to Delete"}, (dialog, which) -> {
                        if (which == 0) {  // Delete All
                            adapter.deleteAllItems();
                        } else if (which == 1) {  // Select Items to Delete
                            // Optionally enable item delete buttons
                            adapter.notifyDataSetChanged();
                        }
                    });
            builder.create().show();
        }



    private void openWidthDepthDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_width_depth, null);
        builder.setView(dialogView);

        EditText inputWidth = dialogView.findViewById(R.id.inputWidth);
        EditText inputDepth = dialogView.findViewById(R.id.inputDepth);
        Button buttonSave = dialogView.findViewById(R.id.buttonSaveWidthDepth);
        Button buttonCancel = dialogView.findViewById(R.id.buttonCancelWidthDepth);

        AlertDialog dialog = builder.create();

        buttonSave.setOnClickListener(v -> {
            saveWidthDepth(inputWidth.getText().toString(), inputDepth.getText().toString());
            dialog.dismiss();
            openLengthDialog();
        });

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void saveWidthDepth(String width, String depth) {
        SharedPreferences prefs = getSharedPreferences("ConcretePrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("width", width);
        editor.putString("depth", depth);
        editor.apply();
    }

    private void openLengthDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_length, null);
        builder.setView(dialogView);

        EditText inputFeet = dialogView.findViewById(R.id.inputLengthFeet);
        EditText inputInches = dialogView.findViewById(R.id.inputLengthInches);
        Button buttonNext = dialogView.findViewById(R.id.buttonNextMeasurement);
        Button buttonFinish = dialogView.findViewById(R.id.buttonFinish);

        AlertDialog dialog = builder.create();

        buttonNext.setOnClickListener(v -> {
            saveLengthMeasurement(inputFeet.getText().toString(), inputInches.getText().toString());
            inputFeet.setText("");
            inputInches.setText("");
        });

        buttonFinish.setOnClickListener(v -> {
            calculateVolume();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void saveLengthMeasurement(String feet, String inches) {
        // Convert to inches and save to list
        int totalInches = Integer.parseInt(feet) * 12 + Integer.parseInt(inches);
        lengthMeasurements.add(String.valueOf(totalInches));
        adapter.notifyDataSetChanged();
    }

    private void calculateVolume() {
        SharedPreferences prefs = getSharedPreferences("ConcretePrefs", Context.MODE_PRIVATE);
        int width = Integer.parseInt(prefs.getString("width", "0"));
        int depth = Integer.parseInt(prefs.getString("depth", "0"));

        int totalLengthInches = 0;
        for (String length : lengthMeasurements) {
            totalLengthInches += Integer.parseInt(length);
        }

        double cubicInches = totalLengthInches * width * depth;
        double cubicYards = cubicInches / 46656; // 46656 cubic inches in a cubic yard

        lengthMeasurements.clear();  // Reset lengths for new calculations
        adapter.notifyDataSetChanged();

        // Show result in RecyclerView
        lengthMeasurements.add("Volume: " + cubicYards + " cubic yards");
        adapter.notifyDataSetChanged();

        // Clear SharedPreferences
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove("width");
        editor.remove("depth");
        editor.apply();
    }
}
