/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Services;


import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.ui.DateMapping.DateMappingTopComponent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author forell
 */
public class DateMapperService {
    private static DateMapperService dateMapperService = null;
    
    private List<DateMappingTopComponent> ctrls;
    
    protected DateMapperService(){
        ctrls = new ArrayList<>();
    }
    
    public static DateMapperService getInstance(){
        if (dateMapperService == null){
            dateMapperService = new DateMapperService();
        }
        return dateMapperService;
    }
    
    public DateMappingTopComponent createDateMappingTopComponent(LogisticServiceProvider s){
        
        for (DateMappingTopComponent m:ctrls){
            if (m.getLsp().equals(s)){
                return m;
            }
        }
        DateMappingTopComponent nm = new DateMappingTopComponent(s);
        ctrls.add(nm);
        return nm;
    }
    
    public DateMappingTopComponent getShowingComponent() throws MoreThanOneViewActiveException{
        int i = 0;
        DateMappingTopComponent mr = null;
        
        for (DateMappingTopComponent m:ctrls){
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
    
    public List<DateMappingTopComponent> getComponents(){
        return ctrls;
    }
    
    public void removeComponent(DateMappingTopComponent mms){
        this.ctrls.remove(mms);
        
    }
    
    
}
