/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities.Rules;

/**
 *
 * @author forell
 */
public enum RuleType {
    LARGER, LARGEREQUAL,SMALLER, SMALLEREQUAL,IDENTITY,BEGINSWITH, ENDSWITH, CONTAINS, EQUALS, 
    NUMBEROFPALLETSPERTOURLARGER, NUMBEROFPALLETSPERTOURSMALLER, NUMBEROFPALLETSPERTOURLARGEREQUAL, 
    NUMBEROFPALLETSPERTOURSMALLEREQUAL, PALLETUTILIZATIONLARGER, PALLETUTILIZATIONLARGEREQUAL, 
    PALLETUTILIZATIONSMALLER, PALLETUTILIZATIONSMALLEREQUAL
}
