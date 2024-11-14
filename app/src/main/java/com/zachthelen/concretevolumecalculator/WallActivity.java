// WallActivity.java
package com.zachthelen.concretevolumecalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import androidx.lifecycle.ViewModelProvider;

public class WallActivity extends AppCompatActivity {
    private WallAdapter adapter;
    private double cumulativeLengthInches = 0;
    private double currentHeight, currentWidth;
    private WallViewModel wallViewModel;
    private ArrayList<WallGroup> wallGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        wallViewModel = new ViewModelProvider(this).get(WallViewModel.class);

        // Use the ViewModel's wallGroups list directly
        wallGroups = wallViewModel.getWallGroups();

        RecyclerView recyclerViewWall = findViewById(R.id.recyclerViewWall);
        recyclerViewWall.setLayoutManager(new LinearLayoutManager(this));

        adapter = new WallAdapter(wallGroups, this);
        recyclerViewWall.setAdapter(adapter);

        Button buttonNewWall = findViewById(R.id.buttonNewWall);
        buttonNewWall.setOnClickListener(v -> showHeightWidthDialog());

        Button buttonDeleteOptions = findViewById(R.id.buttonDeleteOptions);
        buttonDeleteOptions.setOnClickListener(v -> showDeleteOptionsDialog());

        // Update RecyclerView adapter if data changes after rotation
        adapter.notifyDataSetChanged();
    }

    private void saveWallGroup(WallGroup wallGroup) {
        wallViewModel.getWallGroups().add(wallGroup);
        adapter.notifyDataSetChanged();
    }

    private void showHeightWidthDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_height_width, null);
        EditText editTextHeight = dialogView.findViewById(R.id.editTextHeight);
        EditText editTextWidth = dialogView.findViewById(R.id.editTextWidth);

        new AlertDialog.Builder(this)
                .setTitle("New Wall - Height & Width")
                .setView(dialogView)
                .setPositiveButton("Save", (dialog, which) -> {
                    String heightStr = editTextHeight.getText().toString();
                    String widthStr = editTextWidth.getText().toString();
                    if (!heightStr.isEmpty() && !widthStr.isEmpty()) {
                        currentHeight = Double.parseDouble(heightStr);
                        currentWidth = Double.parseDouble(widthStr);
                        cumulativeLengthInches = 0;  // Reset cumulative length for new wall
                        showWallLengthsDialog();  // Show the wall lengths dialog
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showWallLengthsDialog() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_wall_lengths, null);
        EditText editTextFeet = dialogView.findViewById(R.id.editTextFeet);
        EditText editTextInches = dialogView.findViewById(R.id.editTextInches);

        new AlertDialog.Builder(this)
                .setTitle("Wall Lengths")
                .setView(dialogView)
                .setPositiveButton("Next Length", (dialog, which) -> {
                    saveWallLength(editTextFeet, editTextInches);
                    showWallLengthsDialog();  // Reopen the dialog for another length
                })
                .setNeutralButton("New Wall", (dialog, which) -> {
                    saveWallLength(editTextFeet, editTextInches);
                    saveCurrentWallGroup();  // Save this wall to the list
                    showHeightWidthDialog();  // Reopen the height & width dialog for a new wall
                })
                .setNegativeButton("Finish", (dialog, which) -> {
                    saveWallLength(editTextFeet, editTextInches);
                    saveCurrentWallGroup();  // Save this wall to the list
                })
                .show();
    }

    private void saveWallLength(EditText editTextFeet, EditText editTextInches) {
        String feetStr = editTextFeet.getText().toString();
        String inchesStr = editTextInches.getText().toString();
        int feet = feetStr.isEmpty() ? 0 : Integer.parseInt(feetStr);
        int inches = inchesStr.isEmpty() ? 0 : Integer.parseInt(inchesStr);
        double lengthInches = (feet * 12) + inches;
        cumulativeLengthInches += lengthInches;  // Add this length to the cumulative length
    }

    private void saveCurrentWallGroup() {
        // Convert cumulativeLengthInches to feet for calculations
        double cumulativeLengthFeet = cumulativeLengthInches / 12.0;
        double volumeCubicYards = (currentHeight * currentWidth * cumulativeLengthFeet) / 27.0;

        WallGroup newWallGroup = new WallGroup(currentHeight, currentWidth, cumulativeLengthFeet, volumeCubicYards);
        wallGroups.add(newWallGroup);
        adapter.notifyDataSetChanged();  // Update RecyclerView with the new wall entry

        // Reset cumulative length for the next wall entry
        cumulativeLengthInches = 0;
    }

    private void showDeleteOptionsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete Options")
                .setItems(new String[]{"Delete All", "Select Items to Delete"}, (dialog, which) -> {
                    if (which == 0) {  // Delete All
                        wallGroups.clear();
                        adapter.notifyDataSetChanged();
                    } else if (which == 1) {  // Select Items to Delete
                        adapter.enableItemDeletion();
                    }
                });
        builder.create().show();
    }
}
