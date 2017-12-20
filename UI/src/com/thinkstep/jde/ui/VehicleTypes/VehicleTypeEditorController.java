/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.VehicleTypes;

import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.fuelconsumptioncalculation.VehicleType;
import com.thinkstep.jde.persistence.services.VehicleTypeService;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;
import static org.apache.poi.xwpf.usermodel.XWPFRun.FontCharRange.cs;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class VehicleTypeEditorController implements Initializable {

    @FXML
    private AnchorPane ap;
    @FXML
    private TableView<VehicleType> tblVehicleTypes;
    @FXML
    private TableColumn<VehicleType, String> colName;
    @FXML
    private TableColumn<VehicleType, String> colMaxPayload;
    @FXML
    private TableColumn<VehicleType, String> colMaxPallets;
    @FXML
    private TableColumn<VehicleType, String> colFuelConsEmpty;
    @FXML
    private TableColumn<VehicleType, String> colFuelConsLoaded;

    
    private ObservableList<VehicleType> vehicleTypes = FXCollections.observableArrayList();
    private VehicleTypeService vehicleTypeService = VehicleTypeService.getINSTANCE();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        vehicleTypes.addAll(vehicleTypeService.findAllVehicleTypes());
        initializeTable();
    }    

    @FXML
    private void addVehicleType(ActionEvent event) {
        VehicleType veh = new VehicleType();
        veh.setName("new");
        vehicleTypes.add(veh);
        vehicleTypeService.addOrEditVehicleType(veh);
        tblVehicleTypes.refresh();
    }

    @FXML
    private void deleteVehicleType(ActionEvent event) {
        VehicleType veh = tblVehicleTypes.getSelectionModel().getSelectedItem();
        if (veh != null){
            vehicleTypes.remove(veh);
            vehicleTypeService.deleteVehicleType(veh);
        }
    }

    private void initializeTable() {
        tblVehicleTypes.setItems(vehicleTypes);
        tblVehicleTypes.setEditable(true);
        colName.setCellValueFactory(new Callback<CellDataFeatures<VehicleType, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<VehicleType, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getName());
                return s;
            }
        
        });
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<VehicleType, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<VehicleType, String> t){
                VehicleType c = ((VehicleType) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                c.setName(t.getNewValue());
                vehicleTypeService.addOrEditVehicleType(c);
            }
        });
        colMaxPayload.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<VehicleType, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<VehicleType, String> p) {
                NumberFormat nf = NumberFormat.getInstance();
                SimpleStringProperty s = new SimpleStringProperty();
                s.setValue(nf.format(p.getValue().getMaxPayload()));
                return s;
            }
        
        });
        colMaxPayload.setCellFactory(TextFieldTableCell.forTableColumn());
        colMaxPayload.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<VehicleType, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<VehicleType, String> t){
                    VehicleType ef = ((VehicleType) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    try {
                        NumberFormat nf = NumberFormat.getInstance();
                        Number value = nf.parse(t.getNewValue());
                        ef.setMaxPayload(value.doubleValue());
                        vehicleTypeService.addOrEditVehicleType(ef);
                    } catch (Exception ex) {
                        ef.setMaxPayload(0.0);
                    }
                    
                }
            });
        colMaxPallets.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<VehicleType, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<VehicleType, String> p) {
                NumberFormat nf = NumberFormat.getInstance();
                SimpleStringProperty s = new SimpleStringProperty();
                s.setValue(nf.format(p.getValue().getMaxNumFloorPallets()));
                return s;
            }
        
        });
        colMaxPallets.setCellFactory(TextFieldTableCell.forTableColumn());
        colMaxPallets.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<VehicleType, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<VehicleType, String> t){
                    VehicleType ef = ((VehicleType) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    try {
                        NumberFormat nf = NumberFormat.getInstance();
                        Number value = nf.parse(t.getNewValue());
                        ef.setMaxNumFloorPallets(value.doubleValue());
                        vehicleTypeService.addOrEditVehicleType(ef);
                    } catch (Exception ex) {
                        ef.setMaxNumFloorPallets(0.0);
                    }
                    
                }
            });
        colFuelConsEmpty.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<VehicleType, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<VehicleType, String> p) {
                NumberFormat nf = NumberFormat.getInstance();
                SimpleStringProperty s = new SimpleStringProperty();
                s.setValue(nf.format(p.getValue().getFuelConsumptionEmpty()));
                return s;
            }
        
        });
        colFuelConsEmpty.setCellFactory(TextFieldTableCell.forTableColumn());
        colFuelConsEmpty.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<VehicleType, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<VehicleType, String> t){
                    VehicleType ef = ((VehicleType) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    try {
                        NumberFormat nf = NumberFormat.getInstance();
                        Number value = nf.parse(t.getNewValue());
                        ef.setFuelConsumptionEmpty(value.doubleValue());
                        vehicleTypeService.addOrEditVehicleType(ef);
                    } catch (Exception ex) {
                        ef.setFuelConsumptionEmpty(0.0);
                    }
                    
                }
            });
        colFuelConsLoaded.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<VehicleType, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<VehicleType, String> p) {
                NumberFormat nf = NumberFormat.getInstance();
                SimpleStringProperty s = new SimpleStringProperty();
                s.setValue(nf.format(p.getValue().getFuelConsumptionLoaded()));
                return s;
            }
        
        });
        colFuelConsLoaded.setCellFactory(TextFieldTableCell.forTableColumn());
        colFuelConsLoaded.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<VehicleType, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<VehicleType, String> t){
                    VehicleType ef = ((VehicleType) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    try {
                        NumberFormat nf = NumberFormat.getInstance();
                        Number value = nf.parse(t.getNewValue());
                        ef.setFuelConsumptionLoaded(value.doubleValue());
                        vehicleTypeService.addOrEditVehicleType(ef);
                    } catch (Exception ex) {
                        ef.setFuelConsumptionLoaded(0.0);
                    }
                    
                }
            });
        
    }
    
}
