/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.BusinessUnitMapping;

import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.RuleBuilder.AssignBuisnessUnitAction.RuleBuilderBusinessUnitAction;
import com.thinkstep.jde.ui.RuleBuilder.RuleBuilder;

/**
 *
 * @author forell
 */
public class BusinessUnitMapper extends RuleBuilder{
    
    public BusinessUnitMapper(LogisticServiceProvider lsp){
        super(lsp);
        this.label.setText("Buisness unit mapping: "+ lsp.getName());
        this.mappingType = MappingType.BUISNESSUNIT;
        this.actionTypes.add(RuleBuilderBusinessUnitAction.class);
        this.load();
    }
    
    
   
}
