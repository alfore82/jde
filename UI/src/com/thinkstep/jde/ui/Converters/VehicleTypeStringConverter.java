/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.fuelconsumptioncalculation.VehicleType;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class VehicleTypeStringConverter extends StringConverter<VehicleType>{

    @Override
    public String toString(VehicleType object) {
        return (object != null) ? object.getName() : "";
    }

    @Override
    public VehicleType fromString(String string) {
        return null;
    }
    
}
