/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Services;


import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.ShipmentWeightMapping.ShipmentWeightMappingTopComponent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author forell
 */
public class ShipmentWeightMapperService {
    private static ShipmentWeightMapperService substanceReportService = null;
    
    private List<ShipmentWeightMappingTopComponent> ctrls;
    
    protected ShipmentWeightMapperService(){
        ctrls = new ArrayList<>();
    }
    
    public static ShipmentWeightMapperService getInstance(){
        if (substanceReportService == null){
            substanceReportService = new ShipmentWeightMapperService();
        }
        return substanceReportService;
    }
    
    public ShipmentWeightMappingTopComponent createShipmentWeightMappingTopComponent(LogisticServiceProvider s){
        
        for (ShipmentWeightMappingTopComponent m:ctrls){
            if (m.getLsp().equals(s)){
                return m;
            }
        }
        ShipmentWeightMappingTopComponent nm = new ShipmentWeightMappingTopComponent(s);
        ctrls.add(nm);
        return nm;
    }
    
    public ShipmentWeightMappingTopComponent getShowingComponent() throws MoreThanOneViewActiveException{
        int i = 0;
        ShipmentWeightMappingTopComponent mr = null;
        
        for (ShipmentWeightMappingTopComponent m:ctrls){
            if (m.isShowing()){
                mr = m;
                i++;
            }
        }
        
        if (i > 1) {
            throw new MoreThanOneViewActiveException("More than one view for Substance useage is visible");
        } else {
            return mr;
        }
    }
    
    public List<ShipmentWeightMappingTopComponent> getComponents(){
        return ctrls;
    }
    
    public void removeComponent(ShipmentWeightMappingTopComponent mms){
        this.ctrls.remove(mms);
        
    }
    
    
}
