/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Services;


import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.VehicleTypeMapping.VehicleTypeMappingTopComponent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author forell
 */
public class VehicleTypeMapperService {
    private static VehicleTypeMapperService substanceReportService = null;
    
    private List<VehicleTypeMappingTopComponent> ctrls;
    
    protected VehicleTypeMapperService(){
        ctrls = new ArrayList<>();
    }
    
    public static VehicleTypeMapperService getInstance(){
        if (substanceReportService == null){
            substanceReportService = new VehicleTypeMapperService();
        }
        return substanceReportService;
    }
    
    public VehicleTypeMappingTopComponent createVehicleTypeMappingTopComponent(LogisticServiceProvider s){
        
        for (VehicleTypeMappingTopComponent m:ctrls){
            if (m.getLsp().equals(s)){
                return m;
            }
        }
        VehicleTypeMappingTopComponent nm = new VehicleTypeMappingTopComponent(s);
        ctrls.add(nm);
        return nm;
    }
    
    public VehicleTypeMappingTopComponent getShowingComponent() throws MoreThanOneViewActiveException{
        int i = 0;
        VehicleTypeMappingTopComponent mr = null;
        
        for (VehicleTypeMappingTopComponent m:ctrls){
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
    
    public List<VehicleTypeMappingTopComponent> getComponents(){
        return ctrls;
    }
    
    public void removeComponent(VehicleTypeMappingTopComponent mms){
        this.ctrls.remove(mms);
        
    }
    
    
}
