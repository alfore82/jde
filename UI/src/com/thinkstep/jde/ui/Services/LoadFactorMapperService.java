/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Services;


import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.LoadFactorMapping.LoadFactorMappingTopComponent;
import com.thinkstep.jde.ui.VehicleTypeMapping.VehicleTypeMappingTopComponent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author forell
 */
public class LoadFactorMapperService {
    private static LoadFactorMapperService substanceReportService = null;
    
    private List<LoadFactorMappingTopComponent> ctrls;
    
    protected LoadFactorMapperService(){
        ctrls = new ArrayList<>();
    }
    
    public static LoadFactorMapperService getInstance(){
        if (substanceReportService == null){
            substanceReportService = new LoadFactorMapperService();
        }
        return substanceReportService;
    }
    
    public LoadFactorMappingTopComponent createLoadFactorMappingTopComponent(LogisticServiceProvider s){
        
        for (LoadFactorMappingTopComponent m:ctrls){
            if (m.getLsp().equals(s)){
                return m;
            }
        }
        LoadFactorMappingTopComponent nm = new LoadFactorMappingTopComponent(s);
        ctrls.add(nm);
        return nm;
    }
    
    public LoadFactorMappingTopComponent getShowingComponent() throws MoreThanOneViewActiveException{
        int i = 0;
        LoadFactorMappingTopComponent mr = null;
        
        for (LoadFactorMappingTopComponent m:ctrls){
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
    
    public List<LoadFactorMappingTopComponent> getComponents(){
        return ctrls;
    }
    
    public void removeComponent(LoadFactorMappingTopComponent mms){
        this.ctrls.remove(mms);
        
    }
    
    
}
