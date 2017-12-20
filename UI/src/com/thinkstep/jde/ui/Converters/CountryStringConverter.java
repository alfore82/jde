/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.Country;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class CountryStringConverter extends StringConverter<Country>{

    @Override
    public String toString(Country object) {
        return (object != null) ? object.getName() : "";
    }

    @Override
    public Country fromString(String string) {
        return null;
    }
    
}
