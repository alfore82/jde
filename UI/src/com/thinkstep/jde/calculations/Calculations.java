/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.Location;
import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.AssignBusinessUnitAction;
import com.thinkstep.jde.persistence.entities.Rules.AssignConstantAction;
import com.thinkstep.jde.persistence.entities.Rules.AssignDateAction;
import com.thinkstep.jde.persistence.entities.Rules.AssignFuelTypeAction;
import com.thinkstep.jde.persistence.entities.Rules.AssignValueAction;
import com.thinkstep.jde.persistence.entities.Rules.AssignVehicleTypeAction;
import com.thinkstep.jde.persistence.entities.Rules.CalculateDistanceAction;
import com.thinkstep.jde.persistence.entities.Rules.CalculateLoadAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.Rules.ParentConnector;
import com.thinkstep.jde.persistence.entities.Rules.Rule;
import com.thinkstep.jde.persistence.entities.Rules.RuleType;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionFactorInstance;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionProcess;
import com.thinkstep.jde.persistence.entities.emissioncalculation.Parameter;
import com.thinkstep.jde.persistence.entities.fuelconsumptioncalculation.VehicleType;
import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.rawdata.DataItem;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.entities.rawdata.DataType;
import com.thinkstep.jde.persistence.services.ActionService;
import com.thinkstep.jde.persistence.services.BusinessUnitActionService;
import com.thinkstep.jde.persistence.services.CalculateDistanceActionService;
import com.thinkstep.jde.persistence.services.CalculateLoadActionService;
import com.thinkstep.jde.persistence.services.ConstantActionService;
import com.thinkstep.jde.persistence.services.CountryService;
import com.thinkstep.jde.persistence.services.DateActionService;
import com.thinkstep.jde.persistence.services.DistanceService;
import com.thinkstep.jde.persistence.services.FuelTypeActionService;
import com.thinkstep.jde.persistence.services.RuleService;
import com.thinkstep.jde.persistence.services.ValueActionService;
import com.thinkstep.jde.persistence.services.VehicleTypeActionService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import static java.lang.Math.abs;
import java.text.NumberFormat;
import java.util.Comparator;

/**
 *
 * @author forell
 */
public class Calculations {
    DistanceCalculationWrapper dc = new DistanceCalculationWrapper();
    CountryService cService = CountryService.getINSTANCE();
    public void calculate(LogisticServiceProvider lsp, List<DataRow> dataRows, MappingType mappingType, Rule parent, ParentConnector parentConnector){
        ActionService actionService = ActionService.getINSTANCE();
        List<Action> actions = actionService.findAllActionsByLspAndMappingTypeAndParent(lsp, mappingType, parent, parentConnector);
        for (Action action:actions){
            switch (action.getActionType()) {
                case ASSIGNRAWDATAVALUE:
                    executeAssignRawData(dataRows,action);
                    break;
                case ASSIGNCONSTANT:
                    executeAssignConstant(dataRows,action);
                    break;
                case VEHICLETYPE:
                    executeAssignVehicleType(dataRows,action);
                    break;
                case FUELTYPE:
                    executeAssignFuelType(dataRows,action);
                    break;
                case DATE:
                    executeAssignDate(dataRows,action);
                    break;
                case CALCULATEDISTANCE:
                    executeCalculateDistance(dataRows,action);
                    break;
                case CALCULATELOAD:
                    executeCalculateLoad(dataRows,action);
                    break;
                case ASSIGNBUSINESSUNIT:
                    executeAssignBusinessUnit(dataRows,action);
                    break;
                case UNDEFINED:
            }
        }
        RuleService ruleService = RuleService.getINSTANCE();
        List<Rule> rules = ruleService.findRulesByLspAndMappingTypeAndParent(lsp, mappingType, parent, parentConnector);
        for (Rule rule:rules){
            List<DataRow> thenValues = evaluateRuleThen(rule, dataRows);
            calculate(lsp, thenValues, mappingType, rule, ParentConnector.THEN);
            List<DataRow> elseValues = evaluateRuleElse(rule, dataRows);
            calculate(lsp, elseValues, mappingType, rule, ParentConnector.ELSE);
        }
    }

    private void executeAssignRawData(List<DataRow> dataRows, Action action) {
        ValueActionService vaService = ValueActionService.getINSTANCE();
        AssignValueAction a = vaService.findValueActionById(action.getActionId());
        FieldName fieldName = a.getName();
        
        for (DataRow dataRow:dataRows){
            switch (fieldName){
                case GHGEMISSIONSMAIN:
                    dataRow.setGhgEmissionsMain(assignData(dataRow, a.getKey()));
                    break;
                case GHGEMISSIONSPICKUP:
                    dataRow.setGhgEmissionsPickup(assignData(dataRow, a.getKey()));
                    break;
                case GHGEMISSIONSDELIVERY:
                    dataRow.setGhgEmissionsDelivery(assignData(dataRow, a.getKey()));
                    break;
                case FUELCONSUMPTIONMAIN:
                    dataRow.setFuelConsumptionMain(assignData(dataRow, a.getKey()));
                    break;
                case FUELCONSUMPTIONPICKUP:
                    dataRow.setFuelConsumptionPickup(assignData(dataRow, a.getKey()));
                    break;
                case FUELCONSUMPTIONDELIVERY:
                    dataRow.setFuelConsumptionDelivery(assignData(dataRow, a.getKey()));
                    break;
                case DISTANCEMAIN:
                    dataRow.setDistanceMain(assignData(dataRow, a.getKey()));
                    break;
                case DISTANCEPICKUP:
                    dataRow.setDistancePickup(assignData(dataRow, a.getKey()));
                    break;
                case DISTANCEDELIVERY:
                    dataRow.setDistanceDelivery(assignData(dataRow, a.getKey()));
                    break;
                case SHIPMENTWEIGHT:
                    dataRow.setShipmentWeight(assignData(dataRow, a.getKey()));
                    break;
                case NUMBEROFPALLETS:
                    dataRow.setNumberOfPallets(assignData(dataRow, a.getKey()));
                    break;
                case LOADFACTORMAIN:
                    dataRow.setLoadFactorMain(assignData(dataRow, a.getKey()));
                    break;
                case LOADFACTORPICKUP:
                    dataRow.setLoadFactorPickup(assignData(dataRow, a.getKey()));
                    break;
                case LOADFACTORDELIVERY:
                    dataRow.setLoadFactorDelivery(assignData(dataRow, a.getKey()));
                    break;
                case FUELTYPEMAIN:
                    dataRow.setLoadFactorMain(assignData(dataRow, a.getKey()));
                    break;
                case FUELTYPEDELIVERY:
                    dataRow.setLoadFactorPickup(assignData(dataRow, a.getKey()));
                    break;
                case FUELTYPEPICKUP:
                    dataRow.setLoadFactorDelivery(assignData(dataRow, a.getKey()));
            }
        }
    }

    private void executeAssignConstant(List<DataRow> dataRows, Action action) {
        ConstantActionService vaService = ConstantActionService.getINSTANCE();
        AssignConstantAction a = vaService.findAConstantActionById(action.getActionId());
        FieldName fieldName = a.getName();
        
        for (DataRow dataRow:dataRows){
            switch (fieldName){
                case GHGEMISSIONSMAIN:
                    dataRow.setGhgEmissionsMain(a.getValue());
                    break;
                case GHGEMISSIONSPICKUP:
                    dataRow.setGhgEmissionsPickup(a.getValue());
                    break;
                case GHGEMISSIONSDELIVERY:
                    dataRow.setGhgEmissionsDelivery(a.getValue());
                    break;
                case FUELCONSUMPTIONMAIN:
                    dataRow.setFuelConsumptionMain(a.getValue());
                    break;
                case FUELCONSUMPTIONPICKUP:
                    dataRow.setFuelConsumptionPickup(a.getValue());
                    break;
                case FUELCONSUMPTIONDELIVERY:
                    dataRow.setFuelConsumptionDelivery(a.getValue());
                    break;
                case DISTANCEMAIN:
                    dataRow.setDistanceMain(a.getValue());
                    break;
                case DISTANCEPICKUP:
                    dataRow.setDistancePickup(a.getValue());
                    break;
                case DISTANCEDELIVERY:
                    dataRow.setDistanceDelivery(a.getValue());
                    break;
                case SHIPMENTWEIGHT:
                    dataRow.setShipmentWeight(a.getValue());
                    break;
                case LOADFACTORMAIN:
                    dataRow.setLoadFactorMain(a.getValue());
                    break;
                case LOADFACTORPICKUP:
                    dataRow.setLoadFactorPickup(a.getValue());
                    break;
                case LOADFACTORDELIVERY:
                    dataRow.setLoadFactorDelivery(a.getValue());
                    break;
                case EMPTYTRIPFACTORDELIVERY:
                    dataRow.setEmptyTripFactorDelivery(a.getValue());
                    break;
                case EMPTYTRIPFACTORMAIN:
                    dataRow.setEmptyTripFactorMain(a.getValue());
                    break;
                case EMPTYTRIPFACTORPICKUP:
                    dataRow.setEmptyTripFactorPickup(a.getValue());
            }
        }
    }

    private void executeAssignVehicleType(List<DataRow> dataRows, Action action) {
        VehicleTypeActionService vaService = VehicleTypeActionService.getINSTANCE();
        AssignVehicleTypeAction a = vaService.findVehicleTypeActionById(action.getActionId());
        
        for (DataRow dataRow:dataRows){
            switch (a.getName()){
                case VEHICLETYPEPICKUP:
                    dataRow.setVehicleTypePickup(a.getVehicleType());
                    
                    break;
                case VEHICLETYPEMAIN:
                    dataRow.setVehicleTypeMain(a.getVehicleType());
                    
                    
                    break;
                case VEHICLETYPEDELIVERY:
                    dataRow.setVehicleTypeDelivery(a.getVehicleType());
                    
                    break;
            }
        }
    }
    
    private double calculateFuelConsumed(DataRow dr, FieldName name){
        double maxCons = 0;
        double minCons = 0;
        double loadFactor = 0;
        double emptyTripFactor = 0;
        double maxPayload = 0;
        double distance = 0;
        
        
                      
        switch (name){
            case VEHICLETYPEPICKUP:
                if (dr.getVehicleTypePickup() != null){
                    maxCons = dr.getVehicleTypePickup().getFuelConsumptionLoaded();
                    minCons = dr.getVehicleTypePickup().getFuelConsumptionEmpty();
                    loadFactor = dr.getLoadFactorPickup();
                    emptyTripFactor = dr.getEmptyTripFactorPickup();
                    maxPayload = dr.getVehicleTypePickup().getMaxPayload() * 1000;
                    distance = dr.getDistancePickup();
                } else {
                    dr.setError("No vehicle defined: No vehicle defined for pickup leg; ");
                }
                break;
            case VEHICLETYPEMAIN:
                if (dr.getVehicleTypeMain() != null){
                    maxCons = dr.getVehicleTypeMain().getFuelConsumptionLoaded();
                    minCons = dr.getVehicleTypeMain().getFuelConsumptionEmpty();
                    loadFactor = dr.getLoadFactorMain();
                    emptyTripFactor = dr.getEmptyTripFactorMain();
                    maxPayload = dr.getVehicleTypeMain().getMaxPayload() * 1000;
                    distance = dr.getDistanceMain();
                } else {
                    dr.setError("No vehicle defined: No vehicle defined for main leg; ");
                }
                break;
            case VEHICLETYPEDELIVERY:
                if (dr.getVehicleTypeDelivery() != null){
                    maxCons = dr.getVehicleTypeDelivery().getFuelConsumptionLoaded();
                    minCons = dr.getVehicleTypeDelivery().getFuelConsumptionEmpty();
                    loadFactor = dr.getLoadFactorDelivery();
                    emptyTripFactor = dr.getEmptyTripFactorDelivery();
                    maxPayload = dr.getVehicleTypeDelivery().getMaxPayload() * 1000;
                    distance = dr.getDistanceDelivery();
                } else {
                    dr.setError("No vehicle defined: No vehicle defined for delivery leg; ");
                }  
                break;

        }
        double weight = abs(dr.getShipmentWeight());
        double fuelCons = (maxCons-minCons) * loadFactor + minCons;
        double utilizationFactor = loadFactor / (1+emptyTripFactor);
        double specFuelCons = fuelCons / (utilizationFactor * maxPayload) /100;
        double finalcons = specFuelCons * distance * dr.getShipmentWeight();
        return finalcons;
            
        
    }

    private void executeAssignFuelType(List<DataRow> dataRows, Action action) {
        FuelTypeActionService vaService = FuelTypeActionService.getINSTANCE();
        AssignFuelTypeAction a = vaService.findFuelTypeActionById(action.getActionId());
        a.unserialize();
        switch (a.getName()){
            case FUELTYPEDELIVERY:
                for (DataRow dataRow:dataRows){
                    dataRow.setFuelConsumptionDelivery(calculateFuelConsumed(dataRow, FieldName.VEHICLETYPEDELIVERY));
                    EmissionProcess ep = a.getEmissionProcess();
                    List<Parameter> params = a.getParameters();
                    double ghgperl = getGhgPerl(ep, params);
                    double ghg = dataRow.getFuelConsumptionDelivery()*ghgperl;
                    dataRow.setGhgEmissionsDelivery(ghg);
                }
                break;
            case FUELTYPEMAIN:
                for (DataRow dataRow:dataRows){
                    dataRow.setFuelConsumptionMain(calculateFuelConsumed(dataRow, FieldName.VEHICLETYPEMAIN));
                    EmissionProcess ep = a.getEmissionProcess();
                    List<Parameter> params = a.getParameters();
                    double ghgperl = getGhgPerl(ep, params);
                    double ghg = dataRow.getFuelConsumptionMain()*ghgperl;
                    dataRow.setGhgEmissionsMain(ghg);
                }
                break;
            case FUELTYPEPICKUP:
                for (DataRow dataRow:dataRows){
                    dataRow.setFuelConsumptionPickup(calculateFuelConsumed(dataRow, FieldName.VEHICLETYPEPICKUP));
                    EmissionProcess ep = a.getEmissionProcess();
                    List<Parameter> params = a.getParameters();
                    double ghgperl = getGhgPerl(ep, params);
                    double ghg = dataRow.getFuelConsumptionPickup()*ghgperl;
                    dataRow.setGhgEmissionsPickup(ghg);
                }
                break;
        }
        for (DataRow dataRow:dataRows){
            EmissionProcess ep = a.getEmissionProcess();
            List<Parameter> params = a.getParameters();
            double ghgperl = getGhgPerl(ep, params);
            double ghg = dataRow.getFuelConsumptionMain()*ghgperl;
            dataRow.setGhgEmissionsMain(ghg);
        }
    }

    private double assignData(DataRow dataRow, DataKey key) {
        double value = 0;
        List<DataItem> dataItems = dataRow.getDataItems();
        for (DataItem dataItem:dataItems){
            if (dataItem.getDataKey().getId() == key.getId()){
                value = dataItem.getValueNumeric();
            }
        }
        
        return value;
    }

    private double getGhgPerl(EmissionProcess ep, List<Parameter> params) {
        ep.unserialize();
        double result = 0;
        
        List<EmissionFactorInstance> efis = ep.getEmissionFactorInstances();
        for (EmissionFactorInstance efi:efis){
            double value = efi.getEmissionFactor().getGHGEmissions();
            double scaling = efi.getScalingFactor();
            String parameter = efi.getParameter().getName();
            double percentage = 0;
            for (Parameter param:params){
                if (param.getName().equals(parameter)){
                    percentage = param.getValue();
                }
            }
            result = result + value * scaling * percentage;
        }
        
        return result;
    }

    private List<DataRow> evaluateRuleThen(Rule rule, List<DataRow> dataRows) {
        Map<String, Double> tour = null;
        if ( rule.getRuleType().equals(RuleType.NUMBEROFPALLETSPERTOURLARGER) ||
                rule.getRuleType().equals(RuleType.NUMBEROFPALLETSPERTOURLARGEREQUAL) ||
                rule.getRuleType().equals(RuleType.NUMBEROFPALLETSPERTOURSMALLER) ||
                rule.getRuleType().equals(RuleType.NUMBEROFPALLETSPERTOURSMALLEREQUAL) ||
                rule.getRuleType().equals(RuleType.PALLETUTILIZATIONLARGER) ||
                rule.getRuleType().equals(RuleType.PALLETUTILIZATIONLARGEREQUAL) ||
                rule.getRuleType().equals(RuleType.PALLETUTILIZATIONSMALLER) ||
                rule.getRuleType().equals(RuleType.PALLETUTILIZATIONSMALLEREQUAL)){
            tour = dataRows.stream().collect(new PalletsPerTourCollector(rule.getKey()));
        }
                
        List<DataRow> dataRowsThen = new ArrayList<>();
        for (DataRow dataRow:dataRows) {
            if(evaluateRule(rule, dataRow, tour)){
                dataRowsThen.add(dataRow);
            }
        }
        return dataRowsThen;
    }

    private List<DataRow> evaluateRuleElse(Rule rule, List<DataRow> dataRows) {
        Map<String, Double> tour = null;
        if ( rule.getRuleType().equals(RuleType.NUMBEROFPALLETSPERTOURLARGER) ||
                rule.getRuleType().equals(RuleType.NUMBEROFPALLETSPERTOURLARGEREQUAL) ||
                rule.getRuleType().equals(RuleType.NUMBEROFPALLETSPERTOURSMALLER) ||
                rule.getRuleType().equals(RuleType.NUMBEROFPALLETSPERTOURSMALLEREQUAL) ||
                rule.getRuleType().equals(RuleType.PALLETUTILIZATIONLARGER) ||
                rule.getRuleType().equals(RuleType.PALLETUTILIZATIONLARGEREQUAL) ||
                rule.getRuleType().equals(RuleType.PALLETUTILIZATIONSMALLER) ||
                rule.getRuleType().equals(RuleType.PALLETUTILIZATIONSMALLEREQUAL)){
            tour = dataRows.stream().collect(new PalletsPerTourCollector(rule.getKey()));
        }
        
        List<DataRow> dataRowsElse = new ArrayList<>();
        dataRowsElse.addAll(dataRows);
        for (DataRow dataRow:dataRows) {
            if(evaluateRule(rule, dataRow, tour)){
                dataRowsElse.remove(dataRow);
            }
        }
        return dataRowsElse;
    }

    private boolean evaluateRule(Rule rule, DataRow dataRow, Map<String, Double> tour) {
        DataKey key = rule.getKey();
        List<DataItem> dataItems = dataRow.getDataItems();
        DataItem dataItem = getDataItem(key, dataRow);
        boolean result = false;
        switch (rule.getRuleType()){
            case LARGER:
                if (dataItem.getDataType().equals(DataType.NUMERIC)){
                    if (dataItem.getValueNumeric() > rule.getValueDouble()){
                        result = true;
                    }
                }
                break;
            case LARGEREQUAL:
                if (dataItem.getDataType().equals(DataType.NUMERIC)){
                    if (dataItem.getValueNumeric() >= rule.getValueDouble()){
                        result = true;
                    }
                }
                break;
            case SMALLEREQUAL:
                if (dataItem.getDataType().equals(DataType.NUMERIC)){
                    if (dataItem.getValueNumeric() <= rule.getValueDouble()){
                        result = true;
                    }
                }
                break;
            case SMALLER:
                if (dataItem.getDataType().equals(DataType.NUMERIC)){
                    if (dataItem.getValueNumeric() < rule.getValueDouble()){
                        result = true;
                    }
                }
                break;
            case IDENTITY:
                if (dataItem.getDataType().equals(DataType.NUMERIC)){
                    if (dataItem.getValueNumeric() == rule.getValueDouble()){
                        result = true;
                    }
                }
                break;
            case NUMBEROFPALLETSPERTOURLARGER:
                
                
                
                
                if (tour.get(getValueAsString(dataRow,key)) > rule.getValueDouble()){
                    result = true;
                }

                break;
            case NUMBEROFPALLETSPERTOURSMALLER:

                if (tour.get(getValueAsString(dataRow,key)) < rule.getValueDouble()){
                    result = true;
                }
                break;
            case NUMBEROFPALLETSPERTOURLARGEREQUAL:

                if (tour.get(getValueAsString(dataRow,key)) >= rule.getValueDouble()){
                    result = true;

                }

                break;
            case NUMBEROFPALLETSPERTOURSMALLEREQUAL:
                
                System.out.println("TourId : " + getValueAsString(dataRow,key) + "Number of Pallets: " + tour.get(getValueAsString(dataRow,key)) + " Pallets for Shipment: " + dataRow.getNumberOfPallets());
                
                if (tour.get(getValueAsString(dataRow,key)) <= rule.getValueDouble()){
                    result = true;
                    System.out.println("kleiner gleich " + rule.getValueDouble());
                }

                break;
            case PALLETUTILIZATIONLARGER:
                VehicleType veh = dataRow.getVehicleTypeMain();
                
 
                double palletUtilization = tour.get(getValueAsString(dataRow,key))/veh.getMaxNumFloorPallets();
                
                if (palletUtilization > rule.getValueDouble()){
                    result = true;
                }
                
                break;
            case PALLETUTILIZATIONLARGEREQUAL:
                
                VehicleType veh1 = dataRow.getVehicleTypeMain();
                double palletUtilization1 = tour.get(getValueAsString(dataRow,key))/veh1.getMaxNumFloorPallets();
                
                if (palletUtilization1 >= rule.getValueDouble()){
                    result = true;
                }
                
                System.out.println("Pallet utilization: "+ palletUtilization1 + " Number of Pallets: " + tour.get(getValueAsString(dataRow,key)) + " Load Number " + getValueAsString(dataRow,key));
                break;
            case PALLETUTILIZATIONSMALLER:
                
                VehicleType veh7 = dataRow.getVehicleTypeMain();
                double palletUtilization2 = tour.get(getValueAsString(dataRow,key))/veh7.getMaxNumFloorPallets();
                
                if (palletUtilization2 < rule.getValueDouble()){
                    result = true;
                }
                
                System.out.println("Pallet utilization: "+ palletUtilization2 + " Number of Pallets: " + tour.get(getValueAsString(dataRow,key)) + " Load Number " + getValueAsString(dataRow,key));
                break;
            case PALLETUTILIZATIONSMALLEREQUAL:
                
                VehicleType veh8 = dataRow.getVehicleTypeMain();
                double palletUtilization3 = tour.get(getValueAsString(dataRow,key))/veh8.getMaxNumFloorPallets();
                
                if (palletUtilization3 <= rule.getValueDouble()){
                    result = true;
                }
               
                System.out.println("Pallet utilization: "+ palletUtilization3 + " Number of Pallets: " + tour.get(getValueAsString(dataRow,key)) + " Load Number " + getValueAsString(dataRow,key));
                break;
            case BEGINSWITH:
                if (dataItem.getDataType().equals(DataType.TEXT)){
                    if (dataItem.getValueString().startsWith(rule.getValueString())){
                        result = true;
                    }
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    if (nf.format(dataItem.getValueNumeric()).startsWith(rule.getValueString())){
                        result = true;
                    }
                }
                break;
            case ENDSWITH:
                if (dataItem.getDataType().equals(DataType.TEXT)){
                    if (dataItem.getValueString().endsWith(rule.getValueString())){
                        result = true;
                    }
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    if (nf.format(dataItem.getValueNumeric()).endsWith(rule.getValueString())){
                        result = true;
                    }
                }
                break;
            case CONTAINS:
                if (dataItem.getDataType().equals(DataType.TEXT)){
                    if (dataItem.getValueString().contains(rule.getValueString())){
                        result = true;
                    }
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    if (nf.format(dataItem.getValueNumeric()).contains(rule.getValueString())){
                        result = true;
                    }
                }
                break;
            case EQUALS:
                if (dataItem.getDataType().equals(DataType.TEXT)){
                    if (dataItem.getValueString().equals(rule.getValueString())){
                        result = true;
                    }
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    if (nf.format(dataItem.getValueNumeric()).equals(rule.getValueString())){
                        result = true;
                    }
                }
                break;
        }
        
        return result;
        
    }

    private DataItem getDataItem(DataKey key, DataRow dataRow) {
        List<DataItem> dataItems = dataRow.getDataItems();
        DataItem dataItem = null;
        for (DataItem di:dataItems){
            if (di.getDataKey().getKey().equals(key.getKey())){
                dataItem = di;
            }
        }
        return dataItem;
    }

    private void executeAssignDate(List<DataRow> dataRows, Action action) {
        DateActionService daService = DateActionService.getINSTANCE();
        AssignDateAction a = daService.findAssignDateActionById(action.getActionId());
        
        for (DataRow dataRow:dataRows){
            dataRow.setDate(assignDate(dataRow, a.getDataKey()));
        }
    }
    
    private Calendar assignDate(DataRow dataRow, DataKey key){
        Calendar value = null;
        List<DataItem> dataItems = dataRow.getDataItems();
        for (DataItem dataItem:dataItems){
            if (dataItem.getDataKey().getId() == key.getId()){
                try {
                    value = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
                    value.setTime(sdf.parse(dataItem.getValueString()));// all done
                } catch (ParseException ex) {
                    value = null;
                }
            }
        }
        
        return value;
    }

    private void executeCalculateDistance(List<DataRow> dataRows, Action action){
        this.dc.clearTours();
        CalculateDistanceActionService cdService = CalculateDistanceActionService.getINSTANCE();
        CalculateDistanceAction a = cdService.findCalculateDistanceActionById(action.getActionId());
        FieldName fieldName = a.getName();
        int i = 0;
         
        DataKey tourId = a.getTourIdentifier();
        DataKey countryStart = a.getStartCountry();
        DataKey cityStart = a.getStartCity();
        DataKey zipStart = a.getStartZipCode();
        DataKey countryEnd = a.getDestinationCountry();
        DataKey cityEnd = a.getDestinationCity();
        DataKey zipEnd = a.getDestinationZipCode(); 
        Map<String, Set<Location>> tours = dataRows.stream().collect(new TourCollector(tourId, countryStart, zipStart, cityStart, countryEnd, zipEnd, cityEnd));
        
        DistanceService diService = DistanceService.getINSTANCE();
        diService.updateLocations(tours);
        
         
         
        for (DataRow dr:dataRows){
            switch (fieldName){
                case DISTANCEPICKUP:
                   
                    dr.setDistancePickup(calculateDistance(dr, tours, a));
                    
                    break;
                case DISTANCEDELIVERY:
                    
                    dr.setDistanceDelivery(calculateDistance(dr, tours, a));
                    
                    break;
                case DISTANCEMAIN:
                    
                    dr.setDistanceMain(calculateDistance(dr, tours, a));
                    
                    break;
                    
            }
            
        }
    }

    private double calculateDistance(DataRow dr, Map<String, Set<Location>> tours, CalculateDistanceAction a) {
        
        DataKey tour = a.getTourIdentifier();
        DataKey countryStart = a.getStartCountry();
        DataKey cityStart = a.getStartCity();
        DataKey zipStart = a.getStartZipCode();
        DataKey countryEnd = a.getDestinationCountry();
        DataKey cityEnd = a.getDestinationCity();
        DataKey zipEnd = a.getDestinationZipCode();
        
        String tourId;
        if (dr.getDataItem(tour).getDataType().equals(DataType.TEXT)){
            tourId = dr.getDataItem(tour).getValueString();
        } else {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            tourId = ""+nf.format(dr.getDataItem(tour).getValueNumeric());
        }
        
        Set<Location> tourLocations = tours.get(tourId);
        System.out.println("tourId: " + tourId);
        System.out.println("tourLocations: " + tourLocations);
        dc.calculateDistances(tourLocations, tourId);
        
        Location loc1 = new Location();
        loc1.setCountry(cService.findCountryByCode(dr.getDataItem(countryStart).getValueString()));
        loc1.setCity1(dr.getDataItem(cityStart).getValueString());
        if (dr.getDataItem(zipStart).getDataType().equals(DataType.TEXT)){
            loc1.setZipCode(dr.getDataItem(zipStart).getValueString());
        } else {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            loc1.setZipCode(""+nf.format(dr.getDataItem(zipStart).getValueNumeric()));
        }
        
        Location loc2 = new Location();
        loc2.setCountry(cService.findCountryByCode(dr.getDataItem(countryEnd).getValueString()));
        loc2.setCity1(dr.getDataItem(cityEnd).getValueString());
        if (dr.getDataItem(zipEnd).getDataType().equals(DataType.TEXT)){
            loc2.setZipCode(dr.getDataItem(zipEnd).getValueString());
        } else {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            loc2.setZipCode(""+nf.format(dr.getDataItem(zipEnd).getValueNumeric()));
        }
        String errString = "";
        if (dr.getError() != null){
         
            errString = dr.getError();
        }
        
        if (!errString.contains("Adress Extraction Error:")){
            try {
                return dc.getDistance(loc1, loc2, tourId);
            } catch (MissingLocationsException ex) {
                String errorMessage = "Tour Calculation Error: One of the following locations was not found: ";
                for (Location l:ex.getLs()){
                    errorMessage = errorMessage + l.getCountry().getCountryCode() + " - " + l.getZipCode() + " " + l.getCity1();
                }
                errorMessage = errorMessage + "; ";
                dr.setError(dr.getError()+ " " + errorMessage);
                
                return 0;
            }
            
        } else {
            return 0.0;
        }
        
        
        
        

    }

    private void executeAssignBusinessUnit(List<DataRow> dataRows, Action action) {
        
        BusinessUnitActionService buService = BusinessUnitActionService.getINSTANCE();
        AssignBusinessUnitAction a = buService.findAssignBusinessUnitActionById(action.getActionId());
        FieldName fieldName = a.getName();
        
        for (DataRow dataRow:dataRows){
            switch (fieldName){
                case BUSINESSUNIT:
                    dataRow.setBusinessUnit(a.getBusinessUnit());
                    break;
            }
        }
    }

    private void executeCalculateLoad(List<DataRow> dataRows, Action action) {
        CalculateLoadActionService claService = CalculateLoadActionService.getINSTANCE();
        CalculateLoadAction a = claService.findCalculateLoadActionById(action.getActionId());
        FieldName fieldName = a.getName();
        DataKey tourId = a.getTourIdentifier();
        
        Map<String, List<TourData>> tourDatas = dataRows.stream().collect(new TourDataCollector(tourId, fieldName));
        
        for (DataRow dataRow:dataRows){
            switch (fieldName){
                case LOADFACTORPICKUP:
                    dataRow.setLoadFactorPickup(calculateAverageLoad(dataRow, tourDatas, tourId, dataRow.getVehicleTypePickup(), dataRow.getDistancePickup()));
                    break;
                case LOADFACTORMAIN:
                    dataRow.setLoadFactorMain(calculateAverageLoad(dataRow, tourDatas, tourId, dataRow.getVehicleTypeMain(), dataRow.getDistanceMain()));
                    break;
                case LOADFACTORDELIVERY:
                    dataRow.setLoadFactorDelivery(calculateAverageLoad(dataRow, tourDatas, tourId, dataRow.getVehicleTypeDelivery(), dataRow.getDistanceDelivery()));
                    break;
            }
        }
    }

    private double calculateAverageLoad(DataRow dataRow, Map<String, List<TourData>> tourDatas, DataKey tourId, VehicleType vehicleType, double distance) {
        List<LoadFactorDistanceWrapper> partials = new ArrayList<>();
        String tid;
        if (dataRow.getDataItem(tourId).getDataType().equals(DataType.TEXT)){
            tid = dataRow.getDataItem(tourId).getValueString();
        } else {
            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);
            tid = ""+nf.format(dataRow.getDataItem(tourId).getValueNumeric());
        }
        
        int numVehicle = 0;
        
        List<TourData> tourDataList = tourDatas.get(tid).stream().collect(new TourAggregator());
        
        tourDataList.sort(new Comparator<TourData>(){
            @Override
            public int compare(TourData o1, TourData o2) {
                if (o1.getDistance() == o2.getDistance()){
                    return 0;
                }
                if (o1.getDistance()<o2.getDistance()){
                    return -1;
                } else {
                    return 1;
                }
            }
        
        });
        
        for (TourData tourdata:tourDataList){
            LoadFactorDistanceWrapper lfdw = new LoadFactorDistanceWrapper();
            lfdw.setDistance(tourdata.getDistance());
            if (numVehicle == 0){
                numVehicle = 1;
                while (tourdata.getNumpallets()/(numVehicle*vehicleType.getMaxNumFloorPallets())>1 || tourdata.getMass()/(numVehicle*vehicleType.getMaxPayload()*1000)>1){
                    numVehicle++;
                }
            }
            double loadfactor = tourdata.getMass()/(numVehicle * vehicleType.getMaxPayload()*1000);
            lfdw.setLoadFactor(loadfactor);
            partials.add(lfdw);
            if (tourdata.getDistance() == distance){
                break;
            }
        }
        
        
        
        double loadFactorAverage = 0;
        for (int i = 0; i<partials.size();i++){
            if ( i == 0){
                loadFactorAverage = partials.get(i).getDistance() * partials.get(i).getLoadFactor();
            } else {
                loadFactorAverage = loadFactorAverage + (partials.get(i).getDistance() - partials.get(i-1).getDistance()) * partials.get(i).getLoadFactor();
            }
           
        }
        
        loadFactorAverage = loadFactorAverage / partials.get(partials.size()-1).getDistance();
        return loadFactorAverage;
        
        
    }
    
    private String getValueAsString(DataRow dataRow, DataKey key){
        
                String tourId;
                if (dataRow.getDataItem(key).getDataType().equals(DataType.TEXT)){
                    tourId = dataRow.getDataItem(key).getValueString();
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    tourId = ""+nf.format(dataRow.getDataItem(key).getValueNumeric());
                }
                return tourId;
    }
    
    
}
