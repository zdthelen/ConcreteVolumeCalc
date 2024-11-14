package com.zachthelen.concretevolumecalculator;

public class WallGroup {
    private double height;
    private double width;
    private double cumulativeLengthFeet;
    private double volumeCubicYards;

    // Constructor with parameters for height, width, cumulative length in feet, and volume in cubic yards
    public WallGroup(double height, double width, double cumulativeLengthFeet, double volumeCubicYards) {
        this.height = height;
        this.width = width;
        this.cumulativeLengthFeet = cumulativeLengthFeet;
        this.volumeCubicYards = volumeCubicYards;
    }

    // Getters for RecyclerView adapter to access and display data
    public double getHeight() {
        return height;
    }

    public double getWidth() {
        return width;
    }

    public double getCumulativeLengthFeet() {
        return cumulativeLengthFeet;
    }

    public double getVolumeCubicYards() {
        return (height * (width / 12.0) * cumulativeLengthFeet) / 27.0;
    }

}
