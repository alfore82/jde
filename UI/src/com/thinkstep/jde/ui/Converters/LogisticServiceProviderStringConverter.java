/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class LogisticServiceProviderStringConverter extends StringConverter<LogisticServiceProvider>{

    @Override
    public String toString(LogisticServiceProvider object) {
        return (object != null) ? object.getName() : "";
    }

    @Override
    public LogisticServiceProvider fromString(String string) {
        return null;
    }
    
}
