/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.BusyOverlay;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import org.openide.util.Exceptions;

/**
 *
 * @author forell
 */
public class Overlay {
    private Parent root;
    private AnchorPane ap;
    private Runnable run;
    
    public Overlay(AnchorPane ap, Runnable run){
            
          
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.execute(()->{    
            Platform.runLater(()->{
                try {
                    URL location = getClass().getResource("Overlay.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                    root = (Parent) fxmlLoader.load(location.openStream());
                    AnchorPane.setBottomAnchor(root, 0.0);
                    AnchorPane.setLeftAnchor(root, 0.0);
                    AnchorPane.setTopAnchor(root, 0.0);
                    AnchorPane.setRightAnchor(root, 0.0);
                    ap.getChildren().add(root);
                    root.toFront();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }

            });
        });
        exec.execute(run);
        exec.execute(()->{
            Platform.runLater(()->ap.getChildren().remove(root));
        });

    }
    
    public Overlay(AnchorPane ap, Runnable run, double bottomAnchor, double topAnchor, double leftAnchor, double rightAnchor){
        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
        exec.execute(()->{    
            Platform.runLater(()->{
                try {
                    URL location = getClass().getResource("Overlay.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                    root = (Parent) fxmlLoader.load(location.openStream());
                    AnchorPane.setBottomAnchor(root, bottomAnchor);
                    AnchorPane.setLeftAnchor(root, leftAnchor);
                    AnchorPane.setTopAnchor(root, topAnchor);
                    AnchorPane.setRightAnchor(root, rightAnchor);
                    ap.getChildren().add(root);
                    root.toFront();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }

            });
        });
        exec.execute(run);
        exec.execute(()->{
            Platform.runLater(()->ap.getChildren().remove(root));
        });
    }
}
