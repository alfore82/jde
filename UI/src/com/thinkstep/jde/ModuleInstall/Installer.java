/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ModuleInstall;

import com.thinkstep.jde.imports.filereaders.DateExtractor;
import com.thinkstep.jde.persistence.services.DistanceService;
import org.openide.modules.ModuleInstall;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        DistanceService ds = DistanceService.getINSTANCE();
        System.out.println(ds.isInitialized());
        
        
    }

}
