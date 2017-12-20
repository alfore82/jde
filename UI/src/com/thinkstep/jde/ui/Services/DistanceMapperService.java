/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Services;


import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.DistanceMapping.DistanceMappingTopComponent;
import com.thinkstep.jde.ui.LoadFactorMapping.LoadFactorMappingTopComponent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author forell
 */
public class DistanceMapperService {
    private static DistanceMapperService substanceReportService = null;
    
    private List<DistanceMappingTopComponent> ctrls;
    
    protected DistanceMapperService(){
        ctrls = new ArrayList<>();
    }
    
    public static DistanceMapperService getInstance(){
        if (substanceReportService == null){
            substanceReportService = new DistanceMapperService();
        }
        return substanceReportService;
    }
    
    public DistanceMappingTopComponent createDistanceMappingTopComponent(LogisticServiceProvider s){
        
        for (DistanceMappingTopComponent m:ctrls){
            if (m.getLsp().equals(s)){
                return m;
            }
        }
        DistanceMappingTopComponent nm = new DistanceMappingTopComponent(s);
        ctrls.add(nm);
        return nm;
    }
    
    public DistanceMappingTopComponent getShowingComponent() throws MoreThanOneViewActiveException{
        int i = 0;
        DistanceMappingTopComponent mr = null;
        
        for (DistanceMappingTopComponent m:ctrls){
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
    
    public List<DistanceMappingTopComponent> getComponents(){
        return ctrls;
    }
    
    public void removeComponent(DistanceMappingTopComponent mms){
        this.ctrls.remove(mms);
        
    }
    
    
}
