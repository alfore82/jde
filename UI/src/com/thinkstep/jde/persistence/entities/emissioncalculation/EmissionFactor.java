/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.emissioncalculation;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author forell
 */
@Entity
public class EmissionFactor {
    @Id @GeneratedValue
    private long id;
    
    private String uuid;
    private String name;
    
    @Enumerated(EnumType.STRING)
    private EmissionStage emissionStage = EmissionStage.TANKTOWHEEL;
    private double GHGEmissions;

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

    public EmissionStage getEmissionStage() {
        return emissionStage;
    }

    public void setEmissionStage(EmissionStage emissionStage) {
        this.emissionStage = emissionStage;
    }

    public double getGHGEmissions() {
        return GHGEmissions;
    }

    public void setGHGEmissions(double GHGEmissions) {
        this.GHGEmissions = GHGEmissions;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    
    

    
    
    
    
    
    
}
