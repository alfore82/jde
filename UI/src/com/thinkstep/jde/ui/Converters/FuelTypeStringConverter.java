/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionProcess;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class FuelTypeStringConverter extends StringConverter<EmissionProcess>{

    @Override
    public String toString(EmissionProcess object) {
        return (object != null) ? object.getCountry().getName() + "\t" + object.getName()  : "";
    }

    @Override
    public EmissionProcess fromString(String string) {
        return null;
    }
    
}
