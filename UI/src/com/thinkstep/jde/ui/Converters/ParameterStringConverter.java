/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.emissioncalculation.Parameter;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class ParameterStringConverter extends StringConverter<Parameter>{

    @Override
    public String toString(Parameter object) {
        return (object != null) ? object.getName() : "";
    }

    @Override
    public Parameter fromString(String string) {
        return null;
    }
    
}