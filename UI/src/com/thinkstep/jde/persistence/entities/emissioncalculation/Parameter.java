/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.emissioncalculation;

/**
 *
 * @author forell
 */
public class Parameter {
    private String name;
    private double value;
    private boolean balance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public boolean isBalance() {
        return balance;
    }

    public void setBalance(boolean balance) {
        this.balance = balance;
    }
    
    public String toString(){
        return "Name:" + name +";"+ "Value:" + value +";"+ "Balance:" + balance;
    }
    
    public static Parameter fromString(String serial){
        if (serial!=null && !serial.equals("")){
            String parts[] = serial.split(";");
            Parameter p = new Parameter();
            p.setName(parts[0].replace("Name:", ""));
            p.setValue(Double.parseDouble(parts[1].replace("Value:", "")));
            p.setBalance(Boolean.parseBoolean(parts[2].replace("Balance:", "")));
            return p;
        }
        return null;
    }
    
}
