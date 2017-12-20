/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class DataKeyStringConverter extends StringConverter<DataKey>{

    @Override
    public String toString(DataKey object) {
        return (object != null) ? object.getKey() : "";
    }

    @Override
    public DataKey fromString(String string) {
        return null;
    }
    
}