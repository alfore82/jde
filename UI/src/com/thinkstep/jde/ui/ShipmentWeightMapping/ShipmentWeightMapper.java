/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.ShipmentWeightMapping;

import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.RuleBuilder.RawDataAction.RuleBuilderShipmentWeightAction;
import com.thinkstep.jde.ui.RuleBuilder.RuleBuilder;

/**
 *
 * @author forell
 */
public class ShipmentWeightMapper extends RuleBuilder{
    
    public ShipmentWeightMapper(LogisticServiceProvider lsp){
        super(lsp);
        this.label.setText("Shipment weight mapping: "+ lsp.getName());
        this.mappingType = MappingType.SHIPMENTWEIGHT;
        this.actionTypes.add(RuleBuilderShipmentWeightAction.class);
        this.load();
    }
    
    
   
}
