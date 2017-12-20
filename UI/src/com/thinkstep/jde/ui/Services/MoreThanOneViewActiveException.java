/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Services;

/**
 *
 * @author forell
 */
public class MoreThanOneViewActiveException extends Exception{

    // Parameterless Constructor
      public MoreThanOneViewActiveException() {}

      // Constructor that accepts a message
      public MoreThanOneViewActiveException(String message)
      {
         super(message);
      }
    
}
