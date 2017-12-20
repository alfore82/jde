/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Services;


import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionProcess;
import com.thinkstep.jde.ui.ProcessEditor.EmissionProcessEditorTopComponent;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author forell
 */
public class EmissionProcessEditorService {
    private static EmissionProcessEditorService substanceReportService = null;
    
    private List<EmissionProcessEditorTopComponent> ctrls;
    
    protected EmissionProcessEditorService(){
        ctrls = new ArrayList<>();
    }
    
    public static EmissionProcessEditorService getInstance(){
        if (substanceReportService == null){
            substanceReportService = new EmissionProcessEditorService();
        }
        return substanceReportService;
    }
    
    public EmissionProcessEditorTopComponent createEmissionProcessEditorTopComponent(EmissionProcess s){
        
        for (EmissionProcessEditorTopComponent m:ctrls){
            if (m.getEmissionProcess().equals(s)){
                return m;
            }
        }
        EmissionProcessEditorTopComponent nm = new EmissionProcessEditorTopComponent(s);
        ctrls.add(nm);
        return nm;
    }
    
    public EmissionProcessEditorTopComponent getShowingComponent() throws MoreThanOneViewActiveException{
        int i = 0;
        EmissionProcessEditorTopComponent mr = null;
        
        for (EmissionProcessEditorTopComponent m:ctrls){
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
    
    public List<EmissionProcessEditorTopComponent> getComponents(){
        return ctrls;
    }
    
    public void removeComponent(EmissionProcessEditorTopComponent mms){
        this.ctrls.remove(mms);
        
    }
    
    
}
