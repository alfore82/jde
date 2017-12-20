/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.rawdata;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author forell
 */
public class DataItem implements Serializable{
    
   
    
    private DataKey dataKey;
    
    private DataType dataType;
    private String valueString;
    private double valueNumeric;
    
    public DataItem() {
        dataKey = null;
        dataType = DataType.TEXT;
        valueString = "";
        valueNumeric = 0.0;
    }

    public DataItem(DataKey dataKey, String valueString) {
        this.dataKey = dataKey;
        this.dataType = DataType.TEXT;
        this.valueString = valueString;
        this.valueNumeric = 0.0;
    }

    public DataItem(DataKey dataKey, double valueNumeric) {
        this.dataKey = dataKey;
        this.dataType = DataType.NUMERIC;
        this.valueString = "";
        this.valueNumeric = valueNumeric;
    }

    
    public DataKey getDataKey() {
        return dataKey;
    }

    private void setDataKey(DataKey dataKey) {
        this.dataKey = dataKey;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueNumeric = 0.0;
        this.dataType = DataType.TEXT;
        this.valueString = valueString;
    }

    public double getValueNumeric() {
        return valueNumeric;
    }

    public void setValueNumeric(double valueNumeric) {
        this.dataType = DataType.TEXT;
        this.valueString = "";
        this.valueNumeric = valueNumeric;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DataItem other = (DataItem) obj;
        if (Double.doubleToLongBits(this.valueNumeric) != Double.doubleToLongBits(other.valueNumeric)) {
            return false;
        }
        if (!Objects.equals(this.valueString, other.valueString)) {
            return false;
        }
        if (this.dataType != other.dataType) {
            return false;
        }
        return true;
    }

    
    
    
    
    
    
    
    
}
