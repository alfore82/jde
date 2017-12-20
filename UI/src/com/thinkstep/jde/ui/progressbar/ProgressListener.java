/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.progressbar;

/**
 *
 * @author forell
 */
public interface ProgressListener {
    
    public void setTitle(String title);
    public void setAction(String action);
    public void setProgress(double progress);
    
}
