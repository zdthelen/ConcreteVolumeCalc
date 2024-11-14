package com.zachthelen.concretevolumecalculator;

import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

public class WallViewModel extends ViewModel {
    private ArrayList<WallGroup> wallGroups = new ArrayList<>();

    public ArrayList<WallGroup> getWallGroups() {
        return wallGroups;
    }

    public void setWallGroups(ArrayList<WallGroup> wallGroups) {
        this.wallGroups = wallGroups;
    }
}
