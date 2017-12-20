/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

/**
 *
 * @author forell
 */
public class DateComparator implements Comparator<String>{

    @Override
    public int compare(String o1, String o2) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
            Date d1 = df.parse(o1);
            Date d2 = df.parse(o2);
            if (d1.after(d2)){
                return 1;
            } else {
                return -1;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return -1;
    }
    
}
