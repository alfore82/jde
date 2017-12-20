/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.Processes;

import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionProcess;
import com.thinkstep.jde.persistence.services.EmissionProcessService;
import com.thinkstep.jde.persistence.services.UpdateNotification;
import com.thinkstep.jde.ui.Services.EmissionProcessEditorService;
import com.thinkstep.jde.ui.ProcessEditor.EmissionProcessEditorTopComponent;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import javax.swing.SwingUtilities;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class EmissionProcessListController implements Initializable, UpdateNotification {

    @FXML
    private TableView<EmissionProcess> tableProcesses;
    @FXML
    private TableColumn<EmissionProcess, String> colName;
    @FXML
    private TableColumn<EmissionProcess, String> colCountry;
    
    ObservableList<EmissionProcess> processes = FXCollections.observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EmissionProcessService ems = EmissionProcessService.getINSTANCE();
        ems.addListener(this);
        processes.addAll(ems.findAllEmissionProcesses());
        tableProcesses.setItems(processes);
        tableProcesses.setEditable(false);
        colName.setCellValueFactory(new Callback<CellDataFeatures<EmissionProcess, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<EmissionProcess, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getName());
                return s;
            }
        
        });
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colCountry.setCellValueFactory(new Callback<CellDataFeatures<EmissionProcess, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<EmissionProcess, String> p) {
                Country c = p.getValue().getCountry();
                SimpleStringProperty s = new SimpleStringProperty();
                if (c==null){
                    s.set("");
                } else {
                    s.set(c.getName());
                }
                return s;
            }
        
        });
        colCountry.setCellFactory(TextFieldTableCell.forTableColumn());
        
        
    }    

    @FXML
    private void newProcess(ActionEvent event) {
        EmissionProcess ep = new EmissionProcess();
        ep.setName("new");
        processes.add(ep);
        tableProcesses.refresh();
        SwingUtilities.invokeLater(()->{
            EmissionProcessEditorService epes = EmissionProcessEditorService.getInstance();
            EmissionProcessEditorTopComponent tc = epes.createEmissionProcessEditorTopComponent(ep);
            tc.open();
            tc.requestActive();
        });
    }

    @FXML
    private void deleteProcess(ActionEvent event) {
        EmissionProcessService ems = EmissionProcessService.getINSTANCE();
        EmissionProcess ei = tableProcesses.getSelectionModel().getSelectedItem();
        ems.deleteEmissionProcess(ei);
    }

    @FXML
    private void editProcess(ActionEvent event) {
        EmissionProcess ep = tableProcesses.getSelectionModel().getSelectedItem();
        if (ep != null){
            SwingUtilities.invokeLater(()->{
                EmissionProcessEditorService epes = EmissionProcessEditorService.getInstance();
                EmissionProcessEditorTopComponent tc = epes.createEmissionProcessEditorTopComponent(ep);
                tc.open();
                tc.requestActive();
            });
        }
    }

    @Override
    public void refresh() {
        Platform.runLater(()->tableProcesses.refresh());
    }
    
}
