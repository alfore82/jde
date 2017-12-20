/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.FuelTypeMapping;

import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.RuleBuilder.RuleBuilder;
import com.thinkstep.jde.ui.RuleBuilder.FuelTypeAction.RuleBulderFuelTypeAction;
import com.thinkstep.jde.ui.RuleBuilder.RawDataAction.RuleBuilderGHGEmissionsAction;

/**
 *
 * @author forell
 */
public class FuelTypeMapper extends RuleBuilder{
    
    public FuelTypeMapper(LogisticServiceProvider lsp){
        super(lsp);
        this.label.setText("GHG mapping: "+ lsp.getName());
        this.mappingType = MappingType.FUELTYPE;
        this.actionTypes.add(RuleBulderFuelTypeAction.class);
        this.actionTypes.add(RuleBuilderGHGEmissionsAction.class);
        this.load();
    }
    
    
   
}
