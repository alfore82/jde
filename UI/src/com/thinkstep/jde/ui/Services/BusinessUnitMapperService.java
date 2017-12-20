/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Services;


import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.BusinessUnitMapping.BusinessUnitMappingTopComponent;
import com.thinkstep.jde.ui.LoadFactorMapping.LoadFactorMappingTopComponent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author forell
 */
public class BusinessUnitMapperService {
    private static BusinessUnitMapperService substanceReportService = null;
    
    private List<BusinessUnitMappingTopComponent> ctrls;
    
    protected BusinessUnitMapperService(){
        ctrls = new ArrayList<>();
    }
    
    public static BusinessUnitMapperService getInstance(){
        if (substanceReportService == null){
            substanceReportService = new BusinessUnitMapperService();
        }
        return substanceReportService;
    }
    
    public BusinessUnitMappingTopComponent createBusinessUnitMappingTopComponent(LogisticServiceProvider s){
        
        for (BusinessUnitMappingTopComponent m:ctrls){
            if (m.getLsp().equals(s)){
                return m;
            }
        }
        BusinessUnitMappingTopComponent nm = new BusinessUnitMappingTopComponent(s);
        ctrls.add(nm);
        return nm;
    }
    
    public BusinessUnitMappingTopComponent getShowingComponent() throws MoreThanOneViewActiveException{
        int i = 0;
        BusinessUnitMappingTopComponent mr = null;
        
        for (BusinessUnitMappingTopComponent m:ctrls){
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
    
    public List<BusinessUnitMappingTopComponent> getComponents(){
        return ctrls;
    }
    
    public void removeComponent(BusinessUnitMappingTopComponent mms){
        this.ctrls.remove(mms);
        
    }
    
    
}
