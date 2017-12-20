/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.Rules;

import com.thinkstep.jde.persistence.entities.fuelconsumptioncalculation.VehicleType;
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
public class AssignVehicleTypeAction {
    @Id @GeneratedValue
    private long id;
    @Enumerated(EnumType.STRING)
    private FieldName name;
    
    private VehicleType VehicleType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public FieldName getName() {
        return name;
    }

    public void setName(FieldName name) {
        this.name = name;
    }

    public VehicleType getVehicleType() {
        return VehicleType;
    }

    public void setVehicleType(VehicleType VehicleType) {
        this.VehicleType = VehicleType;
    }

    
    
    
    
    
}
