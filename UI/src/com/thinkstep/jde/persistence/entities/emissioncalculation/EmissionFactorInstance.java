/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.emissioncalculation;

import com.thinkstep.jde.persistence.services.EmissionFactorService;
import java.util.List;

/**
 *
 * @author forell
 */
public class EmissionFactorInstance {
    private EmissionFactor emissionFactor = null;
    private double scalingFactor = 1.0;
    private Parameter parameter = null;

    public EmissionFactor getEmissionFactor() {
        return emissionFactor;
    }

    public void setEmissionFactor(EmissionFactor emissionFactor) {
        this.emissionFactor = emissionFactor;
    }

    public double getScalingFactor() {
        return scalingFactor;
    }

    public void setScalingFactor(double scalingFactor) {
        this.scalingFactor = scalingFactor;
    }

    public Parameter getParameter() {
        return parameter;
    }

    public void setParameter(Parameter parameter) {
        this.parameter = parameter;
    }
    
    @Override
    public String toString(){
        return "EmissionFactor:" + emissionFactor.getId() + ";ScalingFactor:" + scalingFactor + ";Parameter:" + parameter.getName();
    }
    
    public static EmissionFactorInstance fromString(String emissionfactor, List<Parameter> params){
        EmissionFactorInstance efi = new EmissionFactorInstance();
        EmissionFactorService efs = EmissionFactorService.getINSTANCE();
        
        String[] splits = emissionfactor.split(";");
        long id = Long.parseLong(splits[0].replace("EmissionFactor:", ""));
        double scalingFactor = Double.parseDouble(splits[1].replace("ScalingFactor:", ""));
        String parameter = splits[2].replace("Parameter:", "");
        efi.setEmissionFactor(efs.findEmissionFactor(id));
        efi.setScalingFactor(scalingFactor);
        for (Parameter p:params){
            if (p.getName().equals(parameter)){
                efi.setParameter(p);
            }
        }
        
        return efi;
        
        
    }
    
    
}
