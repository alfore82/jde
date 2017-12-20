/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.imports.filereaders;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.openide.util.Exceptions;

/**
 *
 * @author forell
 */
public class DistanceMatrixProvider {

    
    
    public List<Short> readDistanceMatrix() {
        InputStreamReader reader = null;
        try {
            List<Short> distances = new ArrayList<>();
            URL location = getClass().getResource("eu2017.dm");
            reader = new InputStreamReader(location.openStream(), "Cp850");
            BufferedReader br = new BufferedReader(reader);
            String line;
            String strOld = "0000";
            line = br.readLine();
            line = br.readLine();
            int i = 1;
            while (((line = br.readLine()) != null)){
                
                // split out line
                List<String> parts = splitInIntervals(line, 6);
                for (String part:parts){
                    if (!part.equals("")){
                        if (strOld.equals("0000")){
                            strOld = part;
                        } else {
                            if (!part.equals("0000")){
                                distances.add(Short.parseShort(part));
                                strOld = part;
                                i++;
                            } else {
                                strOld = part;
                            }
                        }
                    }
                }
            }
            return distances;
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        } finally {
            try {
                reader.close();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return null;
        
    }

    /*
        Splits strings into packages of 6 characters, constant spacing file
    */
    private List<String> splitInIntervals(String line, int partitionSize){
        List<String> parts = new ArrayList<String>();
        int len = line.length();
        for (int i=0; i<len; i+=partitionSize)
        {
            parts.add(line.substring(i, Math.min(len, i + partitionSize)).trim());
        }
        return parts;
    }
    
}
