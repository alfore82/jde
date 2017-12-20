/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Services;


import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.FuelTypeMapping.FuelTypeMappingTopComponent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author forell
 */
public class FuelTypeMapperService {
    private static FuelTypeMapperService substanceReportService = null;
    
    private List<FuelTypeMappingTopComponent> ctrls;
    
    protected FuelTypeMapperService(){
        ctrls = new ArrayList<>();
    }
    
    public static FuelTypeMapperService getInstance(){
        if (substanceReportService == null){
            substanceReportService = new FuelTypeMapperService();
        }
        return substanceReportService;
    }
    
    public FuelTypeMappingTopComponent createFuelTypeMappingTopComponent(LogisticServiceProvider s){
        
        for (FuelTypeMappingTopComponent m:ctrls){
            if (m.getLsp().equals(s)){
                return m;
            }
        }
        FuelTypeMappingTopComponent nm = new FuelTypeMappingTopComponent(s);
        ctrls.add(nm);
        return nm;
    }
    
    public FuelTypeMappingTopComponent getShowingComponent() throws MoreThanOneViewActiveException{
        int i = 0;
        FuelTypeMappingTopComponent mr = null;
        
        for (FuelTypeMappingTopComponent m:ctrls){
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
    
    public List<FuelTypeMappingTopComponent> getComponents(){
        return ctrls;
    }
    
    public void removeComponent(FuelTypeMappingTopComponent mms){
        this.ctrls.remove(mms);
        
    }
    
    
}
