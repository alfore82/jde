/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.calculations;

import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.entities.rawdata.DataType;
import com.thinkstep.jde.persistence.services.CountryService;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
public class TourDataCollector implements Collector<DataRow, Map<String,List<TourData>>, Map<String,List<TourData>>>{
    CountryService cService = CountryService.getINSTANCE();
    DataKey tourIdKey;
    FieldName field;

    public TourDataCollector(DataKey tourId, FieldName field) {
        this.tourIdKey = tourId;
        this.field = field;
    }
    
    
    
    
    
    @Override
    public Supplier<Map<String,List<TourData>>> supplier() {
        return new Supplier<Map<String,List<TourData>>>(){
            @Override
            public Map<String,List<TourData>> get() {
                Map<String,List<TourData>> map = new HashMap<>();
                return map;
            }
            
        };
    }

    @Override
    public BiConsumer<Map<String,List<TourData>>, DataRow> accumulator() {
        return new BiConsumer<Map<String,List<TourData>>, DataRow>(){
            @Override
            public void accept(Map<String,List<TourData>> t, DataRow u) {
                String tourId;
                if (u.getDataItem(tourIdKey).getDataType().equals(DataType.TEXT)){
                    tourId = u.getDataItem(tourIdKey).getValueString();
                } else {
                    NumberFormat nf = NumberFormat.getInstance();
                    nf.setGroupingUsed(false);
                    tourId = ""+nf.format(u.getDataItem(tourIdKey).getValueNumeric());
                    
                }
                
                List<TourData> tourDatas = t.get(tourId);
                
                if (tourDatas == null) {
                    tourDatas = new ArrayList<>();
                }
                TourData td = new TourData();
                
                switch (field){
                    case LOADFACTORPICKUP:
                        td.setDistance(u.getDistancePickup());
                        td.setMass(u.getShipmentWeight());
                        td.setNumpallets(u.getNumberOfPallets());
                        td.setVehicleType(u.getVehicleTypePickup());
                        break;
                    case LOADFACTORMAIN:
                        td.setDistance(u.getDistanceMain());
                        td.setMass(u.getShipmentWeight());
                        td.setNumpallets(u.getNumberOfPallets());
                        td.setVehicleType(u.getVehicleTypeMain());
                        break;
                    case LOADFACTORDELIVERY:
                        td.setDistance(u.getDistanceMain());
                        td.setMass(u.getShipmentWeight());
                        td.setNumpallets(u.getNumberOfPallets());
                        td.setVehicleType(u.getVehicleTypeMain());
                        break;
                        
                }
                tourDatas.add(td);
                
                
                t.put(tourId, tourDatas);
            }
            
        };
    }

    @Override
    public BinaryOperator<Map<String,List<TourData>>> combiner() {
        return new BinaryOperator<Map<String,List<TourData>>>(){
            @Override
            public Map<String,List<TourData>> apply(Map<String,List<TourData>> t, Map<String,List<TourData>> u) {
                Set<String> keys = new HashSet<>();
                keys.addAll(t.keySet());
                keys.addAll(u.keySet());
                
                for (String key:keys){
                    List<TourData> tourData1 = t.get(key);
                    if (tourData1 == null){
                        tourData1 = new ArrayList<>();
                    }
                    
                    List<TourData> tourData2 = u.get(key);
                    if (tourData2 != null) {
                        tourData1.addAll(tourData2);
                    }
                    
                    t.put(key, tourData1);
                }
                return t;
            }
        
        };
    }

    @Override
    public Function<Map<String,List<TourData>>, Map<String,List<TourData>>> finisher() {
        return new Function<Map<String,List<TourData>>, Map<String,List<TourData>>>(){
            @Override
            public Map<String,List<TourData>> apply(Map<String,List<TourData>> t) {
                for (String key:t.keySet()){
                    List<TourData> tourDatas = t.get(key);
                    tourDatas.sort((td1,td2) -> {
                        if (td1.getDistance() > td2.getDistance()){
                            return 1;
                        } else if (td1.getDistance() < td2.getDistance()) {
                            return -1;
                        } else {
                            return 0;
                        }
                    });
                    List<TourData> helperList = new ArrayList<>();
                    helperList.addAll(tourDatas);
                    for (TourData tourData:tourDatas){
                        double mass = 0;
                        double pallets = 0;
                        for (TourData helper:helperList) {
                            mass = mass + helper.getMass();
                            pallets = pallets + helper.getNumpallets();
                        }
                        tourData.setMass(mass);
                        tourData.setNumpallets(pallets);
                        helperList.remove(tourData);
                    }
                }
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
