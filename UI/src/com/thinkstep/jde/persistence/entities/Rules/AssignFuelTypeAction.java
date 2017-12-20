/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.Rules;

import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionProcess;
import com.thinkstep.jde.persistence.entities.emissioncalculation.Parameter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author forell
 */
@Entity
public class AssignFuelTypeAction {
    @Id @GeneratedValue
    private long id;
    
    @Enumerated(EnumType.STRING)
    private FieldName name;

    @OneToOne
    private EmissionProcess emissionProcess;
    
    private String parameterSettings;
    @Transient
    private List<Parameter> parameters = new ArrayList<>();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public EmissionProcess getEmissionProcess() {
        return emissionProcess;
    }

    public void setEmissionProcess(EmissionProcess emissionProcess) {
        this.emissionProcess = emissionProcess;
    }

    public String getParameterSettings() {
        return parameterSettings;
    }

    public void setParameterSettings(String parameterSettings) {
        this.parameterSettings = parameterSettings;
    }

    public List<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }

    public FieldName getName() {
        return name;
    }

    public void setName(FieldName name) {
        this.name = name;
    }
    
    
    

    public void serialize(){
        parameterSettings = "";
        if (parameters.size()>0){
            for (Parameter param:parameters){
                parameterSettings = parameterSettings + param.toString() + "|";
            }
            parameterSettings = parameterSettings.substring(0, parameterSettings.length()-1);
        }
        
    }
    
    public void unserialize(){
        parameters.clear();
        if (!"".equals(parameterSettings)){
            String[] serials = parameterSettings.split("\\|");
            for (int i=0; i<serials.length; i++){
                parameters.add(Parameter.fromString(serials[i]));
            }
        }
    }
    
    public String getParametersDisplay(){
        String out = "";
        if (parameters.size()>0){
            for (Parameter p:parameters){
                NumberFormat nf = NumberFormat.getPercentInstance();
                out = out + p.getName() + " = " + nf.format(p.getValue()) + "; ";
            }
            out = out.substring(0,out.length()-2);
        }
        
        return out;
    }
    
    
    
    
}
