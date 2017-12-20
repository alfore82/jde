/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.imports.filereaders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author forell
 */
public class DateExtractor {
    public static Date extractDate(String data){
        if(data == null){
            return null;
        }
        List<String> dateFormats = new ArrayList<>();
        dateFormats.add("dd.MM.yyyy");
        dateFormats.add("MM/dd/yyyy");
        for (String dateFormat:dateFormats){
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            sdf.setLenient(false);
            try {
                String dateToValidate;
                Date date = sdf.parse(data);
                return date;
            } catch (ParseException e) {
                // do nothing
            }
            
        }
        return null;
        
    }
}
