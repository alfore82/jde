/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;

import java.util.Objects;
import javafx.beans.property.SimpleDoubleProperty;

/**
 *
 * @author forell
 */
public class Dock {
    public SimpleDoubleProperty xProperty = new SimpleDoubleProperty();
    public SimpleDoubleProperty yProperty = new SimpleDoubleProperty();
    
    public Dock() {
    }
    
    public Dock(double x, double y) {
        this.xProperty.set(x);
        this.yProperty.set(y);
        
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
        final Dock other = (Dock) obj;
        if (!Objects.equals(this.xProperty.getValue(), other.xProperty.getValue())) {
            return false;
        }
        if (!Objects.equals(this.yProperty.getValue(), other.yProperty.getValue())) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Dock{" + "xProperty=" + xProperty.getValue() + ", yProperty=" + yProperty.getValue() + '}';
    }
    
    
    
    
    
}
