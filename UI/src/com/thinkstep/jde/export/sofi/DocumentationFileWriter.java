/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.export.sofi;

import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.rawdata.DataItem;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.entities.rawdata.DataType;
import com.thinkstep.jde.ui.Converters.BusinessUnitStringConverter;
import com.thinkstep.jde.ui.views.progress.ProgressBarViewNonFXThread;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import org.openide.util.Exceptions;

/**
 *
 * @author forell
 */
public class DocumentationFileWriter {
    public static void writeFile(List<DataRow> data, File file, List<DataKey> dataKeys){
        if (data.size()>0){
            try {
                
                PrintWriter test = new PrintWriter(file);

                writeHeaderLine(test, data, dataKeys);
                writeContent(test, data, dataKeys, file.getAbsolutePath());
                test.flush();
                test.close();
            } catch (FileNotFoundException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

    private static void writeHeaderLine(PrintWriter test, List<DataRow> data, List<DataKey> dataKeys) {
        DataRow d = data.get(0);
        SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
        String headerLine = "Date\t"+
                            "Shipment Weight [kg]\t"+
                            "Number of Pallets\t"+
                            "Business Unit\t"+
                            "Pickup Leg - Distance [km]\t"+
                            "Pickup Leg - Vehicle Type\t"+
                            "Pickup Leg - Load Factor [%]\t"+
                            "Pickup Leg - Empty Trip Factor [%]\t"+
                            "Pickup Leg - Fuel Consumed [l, kWg]\t"+
                            "Pickup Leg - GHG Emissions [kg CO2e]\t"+
                            "Main Leg - Distance [km]\t"+
                            "Main Leg - Vehicle Type\t"+
                            "Main Leg - Load Factor [%]\t"+
                            "Main Leg - Empty Trip Factor [%]\t"+
                            "Main Leg - Fuel Consumed [l, kWg]\t"+
                            "Main Leg - GHG Emissions [kg CO2e]\t"+
                            "Delivery Leg - Distance [km]\t"+
                            "Delivery Leg - Vehicle Type\t"+
                            "Delivery Leg - Load Factor [%]\t"+
                            "Delivery Leg - Empty Trip Factor [%]\t"+
                            "Delivery Leg - Fuel Consumed [l, kWg]\t"+
                            "Delivery Leg - GHG Emissions [kg CO2e]\t"+
                            "Error";
        for (DataKey dk:dataKeys){
            headerLine = headerLine + "\t" + dk.getKey();
        }
        
        test.println(headerLine);
    }

    private static void writeContent(PrintWriter test, List<DataRow> data, List<DataKey> dataKeys ,String filename) {
        final ProgressBarViewNonFXThread pbv = new ProgressBarViewNonFXThread("Writing" + filename, "Writing documentation file");
        
        pbv.show();
        
        final double lines = data.size();
        AtomicInteger l = new AtomicInteger();
        l.set(0);
        for (DataRow d:data){
            pbv.setProgress(l.get()/lines);
            l.set(l.get()+1);
            String line = "";
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy", Locale.ENGLISH);
            NumberFormat nf = NumberFormat.getInstance();
            NumberFormat nfperc = NumberFormat.getPercentInstance();
            BusinessUnitStringConverter bus = new BusinessUnitStringConverter();
            if (d.getDate() != null){
                line = df.format(d.getDate().getTime());
            }
            String businessUnit = "";
            if (d.getBusinessUnit() != null){
                businessUnit = bus.toString(d.getBusinessUnit());
            }
            String vehicleTypePickup = "";
            if (d.getVehicleTypePickup() != null){
                vehicleTypePickup = d.getVehicleTypePickup().getName();
            }
            
            String vehicleTypeMain = "";
            if (d.getVehicleTypeMain() != null){
                vehicleTypeMain = d.getVehicleTypeMain().getName();
            }
            
            String vehicleTypeDelivery = "";
            if (d.getVehicleTypeDelivery() != null){
                vehicleTypeDelivery = d.getVehicleTypeDelivery().getName();
            }
            
            line = line + "\t" + nf.format(d.getShipmentWeight())
                        + "\t" + nf.format(d.getNumberOfPallets())
                        + "\t" + businessUnit
                    + "\t" + nf.format(d.getDistancePickup())
                    + "\t" + vehicleTypePickup
                    + "\t" + nfperc.format(d.getLoadFactorPickup())
                    + "\t" + nfperc.format(d.getEmptyTripFactorPickup())
                    + "\t" + nf.format(d.getFuelConsumptionPickup())
                    + "\t" + nf.format(d.getGhgEmissionsPickup())
                    + "\t" + nf.format(d.getDistanceMain())
                    + "\t" + vehicleTypeMain
                    + "\t" + nfperc.format(d.getLoadFactorMain())
                    + "\t" + nfperc.format(d.getEmptyTripFactorMain())
                    + "\t" + nf.format(d.getFuelConsumptionMain())
                    + "\t" + nf.format(d.getGhgEmissionsMain())
                    + "\t" + nf.format(d.getDistanceDelivery())
                    + "\t" + vehicleTypeDelivery
                    + "\t" + nfperc.format(d.getLoadFactorDelivery())
                    + "\t" + nfperc.format(d.getEmptyTripFactorDelivery())
                    + "\t" + nf.format(d.getFuelConsumptionDelivery())
                    + "\t" + nf.format(d.getGhgEmissionsDelivery())
                    + "\t" + d.getError();
            for (DataKey dk:dataKeys){
                DataItem di = d.getDataItem(dk);
                if (di != null){
                    if (di.getDataType() == DataType.NUMERIC){
                        line = line + "\t" + di.getValueNumeric();
                    } else {
                        line = line + "\t" + di.getValueString();
                    }
                }
                
            }
            test.println(line);      
            pbv.close();       
        }
    }
}
