/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.LoadFactorMapping;

import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.RuleBuilder.ConstantAction.RuleBuilderConstantLoadFactorAction;
import com.thinkstep.jde.ui.RuleBuilder.RuleBuilder;
import com.thinkstep.jde.ui.RuleBuilder.VehicleTypeAction.LoadFactorBasedTour.LoadCalculatonAction;

/**
 *
 * @author forell
 */
public class LoadFactorMapper extends RuleBuilder{
    
    public LoadFactorMapper(LogisticServiceProvider lsp){
        super(lsp);
        this.label.setText("Load factor mapping: "+ lsp.getName());
        this.mappingType = MappingType.LOADFACTOR;
        this.actionTypes.add(RuleBuilderConstantLoadFactorAction.class);
        this.actionTypes.add(LoadCalculatonAction.class);
        this.load();
    }
    
    
   
}
