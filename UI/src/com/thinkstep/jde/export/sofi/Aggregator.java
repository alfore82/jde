/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.export.sofi;

import com.thinkstep.jde.persistence.entities.BusinessUnit;
import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.TourIdentifierService;
import com.thinkstep.jde.ui.Converters.BusinessUnitStringConverter;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author forell
 */
public class Aggregator {
    public List<LineItem> getAggregatedResults(List<DataRow> dataRows, DataKey tourId){
        Calendar cal = Calendar.getInstance();
        Set<Integer> yearSet = new HashSet<>();
        for (DataRow dr:dataRows){
            cal = dr.getDate();
            yearSet.add(cal.get(Calendar.YEAR));
        }
        List<Integer> years = new ArrayList<>();
        List<Integer> months = new ArrayList<>();
        months.add(Calendar.JANUARY);
        months.add(Calendar.FEBRUARY);
        months.add(Calendar.MARCH);
        months.add(Calendar.APRIL);
        months.add(Calendar.MAY);
        months.add(Calendar.JUNE);
        months.add(Calendar.JULY);
        months.add(Calendar.AUGUST);
        months.add(Calendar.SEPTEMBER);
        months.add(Calendar.OCTOBER);
        months.add(Calendar.NOVEMBER);
        months.add(Calendar.DECEMBER);
        
        
        years.addAll(yearSet);
        List<LineItem> lineItems= new ArrayList<>();
        LineItem headerLine = new LineItem();
        headerLine.setFile("" + dataRows.get(0).getLsp().getName());
        
        String header = "\""            + "LogisticServiceProvider"
                            + "\";\""   + "BusinessUnit"
                            + "\";\""   + "Site ID"
                            + "\";\""   + "Term"
                            + "\";\""   + "GWP"
                            + "\";\""   + "GWP Unit"
                            + "\";\""   + "Number of Deliveries"
                            + "\";\""   + "Number of Deliveries Unit"
                            + "\";\""   + "Mass shipped"
                            + "\";\""   + "Mass shipped Unit"
                            + "\";\""   + "Haulage"
                            + "\";\""   + "Haulage Unit"
                            + "\";\""   + "Distance driven"
                            + "\";\""   + "Distance driven Unit"
                            + "\";\""   + "GHG Transport"
                            + "\";\""   + "GHG Transport" + "\"";
        
        headerLine.setLine(header);
        headerLine.setSite("" + dataRows.get(0).getLsp().getSofiSiteId());
        headerLine.setTerm("");
        lineItems.add(headerLine);
        
        
        for (Integer year:years) {
            for (Integer month:months){
                List<DataRow> dataRowsPerMonth = new ArrayList<>();
                for (DataRow dataRow:dataRows){
                    Calendar c = dataRow.getDate();
                    if (c.get(Calendar.YEAR) == year && c.get(Calendar.MONTH) == month){
                        dataRowsPerMonth.add(dataRow);
                    }
                }
                if (dataRowsPerMonth.size() > 0){
                    lineItems.addAll(getLineItems(dataRowsPerMonth, tourId));
                }
            }
        }
        
        return lineItems;
    }

    private List<LineItem> getLineItems(List<DataRow> dataRowsPerMonth, DataKey tourId) {
        List<LineItem> result = new ArrayList<>();
        List<DataRow> business = new ArrayList<>();
        List<DataRow> retail = new ArrayList<>();
        for (DataRow dataRow:dataRowsPerMonth){
            if(dataRow.getBusinessUnit().equals(BusinessUnit.PROFESSIONAL)) business.add(dataRow);
            else retail.add(dataRow);
        }
        if (business.size() > 0) {
            LineItem buisnessLine = aggregateLine(business, tourId);
            result.add(buisnessLine);
        }
        if (retail.size() > 0) {
            LineItem retailLine = aggregateLine(retail, tourId);
            result.add(retailLine);
        }
        return result;
    }

    private LineItem aggregateLine(List<DataRow> lines, DataKey tourId) {
        LineItem li = new LineItem();
        BusinessUnitStringConverter sb = new BusinessUnitStringConverter();
        TourIdentifierService tiService = TourIdentifierService.getINSTANCE();
        
        
        String bu = sb.toString(lines.get(0).getBusinessUnit());
        String lspString = lines.get(0).getLsp().getName();
        int siteId = lines.get(0).getLsp().getSofiSiteId();
        String termMonth = ""+(lines.get(0).getDate().get(Calendar.MONTH) + 1);
        if (termMonth.length()==1){
            termMonth = "0" + termMonth;
        }
         String term = ""+lines.get(0).getDate().get(Calendar.YEAR) + termMonth;
        double ghg = 0.0;
        double numberParcels = lines.size();
        double massShipped = 0.0;
        double tonneKms = 0.0;
        
        for (DataRow line:lines){
            
            
            ghg = ghg + checkValue(line.getGhgEmissionsDelivery()) + checkValue(line.getGhgEmissionsMain()) + checkValue(line.getGhgEmissionsPickup());
            System.out.println(ghg);
            massShipped = massShipped + checkValue(line.getShipmentWeight());
            tonneKms = tonneKms + checkValue(line.getShipmentWeight()) * checkValue((line.getDistanceDelivery()) + checkValue(line.getDistanceMain()) + checkValue(line.getDistancePickup()));
        
        }
        
        Map<String, Double> tours = lines.stream().collect(new VehicleKmCollector(tourId));
        Set<String> keys = tours.keySet();
        Double vehkmTotal = 0.0;
        for (String key:keys){
            vehkmTotal = vehkmTotal + tours.get(key);

        }
        
        
        Double vehicleKms = vehkmTotal;
        
        String questionnaire = "GHG logistics";
        NumberFormat nf = NumberFormat.getInstance(Locale.GERMANY);
        nf.setGroupingUsed(false);
        li.setSite(""+siteId);
        li.setTerm(term);
        String line = "\""              + lspString
                            + "\";\""   + bu
                            + "\";\""   + siteId
                            + "\";\""   + term
                            + "\";\""   + nf.format(ghg)
                            + "\";\""   + "kg CO2"
                            + "\";\""   + nf.format(numberParcels)
                            + "\";\""   + "unit"
                            + "\";\""   + nf.format(massShipped)
                            + "\";\""   + "kg"
                            + "\";\""   + nf.format(tonneKms)
                            + "\";\""   + "kgkm"
                            + "\";\""   + nf.format(vehicleKms)
                            + "\";\""   + "km"
                            + "\";\""   + "GHG Transport"
                            + "\";\""   + "GHG Transport" + "\"";
        
        li.setLine(line);
        
        return li;
    }
    
    private double checkValue(double value){
        double returnvalue = value;
        if ( Double.isNaN(returnvalue) || Double.isInfinite(returnvalue))
                returnvalue = 0.0;
        return returnvalue;
    }
}
