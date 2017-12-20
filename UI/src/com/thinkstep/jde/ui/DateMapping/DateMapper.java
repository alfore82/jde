/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.DateMapping;

import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.RuleBuilder.DateAction.RuleBuilderDateAction;
import com.thinkstep.jde.ui.RuleBuilder.RuleBuilder;

/**
 *
 * @author forell
 */
public class DateMapper extends RuleBuilder{
    
    public DateMapper(LogisticServiceProvider lsp){
        super(lsp);
        this.label.setText("Date mapping: "+ lsp.getName());
        this.mappingType = MappingType.DATE;
        this.actionTypes.add(RuleBuilderDateAction.class);
        this.load();
    }
    
    
   
}
