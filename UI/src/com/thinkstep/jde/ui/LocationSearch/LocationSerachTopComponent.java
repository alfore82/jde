/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.LocationSearch;

import java.awt.BorderLayout;
import java.io.IOException;
import java.net.URL;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Exceptions;
import org.openide.windows.TopComponent;
import org.openide.util.NbBundle.Messages;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(
        dtd = "-//com.thinkstep.jde.ui.LocationSearch//LocationSerach//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "LocationSerachTopComponent",
        //iconBase="SET/PATH/TO/ICON/HERE", 
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "editor", openAtStartup = false)
@ActionID(category = "Window", id = "com.thinkstep.jde.ui.LocationSearch.LocationSerachTopComponent")
@ActionReference(path = "Menu/Window" , position = 6, separatorAfter = 7 )
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_LocationSerachAction",
        preferredID = "LocationSerachTopComponent"
)
@Messages({
    "CTL_LocationSerachAction=Location Matrix",
    "CTL_LocationSerachTopComponent=Location Matrix",
    "HINT_LocationSerachTopComponent=Location Matrix"
})
public final class LocationSerachTopComponent extends TopComponent {

    private static JFXPanel fxPanel;
    
    public LocationSerachTopComponent() {
        initComponents();
        setName("Location Matrix");
        setToolTipText("Location Matrix");
        setLayout(new BorderLayout());
        init();
    }

    private void init(){
        fxPanel = new JFXPanel();
        add(fxPanel, BorderLayout.CENTER);
        Platform.setImplicitExit(false);
        Platform.runLater(new Runnable(){
            @Override
            public void run() {
                createScene();
            }
        });
    }
    
    private void createScene(){
        
        try {
            URL location = getClass().getResource("LocationSearch.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(location);
            fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());        
            Parent root = (Parent) fxmlLoader.load(location.openStream());
            fxPanel.setScene(new Scene(root));
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }
}
