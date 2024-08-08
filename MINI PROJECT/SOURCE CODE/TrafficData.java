package com.mycompany.smarttrafficsignal;

public class TrafficData {
    private int vehicleCount;
    private double averageSpeed;
    private String intersectionName;

    public TrafficData(int vehicleCount, double averageSpeed, String intersectionName) {
        this.vehicleCount = vehicleCount;
        this.averageSpeed = averageSpeed;
        this.intersectionName = intersectionName;
    }

    public int getVehicleCount() {
        return vehicleCount;
    }

    public double getAverageSpeed() {
        return averageSpeed;
    }

    public String getIntersectionName() {
        return intersectionName;
    }

    @Override
    public String toString() {
        return String.format("Intersection: %s, Vehicle Count: %d, Average Speed: %.2f",
                intersectionName, vehicleCount, averageSpeed);
    }
}
