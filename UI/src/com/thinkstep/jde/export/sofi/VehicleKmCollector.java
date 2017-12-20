/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.export.sofi;

import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.entities.rawdata.DataType;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

/**
 *
 * @author forell
 */
public class VehicleKmCollector implements Collector< DataRow, Map<String, Double> , Map<String, Double>>{
    
    DataKey tourIdKey;
    

    public VehicleKmCollector(DataKey tourId) {
        this.tourIdKey = tourId;
        
    }

    @Override
    public Supplier<Map<String, Double>> supplier() {
        return new Supplier<Map<String, Double>>(){
            @Override
            public Map<String, Double> get() {
                Map<String, Double> map = new HashMap<>();
                return map;
            }
            
        };
    }

    @Override
    public BiConsumer<Map<String, Double>, DataRow> accumulator() {
        return new BiConsumer<Map<String, Double>, DataRow>(){
            @Override
            public void accept(Map<String, Double> t, DataRow u) {
                String tourId;
                if (u.getDataItem(tourIdKey).getDataType().equals(DataType.TEXT)){
                    tourId = u.getDataItem(tourIdKey).getValueString();
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    tourId = ""+nf.format(u.getDataItem(tourIdKey).getValueNumeric());
                }
                
                
                
                
                Double vehkm = t.get(tourId);
                if (vehkm == null){
                    vehkm = 0.0;
                }
                vehkm = Math.max(vehkm, u.getDistanceDelivery() + u.getDistanceMain()+ u.getDistancePickup());
                t.put(tourId, vehkm);
            }
            
        };
    }

    @Override
    public BinaryOperator<Map<String, Double>> combiner() {
        return new BinaryOperator<Map<String, Double>>(){
            @Override
            public Map<String, Double> apply(Map<String, Double> t, Map<String, Double> u) {
                Set<String> keys = new HashSet<>();
                keys.addAll(t.keySet());
                keys.addAll(u.keySet());
                
                for (String key:keys){
                    Double vehkms = t.get(key);
                    if (vehkms == null){
                        vehkms = 0.0;
                    }
                    
                    Double vehkms2 = u.get(key);
                    if (vehkms2 != null) {
                        
                        
                        if (Double.isNaN(vehkms2) || Double.isInfinite(vehkms2)){
                            vehkms = vehkms;
                        } else if (Double.isNaN(vehkms) || Double.isInfinite(vehkms)){
                            vehkms = vehkms2; 
                        } else {
                            vehkms = Math.max(vehkms, vehkms2);
                        }
                        
                    }
                    
                    t.put(key, vehkms);
                }
                return t;
            }
            };
            
        };

    @Override
    public Function<Map<String, Double>, Map<String, Double>> finisher() {
        return new Function<Map<String, Double>, Map<String, Double>>(){
            @Override
            public Map<String, Double> apply(Map<String, Double> t) {
                
                return t;
            }
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        Set<Characteristics> characteristics = new HashSet<>();
        characteristics.add(Characteristics.UNORDERED);
        characteristics.add(Characteristics.IDENTITY_FINISH);
        
        return characteristics;
    }
}
