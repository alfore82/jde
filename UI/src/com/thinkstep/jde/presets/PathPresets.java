/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.presets;

import java.io.File;
import org.openide.util.NbPreferences;

/**
 *
 * @author forell
 */
public class PathPresets {
    public static File getPath(){
        File file;
        String path = NbPreferences.forModule(NbPreferences.class).get("currentpath", "");
        if (path.equals("")) {
            return null;
        } else {
            file = new File (new File(path).getParent());
            if (file.exists()){
                return file;
            } else {
                return null;
            }
            
        }
        
    }
    
    public static void setPath(File file){
        NbPreferences.forModule(NbPreferences.class).put("currentpath", file.getAbsolutePath());
    }
}
