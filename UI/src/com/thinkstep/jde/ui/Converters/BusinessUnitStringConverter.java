/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.BusinessUnit;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class BusinessUnitStringConverter extends StringConverter<BusinessUnit>{

    @Override
    public String toString(BusinessUnit object) {
        String result;
        if (object != null) {
            switch (object) {
                case PROFESSIONAL: 
                    result = "Professional";
                    break;
                case RETAIL:
                    result = "Retail";
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
    public BusinessUnit fromString(String string) {
        if (string.equals("Professional")){
            return BusinessUnit.PROFESSIONAL;
        } else if (string.equals("Retail")){
            return BusinessUnit.RETAIL;
        } else {
            return BusinessUnit.UNDEFINDED;
        }
    }
    
}
