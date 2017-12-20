/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;

import javafx.scene.paint.Color;
import javafx.scene.shape.CubicCurve;

/**
 *
 * @author forell
 */
public class Connector extends CubicCurve{

    private CubicCurve c = this;
    private Dock dockStart;
    
    public Connector(Dock dockStart, Dock dockEnd) {
        this.dockStart = dockStart;
        c.startXProperty().bind(dockStart.xProperty);
        c.startYProperty().bind(dockStart.yProperty);
        c.controlX1Property().bind(dockStart.xProperty);
        c.controlY1Property().bind(dockStart.yProperty.add(30));
        c.controlX2Property().bind(dockEnd.xProperty);
        c.controlY2Property().bind(dockEnd.yProperty.subtract(30));
        c.endXProperty().bind(dockEnd.xProperty);
        c.endYProperty().bind(dockEnd.yProperty);
        c.fillProperty().setValue(Color.TRANSPARENT);
        c.strokeProperty().setValue(Color.BLACK);
    }

    public Dock getDockStart() {
        return dockStart;
    }
    
    
    
}
