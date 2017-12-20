/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.views.datatable;

import com.thinkstep.jde.calculations.Calculations;
import com.thinkstep.jde.export.sofi.Aggregator;
import com.thinkstep.jde.export.sofi.ConnectionBuilder;
import com.thinkstep.jde.export.sofi.DocumentationFileWriter;
import com.thinkstep.jde.export.sofi.LineItem;
import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.Rules.ParentConnector;
import com.thinkstep.jde.persistence.entities.fuelconsumptioncalculation.VehicleType;
import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.preferences.TourIdentifier;
import com.thinkstep.jde.persistence.entities.rawdata.DataItem;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.entities.rawdata.DataType;
import com.thinkstep.jde.persistence.services.DataKeyService;
import com.thinkstep.jde.persistence.services.DataRowService;
import com.thinkstep.jde.persistence.services.LogisticServiceProviderService;
import com.thinkstep.jde.persistence.services.TourIdentifierService;
import com.thinkstep.jde.persistence.services.UpdateNotification;
import com.thinkstep.jde.ui.BusinessUnitMapping.BusinessUnitMappingTopComponent;
import com.thinkstep.jde.ui.BusyOverlay.Overlay;
import com.thinkstep.jde.ui.Converters.BusinessUnitStringConverter;
import com.thinkstep.jde.ui.Converters.DateComparator;
import com.thinkstep.jde.ui.Converters.LogisticServiceProviderStringConverter;
import com.thinkstep.jde.ui.Converters.NumberComparator;
import com.thinkstep.jde.ui.DateMapping.DateMappingTopComponent;
import com.thinkstep.jde.ui.DistanceMapping.DistanceMappingTopComponent;
import com.thinkstep.jde.ui.FuelTypeMapping.FuelTypeMappingTopComponent;
import com.thinkstep.jde.ui.LoadFactorMapping.LoadFactorMappingTopComponent;
import com.thinkstep.jde.ui.MapTourIdentifier.TourIdentifierController;
import com.thinkstep.jde.ui.Services.BusinessUnitMapperService;
import com.thinkstep.jde.ui.Services.DateMapperService;
import com.thinkstep.jde.ui.Services.DistanceMapperService;
import com.thinkstep.jde.ui.Services.FuelTypeMapperService;
import com.thinkstep.jde.ui.Services.LoadFactorMapperService;
import com.thinkstep.jde.ui.Services.ShipmentWeightMapperService;
import com.thinkstep.jde.ui.Services.VehicleTypeMapperService;
import com.thinkstep.jde.ui.ShipmentWeightMapping.ShipmentWeightMappingTopComponent;
import com.thinkstep.jde.ui.VehicleTypeMapping.VehicleTypeMappingTopComponent;
import com.thinkstep.jde.ui.views.progress.ProgressBarView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.util.Callback;
import javax.swing.SwingUtilities;
import org.openide.util.Exceptions;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class DataTableController implements Initializable, UpdateNotification {

    @FXML
    private ComboBox<LogisticServiceProvider> cbLsp;

    private ObservableList<LogisticServiceProvider> lsps = FXCollections.observableArrayList();
    private ObservableList<DataRow> drs = FXCollections.observableArrayList();
    @FXML
    private TableView<DataRow> tblData;
    @FXML
    private TableColumn<DataRow, String> colDate;
    @FXML
    private TableColumn<DataRow, String> colError;
    @FXML
    private TableColumn<DataRow, String> colShipmentWeight;
    @FXML
    private TableColumn<DataRow, String> colNumberOfPallets;
    @FXML
    private TableColumn<DataRow, String> colGhgEmissionsMain;
    @FXML
    private TableColumn<DataRow, String> colFuelConsumptionMain;
    @FXML
    private TableColumn<DataRow, String> colDistanceMain;
    @FXML
    private TableColumn<DataRow, String> colVehicleTypeMain;
    @FXML
    private TableColumn<DataRow, String> colLoadFactorMain;
    @FXML
    private TableColumn<DataRow, String> colEmptyTripFactorMain;
    @FXML
    private TableColumn<DataRow, String> colGhgEmissionsPickup;
    @FXML
    private TableColumn<DataRow, String> colFuelConsumptionPickup;
    @FXML
    private TableColumn<DataRow, String> colDistancePickup;
    @FXML
    private TableColumn<DataRow, String> colVehicleTypePickup;
    @FXML
    private TableColumn<DataRow, String> colLoadFactorPickup;
    @FXML
    private TableColumn<DataRow, String> colEmptyTripFactorPickup;
    @FXML
    private TableColumn<DataRow, String> colGhgEmissionsDelivery;
    @FXML
    private TableColumn<DataRow, String> colFuelConsumptionDelivery;
    @FXML
    private TableColumn<DataRow, String> colDistanceDelivery;
    @FXML
    private TableColumn<DataRow, String> colVehicleTypeDelivery;
    @FXML
    private TableColumn<DataRow, String> colLoadFactorDelivery;
    @FXML
    private TableColumn<DataRow, String> colEmptyTripFactorDelivery;
    @FXML
    private TableColumn<DataRow, String> colBusinessUnit;
    
    @FXML
    private TableColumn<DataRow, String> colRawData;
    @FXML
    private TableColumn<?, ?> colGhgEmissions1;
    @FXML
    private TableColumn<?, ?> colGhgEmissions11;
    @FXML
    private TableColumn<?, ?> colGhgEmissions111;
    @FXML
    private AnchorPane apMain;

    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeCombobox();
        initializeTable();
    }

    private void initializeCombobox(){
        LogisticServiceProviderService lspservice = LogisticServiceProviderService.getINSTANCE();
        lspservice.addListener(this);
        lsps.addAll(lspservice.findAllLsps());
        cbLsp.setItems(lsps);
        cbLsp.setConverter(new LogisticServiceProviderStringConverter());
        cbLsp.setOnAction(ev->{
            double bottomAnchor = AnchorPane.getBottomAnchor(tblData);
            double topAnchor = AnchorPane.getTopAnchor(tblData);
            double leftAnchor = AnchorPane.getLeftAnchor(tblData);
            double rightAnchor = AnchorPane.getRightAnchor(tblData);
            LogisticServiceProvider lsp = cbLsp.getSelectionModel().getSelectedItem();
            if (lsp != null){
                Overlay ov = new Overlay(apMain,()->{
                    DataRowService drService = DataRowService.getINSTANCE();
                    drs.clear();
                    drs.addAll(drService.findAllDataRowsByLsp(lsp));
                    Platform.runLater(()->createRawDataColumns(lsp));

                }, bottomAnchor, topAnchor, leftAnchor, rightAnchor);
            }
        });
    }

    private void initializeTable() {
        tblData.setItems(drs);
        tblData.tableMenuButtonVisibleProperty().set(true);
        tblData.setEditable(true);
        colDate.setEditable(false);
        colDate.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                Calendar date = p.getValue().getDate();
                if (date != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
                    s.set(sdf.format(date.getTime()));
                } else {
                    s.set("");
                }
                
                
                return s;
            }
        
        });
        colDate.setCellFactory(TextFieldTableCell.forTableColumn());
        colDate.setComparator(new DateComparator());
        colBusinessUnit.setEditable(false);
        colBusinessUnit.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                BusinessUnitStringConverter busc = new BusinessUnitStringConverter();
                s.set(busc.toString(p.getValue().getBusinessUnit()));
                return s;
            }
        
        });
        colBusinessUnit.setCellFactory(TextFieldTableCell.forTableColumn());
        colGhgEmissionsMain.setEditable(false);
        colGhgEmissionsMain.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getGhgEmissionsMain()));
                return s;
            }
        
        });
        colGhgEmissionsMain.setCellFactory(TextFieldTableCell.forTableColumn());
        colGhgEmissionsMain.setComparator(new NumberComparator());
        colGhgEmissionsPickup.setEditable(false);
        colGhgEmissionsPickup.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getGhgEmissionsPickup()));
                return s;
            }
        
        });
        colGhgEmissionsPickup.setCellFactory(TextFieldTableCell.forTableColumn());
        colGhgEmissionsPickup.setComparator(new NumberComparator());
        colGhgEmissionsDelivery.setEditable(false);
        colGhgEmissionsDelivery.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getGhgEmissionsDelivery()));
                return s;
            }
        
        });
        colGhgEmissionsDelivery.setCellFactory(TextFieldTableCell.forTableColumn());
        colGhgEmissionsDelivery.setComparator(new NumberComparator());
        colFuelConsumptionMain.setEditable(false);
        colFuelConsumptionMain.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getFuelConsumptionMain()));
                return s;
            }
        
        });
        colFuelConsumptionMain.setCellFactory(TextFieldTableCell.forTableColumn());
        colFuelConsumptionMain.setComparator(new NumberComparator());
        colFuelConsumptionPickup.setEditable(false);
        colFuelConsumptionPickup.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getFuelConsumptionPickup()));
                return s;
            }
        
        });
        colFuelConsumptionPickup.setCellFactory(TextFieldTableCell.forTableColumn());
        colFuelConsumptionPickup.setComparator(new NumberComparator());
        colFuelConsumptionDelivery.setEditable(false);
        colFuelConsumptionDelivery.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getFuelConsumptionDelivery()));
                return s;
            }
        
        });
        colFuelConsumptionDelivery.setCellFactory(TextFieldTableCell.forTableColumn());
        colFuelConsumptionDelivery.setComparator(new NumberComparator());
        colDistanceMain.setEditable(false);
        colDistanceMain.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getDistanceMain()));
                return s;
            }
        
        });
        colDistanceMain.setCellFactory(TextFieldTableCell.forTableColumn());
        colDistanceMain.setComparator(new NumberComparator());
        colDistancePickup.setEditable(false);
        colDistancePickup.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getDistancePickup()));
                return s;
            }
        
        });
        colDistancePickup.setCellFactory(TextFieldTableCell.forTableColumn());
        colDistancePickup.setComparator(new NumberComparator());
        colDistanceDelivery.setEditable(false);
        colDistanceDelivery.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getDistanceDelivery()));
                return s;
            }
        
        });
        colDistanceDelivery.setCellFactory(TextFieldTableCell.forTableColumn());
        colDistanceDelivery.setComparator(new NumberComparator());
        colShipmentWeight.setEditable(false);
        colShipmentWeight.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getShipmentWeight()));
                return s;
            }
        
        });
        colShipmentWeight.setCellFactory(TextFieldTableCell.forTableColumn());
        colShipmentWeight.setComparator(new NumberComparator());
        colVehicleTypeMain.setEditable(false);
        colVehicleTypeMain.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                VehicleType vt = p.getValue().getVehicleTypeMain();
                if (vt != null){
                    s.set(vt.getName());
                } else {
                    s.set("");
                }
                return s;
            }
        
        });
        colVehicleTypeMain.setCellFactory(TextFieldTableCell.forTableColumn());
        colVehicleTypePickup.setEditable(false);
        colVehicleTypePickup.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                VehicleType vt = p.getValue().getVehicleTypePickup();
                if (vt != null){
                    s.set(vt.getName());
                } else {
                    s.set("");
                }
                return s;
            }
        
        });
        colVehicleTypePickup.setCellFactory(TextFieldTableCell.forTableColumn());
        colVehicleTypeDelivery.setEditable(false);
        colVehicleTypeDelivery.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                VehicleType vt = p.getValue().getVehicleTypeDelivery();
                if (vt != null){
                    s.set(vt.getName());
                } else {
                    s.set("");
                }
                return s;
            }
        
        });
        colVehicleTypeDelivery.setCellFactory(TextFieldTableCell.forTableColumn());
        
        colLoadFactorMain.setEditable(false);
        colLoadFactorMain.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getPercentInstance();
                s.set(nf.format(p.getValue().getLoadFactorMain()));
                return s;
            }
        
        });
        colLoadFactorMain.setCellFactory(TextFieldTableCell.forTableColumn());
        colLoadFactorMain.setComparator(new NumberComparator());
        colLoadFactorPickup.setEditable(false);
        colLoadFactorPickup.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getPercentInstance();
                s.set(nf.format(p.getValue().getLoadFactorPickup()));
                return s;
            }
        
        });
        colLoadFactorPickup.setCellFactory(TextFieldTableCell.forTableColumn());
        colLoadFactorPickup.setComparator(new NumberComparator());
        colLoadFactorDelivery.setEditable(false);
        colLoadFactorDelivery.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getPercentInstance();
                s.set(nf.format(p.getValue().getLoadFactorDelivery()));
                return s;
            }
        
        });
        colLoadFactorDelivery.setCellFactory(TextFieldTableCell.forTableColumn());
        colLoadFactorDelivery.setComparator(new NumberComparator());
        
        colEmptyTripFactorMain.setEditable(false);
        colEmptyTripFactorMain.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getPercentInstance();
                s.set(nf.format(p.getValue().getEmptyTripFactorMain()));
                return s;
            }
        
        });
        colEmptyTripFactorMain.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmptyTripFactorMain.setComparator(new NumberComparator());
        colEmptyTripFactorPickup.setEditable(false);
        colEmptyTripFactorPickup.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getPercentInstance();
                s.set(nf.format(p.getValue().getEmptyTripFactorPickup()));
                return s;
            }
        
        });
        colEmptyTripFactorPickup.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmptyTripFactorPickup.setComparator(new NumberComparator());
        colEmptyTripFactorDelivery.setEditable(false);
        colEmptyTripFactorDelivery.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getPercentInstance();
                s.set(nf.format(p.getValue().getEmptyTripFactorDelivery()));
                return s;
            }
        
        });
        colEmptyTripFactorDelivery.setCellFactory(TextFieldTableCell.forTableColumn());
        colEmptyTripFactorDelivery.setComparator(new NumberComparator());
        
        colNumberOfPallets.setEditable(false);
        colNumberOfPallets.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getInstance();
                s.set(nf.format(p.getValue().getNumberOfPallets()));
                return s;
            }
        
        });
        colNumberOfPallets.setCellFactory(TextFieldTableCell.forTableColumn());
        colNumberOfPallets.setComparator(new NumberComparator());
        
        colError.setEditable(false);
        colError.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.set(p.getValue().getError());
                return s;
            }
        
        });
        colError.setCellFactory(TextFieldTableCell.forTableColumn());
        
        colRawData.setVisible(false);
        
        
        
        
    }

    

    @FXML
    private void FuelMapping(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getValue();
        if (lsp != null){
            SwingUtilities.invokeLater(()->{
                FuelTypeMapperService mapperService = FuelTypeMapperService.getInstance();
                FuelTypeMappingTopComponent tc = mapperService.createFuelTypeMappingTopComponent(lsp);
                tc.open();
                tc.requestActive();
            });
        }
    }

    private void createRawDataColumns(LogisticServiceProvider lsp) {
        colRawData.getColumns().clear();
        DataKeyService dkService = DataKeyService.getINSTANCE();
        List<DataKey> dks = dkService.findDataKeysByLsp(lsp);
        for (DataKey dk:dks) {
            TableColumn<DataRow, String> col = new TableColumn<>(dk.getKey());
            col.setCellValueFactory(new Callback<CellDataFeatures<DataRow, String>,ObservableValue<String>>(){
                @Override
                public ObservableValue<String> call(CellDataFeatures<DataRow, String> p) {
                    SimpleStringProperty s = new SimpleStringProperty();
                    DataRow dr = p.getValue();
                    DataItem di = dr.getDataItem(dk);
                    if (di != null) {
                        if (di.getDataType().equals(DataType.NUMERIC)){
                            NumberFormat nf = NumberFormat.getInstance();
                            nf.setGroupingUsed(false);
                            s.set(nf.format(di.getValueNumeric()));
                        } else {
                            s.set(di.getValueString());
                        }
                    } else {
                        s.set("");
                    }
                    return s;
                }

            });
            col.setCellFactory(TextFieldTableCell.forTableColumn());
            col.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<DataRow, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<DataRow, String> t){
                    DataRow dr = ((DataRow) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    DataItem di = dr.getDataItem(dk);
                    if (di.getDataType().equals(DataType.NUMERIC)){
                        NumberFormat nf = NumberFormat.getInstance();
                        try {
                            di.setValueNumeric(nf.parse(t.getNewValue()).doubleValue());
                        } catch (ParseException ex) {
                            di.setValueNumeric(0);
                        } 
                    } else {
                        di.setValueString(t.getNewValue());
                    }
                }
            });
            col.setEditable(true);
            colRawData.getColumns().add(col);
        }
        
        colRawData.setVisible(true);
        tblData.applyCss();
        tblData.layout();
    }

    @FXML
    private void vehicleTypeMapping(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getValue();
        if (lsp != null){
            SwingUtilities.invokeLater(()->{
                VehicleTypeMapperService mapperService = VehicleTypeMapperService.getInstance();
                VehicleTypeMappingTopComponent tc = mapperService.createVehicleTypeMappingTopComponent(lsp);
                tc.open();
                tc.requestActive();
            });
        }
        
    }

    @FXML
    private void loadFactorMapping(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getValue();
        if (lsp != null){
            SwingUtilities.invokeLater(()->{
                LoadFactorMapperService mapperService = LoadFactorMapperService.getInstance();
                LoadFactorMappingTopComponent tc = mapperService.createLoadFactorMappingTopComponent(lsp);
                tc.open();
                tc.requestActive();
            });
        }
    }

    @FXML
    private void shipmentWeightMapping(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getValue();
        if (lsp != null){
            SwingUtilities.invokeLater(()->{
                ShipmentWeightMapperService mapperService = ShipmentWeightMapperService.getInstance();
                ShipmentWeightMappingTopComponent tc = mapperService.createShipmentWeightMappingTopComponent(lsp);
                tc.open();
                tc.requestActive();
            });
        }
    }

    @FXML
    private void distanceMapping(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getValue();
        if (lsp != null){
            SwingUtilities.invokeLater(()->{
                DistanceMapperService mapperService = DistanceMapperService.getInstance();
                DistanceMappingTopComponent tc = mapperService.createDistanceMappingTopComponent(lsp);
                tc.open();
                tc.requestActive();
            });
        }
    }
    
    @FXML
    private void calculateGhgEmission(ActionEvent event) {
        LogisticServiceProvider lsp = this.cbLsp.getValue();
        if (lsp != null){
            ProgressBarView pbv = new ProgressBarView("Running calculations", "Running calculations");
            pbv.setProgress(0);
            pbv.show();
            new Thread(){
                @Override
                public void run(){
                    Platform.runLater(()->pbv.setProcessedFile("Assigning buisness unit"));
                    new Calculations().calculate(lsp, drs, MappingType.BUISNESSUNIT, null, ParentConnector.ALL);
                    Platform.runLater(()->{
                        pbv.setProcessedFile("Assigning buisness date");
                        pbv.setProgress(1.0/7.0);
                    });
                    new Calculations().calculate(lsp, drs, MappingType.DATE, null, ParentConnector.ALL);
                    Platform.runLater(()->{
                        pbv.setProcessedFile("Assigning shipment weight");
                        pbv.setProgress(2.0/7.0);
                    });
                    new Calculations().calculate(lsp, drs, MappingType.SHIPMENTWEIGHT, null, ParentConnector.ALL);
                    Platform.runLater(()->{
                        pbv.setProcessedFile("Assigning distance");
                        pbv.setProgress(3.0/7.0);
                    });
                    new Calculations().calculate(lsp, drs, MappingType.DISTANCE, null, ParentConnector.ALL);
                    Platform.runLater(()->{
                    pbv.setProcessedFile("Assigning vehicle type");
                    pbv.setProgress(4.0/7.0);
                    });
                    new Calculations().calculate(lsp, drs, MappingType.VEHICLETYPE, null, ParentConnector.ALL);
                    Platform.runLater(()->{
                        pbv.setProcessedFile("Assigning load factor");
                        pbv.setProgress(5.0/7.0);
                    });
                    new Calculations().calculate(lsp, drs, MappingType.LOADFACTOR, null, ParentConnector.ALL);
                    Platform.runLater(()->{
                    pbv.setProcessedFile("Assigning fuel type");
                    pbv.setProgress(6.0/7.0);
                    });
                    new Calculations().calculate(lsp, drs, MappingType.FUELTYPE, null, ParentConnector.ALL);

                    
                    Platform.runLater(()->{
                        pbv.close();
                        tblData.refresh();
                    });
                }
            }.start();
        }
    }

    

    

    



    @FXML
    private void dateMapping(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getValue();
        if (lsp != null){
            SwingUtilities.invokeLater(()->{
                DateMapperService mapperService = DateMapperService.getInstance();
                DateMappingTopComponent tc = mapperService.createDateMappingTopComponent(lsp);
                tc.open();
                tc.requestActive();
            });
        }
    }

    @Override
    public void refresh() {
        lsps.clear();
        lsps.addAll(LogisticServiceProviderService.getINSTANCE().findAllLsps());
    }

   
    @FXML
    private void mapBu(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getValue();
        if (lsp != null){
            SwingUtilities.invokeLater(()->{
                BusinessUnitMapperService mapperService = BusinessUnitMapperService.getInstance();
                BusinessUnitMappingTopComponent tc = mapperService.createBusinessUnitMappingTopComponent(lsp);
                tc.open();
                tc.requestActive();
            });
        }
        
    }

    @FXML
    private void exportSoFi(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getValue();
        if (lsp != null) {
            TourIdentifierService tidService = TourIdentifierService.getINSTANCE();
            TourIdentifier tid = tidService.findTourIdentifier(lsp);
            if (tid != null && tid.getDataKey() != null){
                new Thread(){
                    @Override
                    public void run(){
                        Aggregator agg = new Aggregator();
                        List<LineItem> dis = agg.getAggregatedResults(drs, tid.getDataKey());
                        ConnectionBuilder.importData(dis);
                    }
                }.start();        
            }
        }
    }

    @FXML
    private void exportFile(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getSelectionModel().getSelectedItem();
        if (lsp != null) {
            TourIdentifierService tidService = TourIdentifierService.getINSTANCE();
            TourIdentifier tid = tidService.findTourIdentifier(lsp);
            if (tid != null && tid.getDataKey() != null){
                File file;
                FileChooser fileChooser = new FileChooser();
                ExtensionFilter ef1 = new FileChooser.ExtensionFilter("CSV-file","*.csv");
                fileChooser.setTitle("Save SoFi import file");

                fileChooser.getExtensionFilters().addAll(ef1);
                file = fileChooser.showSaveDialog(new Stage());
                if (file != null) {
                    new Thread(){

                        @Override
                        public void run(){
                            Aggregator agg = new Aggregator();
                            List<LineItem> dis = agg.getAggregatedResults(drs,tid.getDataKey());
                            ConnectionBuilder.prepareImportFiles(dis, file);
                        }
                    }.start();
                }
            }
            
        }
        
        
    }

    @FXML
    private void saveData(ActionEvent event) {
        DataRowService drService = DataRowService.getINSTANCE();
        drService.addOrEditDataRows(drs);
    }

    @FXML
    private void exportDocuFile(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getSelectionModel().getSelectedItem();
        if (lsp != null) {
            File file;
            FileChooser fileChooser = new FileChooser();
            ExtensionFilter ef1 = new FileChooser.ExtensionFilter("tab stop separated text","*.txt");
            fileChooser.setTitle("Save Documentation file");

            fileChooser.getExtensionFilters().addAll(ef1);
            file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                new Thread(){

                    @Override
                    public void run(){
                        DataKeyService dkService = DataKeyService.getINSTANCE();
                        List<DataKey> dks = dkService.findDataKeysByLsp(lsp);
                        DocumentationFileWriter.writeFile(drs, file,dks);
                    }
                }.start();
            }
        }
    }

    @FXML
    private void mapTourIdentifier(ActionEvent event) {
        LogisticServiceProvider lsp = cbLsp.getSelectionModel().getSelectedItem();
        
        if (lsp != null) {   
        
        
            try {
                URL location = getClass().getResource("/com/thinkstep/jde/ui/MapTourIdentifier/TourIdentifier.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(location);
                fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());
                Parent root = (Parent) fxmlLoader.load(location.openStream());
                TourIdentifierController tidc = (TourIdentifierController) fxmlLoader.getController();
                tidc.setLsp(lsp);
                
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setAlwaysOnTop(true);
                stage.show();
            } catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        
    }



    
    
}
