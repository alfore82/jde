/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.Rules.RuleType;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class RuleTypeStringConverter extends StringConverter<RuleType>{

    @Override
    public String toString(RuleType object) {
        switch (object){
            case LARGER:
                return ">";
            case LARGEREQUAL:
                return ">=";
            case SMALLER:
                return "<";
            case SMALLEREQUAL:
                return"<=";
            case IDENTITY:
                return "=";
            case BEGINSWITH:
                return "Begins with...";
            case ENDSWITH: 
                return "Ends with...";
            case CONTAINS:
                return "Contains...";
            case EQUALS:
                return "Equals...";
            case NUMBEROFPALLETSPERTOURLARGER:
                return "Num. Pal. per Tour >";
            case NUMBEROFPALLETSPERTOURSMALLER:
                return "Num. Pal. per Tour <";
            case NUMBEROFPALLETSPERTOURLARGEREQUAL:
                return "Num. Pal. per Tour >=";
            case NUMBEROFPALLETSPERTOURSMALLEREQUAL:
                return "Num. Pal. per Tour <=";
            case PALLETUTILIZATIONLARGER:
                return "Pal. Utilization >";
            case PALLETUTILIZATIONLARGEREQUAL:
                return "Pal. Utilization >=";
            case PALLETUTILIZATIONSMALLER:
                return "Pal. Utilization <";
            case PALLETUTILIZATIONSMALLEREQUAL:
                return "Pal. Utilization <=";
        }
        return null;
    }

    @Override
    public RuleType fromString(String string) {
        return null;
    }
    
}
