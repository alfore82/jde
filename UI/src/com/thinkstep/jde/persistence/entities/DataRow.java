/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thinkstep.jde.persistence.entities.fuelconsumptioncalculation.VehicleType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.rawdata.DataItem;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author forell
 */
@Entity
public class DataRow {
    @Id @GeneratedValue
    private long id;
    @OneToOne
    private LogisticServiceProvider lsp;
    @Temporal(TemporalType.DATE)
    private Calendar date;
    private double shipmentWeight;
    private double numberOfPallets;
    private String error = "";
    @Transient
    private List<DataItem> dataItems;
    
    @Lob
    private String dataItemsSerial;
    
    
    private double ghgEmissionsPickup;
    private double loadFactorPickup;
    private double fuelConsumptionPickup;
    private double distancePickup;
    private VehicleType vehicleTypePickup;
    private double emptyTripFactorPickup;
    
    private double ghgEmissionsMain;
    private double loadFactorMain;
    private double fuelConsumptionMain;
    private double distanceMain;
    private VehicleType vehicleTypeMain;
    private double emptyTripFactorMain;
    
    private double ghgEmissionsDelivery;
    private double loadFactorDelivery;
    private double fuelConsumptionDelivery;
    private double distanceDelivery;
    private VehicleType vehicleTypeDelivery;
    private double emptyTripFactorDelivery;
    
    @Enumerated(EnumType.STRING)
    private BusinessUnit businessUnit;

    public double getGhgEmissionsPickup() {
        return ghgEmissionsPickup;
    }

    public void setGhgEmissionsPickup(double ghgEmissionsPickup) {
        this.ghgEmissionsPickup = ghgEmissionsPickup;
    }

    public double getLoadFactorPickup() {
        return loadFactorPickup;
    }

    public void setLoadFactorPickup(double loadFactorPickup) {
        this.loadFactorPickup = loadFactorPickup;
    }

    public double getFuelConsumptionPickup() {
        return fuelConsumptionPickup;
    }

    public void setFuelConsumptionPickup(double fuelConsumptionPickup) {
        this.fuelConsumptionPickup = fuelConsumptionPickup;
    }

    public double getDistancePickup() {
        return distancePickup;
    }

    public void setDistancePickup(double distancePickup) {
        this.distancePickup = distancePickup;
    }

    public VehicleType getVehicleTypePickup() {
        return vehicleTypePickup;
    }

    public void setVehicleTypePickup(VehicleType vehicleTypePickup) {
        this.vehicleTypePickup = vehicleTypePickup;
    }

    public double getEmptyTripFactorPickup() {
        return emptyTripFactorPickup;
    }

    public void setEmptyTripFactorPickup(double emptyTripFactorPickup) {
        this.emptyTripFactorPickup = emptyTripFactorPickup;
    }

    public double getGhgEmissionsDelivery() {
        return ghgEmissionsDelivery;
    }

    public void setGhgEmissionsDelivery(double ghgEmissionsDelivery) {
        this.ghgEmissionsDelivery = ghgEmissionsDelivery;
    }

    public double getLoadFactorDelivery() {
        return loadFactorDelivery;
    }

    public void setLoadFactorDelivery(double loadFactorDelivery) {
        this.loadFactorDelivery = loadFactorDelivery;
    }

    public double getFuelConsumptionDelivery() {
        return fuelConsumptionDelivery;
    }

    public void setFuelConsumptionDelivery(double fuelConsumptionDelivery) {
        this.fuelConsumptionDelivery = fuelConsumptionDelivery;
    }

    public double getDistanceDelivery() {
        return distanceDelivery;
    }

    public void setDistanceDelivery(double distanceDelivery) {
        this.distanceDelivery = distanceDelivery;
    }

    public VehicleType getVehicleTypeDelivery() {
        return vehicleTypeDelivery;
    }

    public void setVehicleTypeDelivery(VehicleType vehicleTypeDelivery) {
        this.vehicleTypeDelivery = vehicleTypeDelivery;
    }

    public double getEmptyTripFactorDelivery() {
        return emptyTripFactorDelivery;
    }

    public void setEmptyTripFactorDelivery(double emptyTripFactorDelivery) {
        this.emptyTripFactorDelivery = emptyTripFactorDelivery;
    }

    public String getDataItemsSerial() {
        return dataItemsSerial;
    }

    public void setDataItemsSerial(String dataItemsSerial) {
        this.dataItemsSerial = dataItemsSerial;
    }
    
    public void serialize(){
        Gson gson = new Gson();
        this.dataItemsSerial = gson.toJson(this.dataItems);
    }
    
    public void deSerialize(){
        Gson gson = new Gson();
        Type collectionType = new TypeToken<ArrayList<DataItem>>(){}.getType();
        this.dataItems = gson.fromJson(this.dataItemsSerial, collectionType);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LogisticServiceProvider getLsp() {
        return lsp;
    }

    public void setLsp(LogisticServiceProvider lsp) {
        this.lsp = lsp;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public double getGhgEmissionsMain() {
        return ghgEmissionsMain;
    }

    public void setGhgEmissionsMain(double ghgEmissionsMain) {
        this.ghgEmissionsMain = ghgEmissionsMain;
    }

    public double getShipmentWeight() {
        return shipmentWeight;
    }

    public void setShipmentWeight(double shipmentWeight) {
        this.shipmentWeight = shipmentWeight;
    }

    public double getLoadFactorMain() {
        return loadFactorMain;
    }

    public void setLoadFactorMain(double loadFactorMain) {
        this.loadFactorMain = loadFactorMain;
    }

    public double getFuelConsumptionMain() {
        return fuelConsumptionMain;
    }

    public void setFuelConsumptionMain(double fuelConsumptionMain) {
        this.fuelConsumptionMain = fuelConsumptionMain;
    }

    public double getDistanceMain() {
        return distanceMain;
    }

    public void setDistanceMain(double distanceMain) {
        this.distanceMain = distanceMain;
    }

    public VehicleType getVehicleTypeMain() {
        return vehicleTypeMain;
    }

    public void setVehicleTypeMain(VehicleType vehicleTypeMain) {
        this.vehicleTypeMain = vehicleTypeMain;
    }

    public double getNumberOfPallets() {
        return numberOfPallets;
    }

    public void setNumberOfPallets(double numberOfPallets) {
        this.numberOfPallets = numberOfPallets;
    }

    public double getEmptyTripFactorMain() {
        return emptyTripFactorMain;
    }

    public void setEmptyTripFactorMain(double emptyTripFactorMain) {
        this.emptyTripFactorMain = emptyTripFactorMain;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<DataItem> getDataItems() {
        return dataItems;
    }

    public void setDataItems(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }
    
    
    
    public DataItem getDataItem(DataKey key){
        for (DataItem di:dataItems){
            if (di.getDataKey().getId() == key.getId()){
                return di;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "DataRow{" + "id=" + id + ", lsp=" + lsp + ", date=" + date + ", error=" + error + ", distancePickup=" + distancePickup + ", distanceMain=" + distanceMain + ", distanceDelivery=" + distanceDelivery + '}';
    }

    
    

    
    
    
}
