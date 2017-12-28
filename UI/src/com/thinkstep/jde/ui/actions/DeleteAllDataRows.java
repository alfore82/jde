/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.actions;

import com.thinkstep.jde.persistence.services.DataRowService;
import com.thinkstep.jde.ui.progressbar.ProgressbarController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;

@ActionID(
        category = "Edit",
        id = "com.thinkstep.jde.ui.actions.DeleteAllDataRows"
)
@ActionRegistration(
        displayName = "#CTL_DeleteAllDataRows"
)
@ActionReferences({
    @ActionReference(path = "Menu/Edit", position = 3333),
    @ActionReference(path = "Shortcuts", name = "D-DELETE")
})
@Messages("CTL_DeleteAllDataRows=Delete data for all Lsps")
public final class DeleteAllDataRows implements ActionListener {

    ProgressbarController pgbController;
    
    @Override
    public void actionPerformed(ActionEvent e) {
        Platform.runLater(()->{
            try {
                
                Stage stage = new Stage();
                URL location = getClass().getResource("/com/thinkstep/jde/ui/progressbar/Progressbar.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(location);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                Parent root = (Parent) fxmlLoader.load(location.openStream());
                pgbController = (ProgressbarController) fxmlLoader.getController();
                pgbController.setTitle("Deleting Data");
                pgbController.setAction("Deleting all data for all Lsps");
                pgbController.setProgress(-0.1);
                Scene scene = new Scene(root);
                stage.setTitle("Deleting data");
                stage.setScene(scene);
                stage.show();
                new Thread(){
                    public void run(){
                        DataRowService drService = DataRowService.getINSTANCE();
                        drService.deleteAllDataRows();
                        Platform.runLater(()->stage.close());
                        
                    }
                }.start();
                
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        });        
    }
}
