/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionStage;
import javafx.util.StringConverter;

/**
 *
 * @author forell
 */
public class FieldNameStringConverter extends StringConverter<FieldName>{

    @Override
    public String toString(FieldName object) {
        String result;
        if (object != null) {
            switch (object) {
                case DATE:
                    result = "Shipment date";
                    break;
                case SHIPMENTWEIGHT:
                    result = "Shipment weight";
                    break;
                case NUMBEROFPALLETS:
                    result = "Number of pallets";
                    break;
                case DISTANCEMAIN:
                    result = "Distance main leg";
                    break;
                case VEHICLETYPEMAIN:
                    result = "Vehicle type main leg";
                    break;
                case LOADFACTORMAIN:
                    result = "Load factor main leg";
                    break;
                case EMPTYTRIPFACTORMAIN:
                    result = "Empty trip factor main leg";
                    break;
                case FUELCONSUMPTIONMAIN:
                    result = "Fuel consumed main leg";
                    break;
                case GHGEMISSIONSMAIN:
                    result = "GHG emissions main leg";
                    break;
                case DISTANCEPICKUP:
                    result = "Distance pick up leg";
                    break;
                case VEHICLETYPEPICKUP:
                    result = "Vehicle type pick up leg";
                    break;
                case LOADFACTORPICKUP:
                    result = "Load factor pick up leg";
                    break;
                case EMPTYTRIPFACTORPICKUP:
                    result = "Empty trip factor pick up leg";
                    break;
                case FUELCONSUMPTIONPICKUP:
                    result = "Fuel consumed pick up leg";
                    break;
                case GHGEMISSIONSPICKUP:
                    result = "GHG emissions pick up leg";
                    break;
                case DISTANCEDELIVERY:
                    result = "Distance delivery leg";
                    break;
                case VEHICLETYPEDELIVERY:
                    result = "Vehicle type delivery leg";
                    break;
                case LOADFACTORDELIVERY:
                    result = "Load factor delivery leg";
                    break;
                case EMPTYTRIPFACTORDELIVERY:
                    result = "Empty trip factor delivery leg";
                    break;
                case FUELCONSUMPTIONDELIVERY:
                    result = "Fuel consumed delivery leg";
                    break;
                case GHGEMISSIONSDELIVERY:
                    result = "GHG emissions delivery leg";
                    break;
                case BUSINESSUNIT:
                    result = "Business unit";
                    break;
                case FUELTYPEPICKUP:
                    result = "Fuel type pick up leg";
                    break;
                case FUELTYPEMAIN:
                    result = "Fuel type main leg";
                    break;
                case FUELTYPEDELIVERY:
                    result = "Fuel type delivery leg";
                    break;
                default:
                    result = "";
            }
        } else {
            result = "";
        }
        return result;
    }

    @Override
    public FieldName fromString(String string) {
        if (string.equals("Shipment date")){
            return FieldName.DATE;
        } else if (string.equals("Shipment weight")){
            return FieldName.SHIPMENTWEIGHT;
        } else if (string.equals("Number of pallets")){
            return FieldName.NUMBEROFPALLETS;
        } else if (string.equals("Distance main leg")){
            return FieldName.DISTANCEMAIN;
        } else if (string.equals("Vehicle type main leg")){
            return FieldName.VEHICLETYPEMAIN;
        } else if (string.equals("Load factor main leg")){
            return FieldName.LOADFACTORMAIN;
        } else if (string.equals("Empty trip factor main leg")){
            return FieldName.EMPTYTRIPFACTORMAIN;
        } else if (string.equals("Fuel consumed main leg")){
            return FieldName.FUELCONSUMPTIONMAIN;
        } else if (string.equals("GHG emissions main leg")){
            return FieldName.GHGEMISSIONSMAIN;
        } if (string.equals("Distance pick up leg")){
            return FieldName.DISTANCEPICKUP;
        } else if (string.equals("Vehicle type pick up leg")){
            return FieldName.VEHICLETYPEPICKUP;
        } else if (string.equals("Load factor pick up leg")){
            return FieldName.LOADFACTORPICKUP;
        } else if (string.equals("Empty trip pick up leg")){
            return FieldName.EMPTYTRIPFACTORPICKUP;
        } else if (string.equals("Fuel consumed pick up leg")){
            return FieldName.FUELCONSUMPTIONPICKUP;
        } else if (string.equals("GHG emissions pick up leg")){
            return FieldName.GHGEMISSIONSPICKUP;
        } if (string.equals("Distance delivery leg")){
            return FieldName.DISTANCEDELIVERY;
        } else if (string.equals("Vehicle type delivery leg")){
            return FieldName.VEHICLETYPEDELIVERY;
        } else if (string.equals("Load factor delivery leg")){
            return FieldName.LOADFACTORDELIVERY;
        } else if (string.equals("Empty trip delivery leg")){
            return FieldName.EMPTYTRIPFACTORDELIVERY;
        } else if (string.equals("Fuel consumed delivery leg")){
            return FieldName.FUELCONSUMPTIONDELIVERY;
        } else if (string.equals("GHG emissions delivery leg")){
            return FieldName.GHGEMISSIONSDELIVERY;
        } else if (string.equals("Business unit")){
            return FieldName.BUSINESSUNIT;
        } else {
            return null;
        }
    }
    
}
