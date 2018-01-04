/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.actions;

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
        category = "File",
        id = "com.thinkstep.jde.ui.actions.ChangeDataBase"
)
@ActionRegistration(
        displayName = "#CTL_ChangeDataBase"
)
@ActionReferences({
    @ActionReference(path = "Menu/File", position = 900, separatorAfter = 950),
    @ActionReference(path = "Shortcuts", name = "D-D")
})
@Messages("CTL_ChangeDataBase=Change database")
public final class ChangeDataBase implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent e) {
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                try {
                    Stage stage = new Stage();
                    URL location = getClass().getResource("ChangeDatabase.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(location);
                    fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                    Parent root = (Parent) fxmlLoader.load(location.openStream());
                    ChangeDatabaseController ctrl = (ChangeDatabaseController) fxmlLoader.getController();
                    Scene scene = new Scene(root);
                    stage.setTitle("Change database location");
                    stage.setScene(scene);
                    stage.show();
                } catch (IOException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
            
        });
    }
}
