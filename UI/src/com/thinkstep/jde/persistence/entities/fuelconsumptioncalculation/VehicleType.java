/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.fuelconsumptioncalculation;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author forell
 */
@Entity
public class VehicleType {
    @Id @GeneratedValue
    private long id;
    
    private String name = "";
    private double maxPayload = 1.0;
    private double maxNumFloorPallets = 0.0;
    private double fuelConsumptionEmpty = 1.0;
    private double fuelConsumptionLoaded = 1.0;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxPayload() {
        return maxPayload;
    }

    public void setMaxPayload(double maxPayload) {
        this.maxPayload = maxPayload;
    }

    public double getMaxNumFloorPallets() {
        return maxNumFloorPallets;
    }

    public void setMaxNumFloorPallets(double maxNumFloorPallets) {
        this.maxNumFloorPallets = maxNumFloorPallets;
    }

    public double getFuelConsumptionEmpty() {
        return fuelConsumptionEmpty;
    }

    public void setFuelConsumptionEmpty(double fuelConsumptionEmpty) {
        this.fuelConsumptionEmpty = fuelConsumptionEmpty;
    }

    public double getFuelConsumptionLoaded() {
        return fuelConsumptionLoaded;
    }

    public void setFuelConsumptionLoaded(double fuelConsumptionLoaded) {
        this.fuelConsumptionLoaded = fuelConsumptionLoaded;
    }
    
    
    
    
    
}
