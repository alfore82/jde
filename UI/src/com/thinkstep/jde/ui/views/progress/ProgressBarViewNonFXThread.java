/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.views.progress;

import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.openide.util.Exceptions;

/**
 *
 * @author forell
 */
public class ProgressBarViewNonFXThread {
    
    private ProgressBarController pbc;
    private Stage stage;
    
    public ProgressBarViewNonFXThread(){
        Platform.runLater(()->{
            try {
                URL location = ProgressBarViewNonFXThread.class.getResource("ProgressBar.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(location);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                Parent root = (Parent) fxmlLoader.load(location.openStream());
                Scene scene = new Scene(root);
                stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UTILITY);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setAlwaysOnTop(true);

                pbc = (ProgressBarController) fxmlLoader.getController();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
        
    }
    
    public ProgressBarViewNonFXThread(String processedFile){
        Platform.runLater(()->{
            try {
                URL location = getClass().getResource("ProgressBar.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(location);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                Parent root = (Parent) fxmlLoader.load(location.openStream());
                Scene scene = new Scene(root);
                stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.centerOnScreen();
                pbc = (ProgressBarController) fxmlLoader.getController();
                pbc.setImportedItemText(processedFile);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
    }
    
    public ProgressBarViewNonFXThread(String processedFile, String action){
        Platform.runLater(()->{
            try {
                URL location = getClass().getResource("ProgressBar.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(location);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                Parent root = (Parent) fxmlLoader.load(location.openStream());
                Scene scene = new Scene(root);
                stage = new Stage();
                stage.setScene(scene);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.centerOnScreen();
                pbc = (ProgressBarController) fxmlLoader.getController();
                pbc.setImportedItemText(processedFile);
                pbc.setAction(action);
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });
    }
    
    public void show(){
        Platform.runLater(()->{
            stage.show();
        });    
    }
    
    public void setProcessedFile(String processedFile){
        Platform.runLater(()->{
            pbc.setImportedItemText(processedFile);
        });
    }
    
    
    
    public void setProgress(double progress){
        Platform.runLater(()->{
            pbc.setProgress(progress);
        });
    }
    
    public void close(){
        Platform.runLater(()->{
            stage.close();
        });
    }
}
