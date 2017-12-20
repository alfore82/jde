/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.DistanceMapping;

import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.RuleBuilder.ConstantAction.RuleBuilderConstantDistanceAction;
import com.thinkstep.jde.ui.RuleBuilder.DistanceCalculationAction.DistanceCalculationAction;
import com.thinkstep.jde.ui.RuleBuilder.RawDataAction.RuleBuilderDistanceAction;
import com.thinkstep.jde.ui.RuleBuilder.RuleBuilder;

/**
 *
 * @author forell
 */
public class DistanceMapper extends RuleBuilder{
    
    public DistanceMapper(LogisticServiceProvider lsp){
        super(lsp);
        this.label.setText("Distance mapping: "+ lsp.getName());
        this.mappingType = MappingType.DISTANCE;
        this.actionTypes.add(RuleBuilderDistanceAction.class);
        this.actionTypes.add(RuleBuilderConstantDistanceAction.class);
        this.actionTypes.add(DistanceCalculationAction.class);
        this.load();
    }
    
    
   
}
