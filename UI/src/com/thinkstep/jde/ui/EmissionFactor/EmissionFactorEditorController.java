/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.EmissionFactor;

import com.thinkstep.jde.ui.Converters.EmissionStageStringConverter;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionFactor;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionStage;
import com.thinkstep.jde.persistence.services.EmissionFactorService;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class EmissionFactorEditorController implements Initializable {

    @FXML
    private AnchorPane ap;
    @FXML
    private TableView<EmissionFactor> tblEmissionFactors;
    @FXML
    private TableColumn<EmissionFactor, String> colName;
    @FXML
    private TableColumn<EmissionFactor, String> colUuid;
    @FXML
    private TableColumn<EmissionFactor, EmissionStage> colStage;
    @FXML
    private TableColumn<EmissionFactor, String> colAmount;
    
    private ObservableList<EmissionFactor> emFactors = FXCollections.observableArrayList();
    ObservableList<EmissionStage> emStages = FXCollections.observableArrayList();
    
    private EmissionFactorService ems = EmissionFactorService.getINSTANCE();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        emFactors.addAll(ems.findAllEmissionFactor());
        emStages.add(EmissionStage.TANKTOWHEEL);
        emStages.add(EmissionStage.WELLTOTANK);
        tblEmissionFactors.setItems(emFactors);
        tblEmissionFactors.setEditable(true);
        tblEmissionFactors.setRowFactory(tv -> {
            TableRow<EmissionFactor> row = new TableRow<>();
            row.setOnDragDetected(event ->{
                if(!row.isEmpty()){
                    EmissionFactor ei = row.getItem();
                    Dragboard db = row.startDragAndDrop(TransferMode.COPY);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.putString("EmissionFactor:"+ei.getId());
                    db.setContent(cc);
                    event.consume();
                }
            
            });
            
            
            return row;
        
        });
        colName.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EmissionFactor, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EmissionFactor, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.setValue(p.getValue().getName());
                return s;
            }
        
        });
        colName.setCellFactory(TextFieldTableCell.forTableColumn());
        colName.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<EmissionFactor, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<EmissionFactor, String> t){
                EmissionFactor c = ((EmissionFactor) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                c.setName(t.getNewValue());
                ems.addOrEditEmissionFactor(c);
            }
        });
        colUuid.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EmissionFactor, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EmissionFactor, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.setValue(p.getValue().getUuid());
                return s;
            }
        
        });
        colUuid.setCellFactory(TextFieldTableCell.forTableColumn());
        colUuid.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<EmissionFactor, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<EmissionFactor, String> t){
                EmissionFactor c = ((EmissionFactor) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                c.setName(t.getNewValue());
                ems.addOrEditEmissionFactor(c);
            }
        });
        
        colStage.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EmissionFactor, EmissionStage>,ObservableValue<EmissionStage>>(){
            @Override
            public ObservableValue<EmissionStage> call(TableColumn.CellDataFeatures<EmissionFactor, EmissionStage> p) {
                SimpleObjectProperty<EmissionStage> esp = new SimpleObjectProperty<>();
                esp.setValue(p.getValue().getEmissionStage());                
                return esp;
            }
        
        });
        colStage.setCellFactory(ComboBoxTableCell.forTableColumn(new EmissionStageStringConverter(), emStages));
        colStage.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<EmissionFactor, EmissionStage>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<EmissionFactor, EmissionStage> t){
                    EmissionFactor ef = ((EmissionFactor) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    ef.setEmissionStage(t.getNewValue());
                    
                    ems.addOrEditEmissionFactor(ef);
                }
            });
        
        colAmount.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EmissionFactor, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EmissionFactor, String> p) {
                NumberFormat nf = NumberFormat.getInstance();
                SimpleStringProperty s = new SimpleStringProperty();
                s.setValue(nf.format(p.getValue().getGHGEmissions()));
                return s;
            }
        
        });
        colAmount.setCellFactory(TextFieldTableCell.forTableColumn());
        colAmount.setOnEditCommit(
            new EventHandler<TableColumn.CellEditEvent<EmissionFactor, String>>() {
                @Override
                public void handle(TableColumn.CellEditEvent<EmissionFactor, String> t){
                    EmissionFactor ef = ((EmissionFactor) t.getTableView().getItems().get(
                            t.getTablePosition().getRow()));
                    try {
                        NumberFormat nf = NumberFormat.getInstance();
                        Number value = nf.parse(t.getNewValue());
                        ef.setGHGEmissions(value.doubleValue());
                        ems.addOrEditEmissionFactor(ef);
                    } catch (Exception ex) {
                        ef.setGHGEmissions(0.0);
                    }
                    
                }
            });
        
        
    }    

    @FXML
    private void addEmissionFactor(ActionEvent event) {
        EmissionFactor ef = new EmissionFactor();
        ef.setName("new");
        ems.addOrEditEmissionFactor(ef);
        emFactors.add(ef);
    }

    @FXML
    private void deleteEmissionFactor(ActionEvent event) {
        EmissionFactor ef = tblEmissionFactors.getSelectionModel().getSelectedItem();
        ems.deleteEmissionFactor(ef);
        emFactors.remove(ef);
    }
    
}