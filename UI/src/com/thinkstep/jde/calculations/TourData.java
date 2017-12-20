/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.fuelconsumptioncalculation.VehicleType;

/**
 *
 * @author forell
 */
public class TourData {
    private double mass;
    private double numpallets;
    private double distance;
    private VehicleType vehicleType;
    private int numVehicle = 0;

    public double getMass() {
        return mass;
    }

    public void setMass(double mass) {
        this.mass = mass;
    }

    public double getNumpallets() {
        return numpallets;
    }

    public void setNumpallets(double numpallets) {
        this.numpallets = numpallets;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(VehicleType vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getNumVehicle() {
        return numVehicle;
    }

    public void setNumVehicle(int numVehicle) {
        this.numVehicle = numVehicle;
    }
    
    
    
}
