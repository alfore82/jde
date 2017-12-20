/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.VehicleTypeMapping;

import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.RuleBuilder.RuleBuilder;
import com.thinkstep.jde.ui.RuleBuilder.VehicleTypeAction.RuleBulderVehicleTypeAction;

/**
 *
 * @author forell
 */
public class VehicleTypeMapper extends RuleBuilder{
    
    public VehicleTypeMapper(LogisticServiceProvider lsp){
        super(lsp);
        this.label.setText("Vehicle type mapping: "+ lsp.getName());
        this.mappingType = MappingType.VEHICLETYPE;
        this.actionTypes.add(RuleBulderVehicleTypeAction.class);
        this.load();
    }
    
    
   
}
