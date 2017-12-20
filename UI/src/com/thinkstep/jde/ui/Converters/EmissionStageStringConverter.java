/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionStage;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class EmissionStageStringConverter extends StringConverter<EmissionStage>{

    @Override
    public String toString(EmissionStage object) {
        String result;
        if (object != null) {
            switch (object) {
                case WELLTOTANK: 
                    result = "Well to tank";
                    break;
                case TANKTOWHEEL:
                    result = "Tank to wheel";
                    break;
                default:
                    result = "";
            }
        } else {
            result = "";
        }
        return result;
    }

    @Override
    public EmissionStage fromString(String string) {
        if (string.equals("Well to tank")){
            return EmissionStage.WELLTOTANK;
        } else if (string.equals("Tank to wheel")){
            return EmissionStage.TANKTOWHEEL;
        } else {
            return null;
        }
    }
    
}
