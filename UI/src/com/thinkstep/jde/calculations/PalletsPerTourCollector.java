/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.entities.rawdata.DataType;
import com.thinkstep.jde.persistence.services.CountryService;
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
public class PalletsPerTourCollector implements Collector<DataRow, Map<String,Double>, Map<String,Double>>{
    CountryService cService = CountryService.getINSTANCE();
    DataKey tourIdKey;

    public PalletsPerTourCollector(DataKey tourId) {
        this.tourIdKey = tourId;
    }
    
    
    
    
    
    @Override
    public Supplier<Map<String,Double>> supplier() {
        return new Supplier<Map<String,Double>>(){
            @Override
            public Map<String,Double> get() {
                Map<String,Double> map = new HashMap<>();
                return map;
            }
            
        };
    }

    @Override
    public BiConsumer<Map<String,Double>, DataRow> accumulator() {
        return new BiConsumer<Map<String,Double>, DataRow>(){
            @Override
            public void accept(Map<String,Double> t, DataRow u) {
                String tourId;
                if (u.getDataItem(tourIdKey).getDataType().equals(DataType.TEXT)){
                    tourId = u.getDataItem(tourIdKey).getValueString();
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    tourId = ""+nf.format(u.getDataItem(tourIdKey).getValueNumeric());
                    
                }
                
                Double numpallets = t.get(tourId);
                
                if (numpallets == null){
                    numpallets = u.getNumberOfPallets();
                } else {
                    numpallets = numpallets + Math.abs(u.getNumberOfPallets());
                }
                t.put(tourId, numpallets);
            }
            
        };
    }

    @Override
    public BinaryOperator<Map<String,Double>> combiner() {
        return new BinaryOperator<Map<String,Double>>(){
            @Override
            public Map<String,Double> apply(Map<String,Double> t, Map<String,Double> u) {
                Set<String> keys = new HashSet<>();
                keys.addAll(t.keySet());
                keys.addAll(u.keySet());
                
                for (String key:keys){
                    Double numPallets = t.get(key);
                    if (numPallets == null){
                        numPallets = 0.0;
                    }
                    
                    Double numPallets2 = u.get(key);
                    if (numPallets2 != null) {
                        numPallets = numPallets + numPallets2;
                    }
                    
                    t.put(key, numPallets);
                }
                return t;
            }
        
        };
    }

    @Override
    public Function< Map<String,Double>,  Map<String,Double>> finisher() {
        return new Function<Map<String,Double>, Map<String,Double>>(){
            @Override
            public Map<String,Double> apply(Map<String,Double> t) {
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
