/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.ProcessEditor;

import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionFactor;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionFactorInstance;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionProcess;
import com.thinkstep.jde.persistence.entities.emissioncalculation.Parameter;
import com.thinkstep.jde.persistence.services.CountryService;
import com.thinkstep.jde.persistence.services.DataRowService;
import com.thinkstep.jde.persistence.services.EmissionFactorService;
import com.thinkstep.jde.persistence.services.EmissionProcessService;
import com.thinkstep.jde.ui.Converters.CountryStringConverter;
import com.thinkstep.jde.ui.Converters.ParameterStringConverter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class ProcessEditorController implements Initializable {

    @FXML
    private TextField tfName;
    @FXML
    private ComboBox<Country> cbCountry;
    @FXML
    private TableView<Parameter> tableParams;
    @FXML
    private TableColumn<Parameter, String> colParam;
    @FXML
    private TableColumn<Parameter, String> colValue;
    @FXML
    private TableColumn<Parameter, Boolean> colBalance;
    @FXML
    private TableView<EmissionFactorInstance> tableEmissionFactors;
    @FXML
    private TableColumn<EmissionFactorInstance, Parameter> colParamDropDown;
    @FXML
    private TableColumn<EmissionFactorInstance, String> colScalingFactor;
    @FXML
    private TableColumn<EmissionFactorInstance, String> colEmissionFactor;

    
    private EmissionProcess process;
    private ObservableList<Parameter> params = FXCollections.observableArrayList();
    private ObservableList<EmissionFactorInstance> efis  = FXCollections.observableArrayList();
    private ObservableList<Country> countries = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeComboboxCountry();
        initializeTableParameters();
        initializeTableEmissionFactors();
    }
    
    public void setProcess(EmissionProcess p){
        this.process = p;
        p.unserialize();
        this.tfName.setText(p.getName());
        this.params.clear();
        this.params.addAll(p.getParams());
        this.efis.clear();
        this.efis.addAll(p.getEmissionFactorInstances());
        this.tableParams.refresh();
        this.tableEmissionFactors.refresh();
    }
    
    private void initializeTableParameters(){
        tableParams.setItems(params);
        tableParams.setEditable(true);
        colParam.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parameter, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Parameter, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.setValue(p.getValue().getName());
                return s;
            }
        
        });
        colParam.setCellFactory(TextFieldTableCell.forTableColumn());
        colParam.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Parameter, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Parameter, String> t){
                Parameter c = ((Parameter) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                c.setName(t.getNewValue());
            }
        });
        colValue.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parameter, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Parameter, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = NumberFormat.getPercentInstance();
                s.setValue(nf.format(p.getValue().getValue()));
                return s;
            }
        
        });
        colValue.setCellFactory(TextFieldTableCell.forTableColumn());
        colValue.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Parameter, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Parameter, String> t){
                Parameter c = ((Parameter) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                NumberFormat nf = NumberFormat.getPercentInstance();
                
                try {
                    String newValue = t.getNewValue();
                    if (newValue.endsWith("%")){
                        c.setValue((double) nf.parse(newValue));
                        reCalculate();
                        tableParams.refresh();
                    } else {
                        c.setValue((double) nf.parse(newValue + "%"));
                        reCalculate();
                        tableParams.refresh();
                    }
                    
                } catch (ParseException ex) {
                    c.setValue(0);
                    reCalculate();
                    tableParams.refresh();
                }
            }
        });
        
        
        colBalance.setCellValueFactory(new Callback<CellDataFeatures<Parameter, Boolean>, ObservableValue<Boolean>>() {
 
            @Override
            public ObservableValue<Boolean> call(CellDataFeatures<Parameter, Boolean> param) {
                Parameter p = param.getValue();
 
                SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(p.isBalance());
 
                // Note: singleCol.setOnEditCommit(): Not work for
                // CheckBoxTableCell.
 
                // When "Single?" column change.
                booleanProp.addListener(new ChangeListener<Boolean>() {
 
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
                            Boolean newValue) {
                        if (newValue == true){
                            params.forEach(par->{
                            par.setBalance(false);
                            });
                            p.setBalance(newValue);
                            reCalculate();
                        }
                        tableParams.refresh();
                    }
                });
                return booleanProp;
            }
        });
 
        colBalance.setCellFactory(new Callback<TableColumn<Parameter, Boolean>, TableCell<Parameter, Boolean>>() {
            @Override
            public TableCell<Parameter, Boolean> call(TableColumn<Parameter, Boolean> p) {
                CheckBoxTableCell<Parameter, Boolean> cell = new CheckBoxTableCell<Parameter, Boolean>();
                cell.setAlignment(Pos.CENTER);
                return cell;
            }
        });
    }
    
    private void initializeTableEmissionFactors(){
        tableEmissionFactors.setItems(efis);
        tableEmissionFactors.setEditable(true);
        tableEmissionFactors.setRowFactory(tv -> {
            TableRow<EmissionFactorInstance> row = new TableRow<>();
            row.setOnDragOver(event ->{
                if(!row.isEmpty()){
                    Dragboard db = event.getDragboard();
                    if (db.getString().startsWith("EmissionFactor")){
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                    }
                }
                event.consume();
            });
            row.setOnDragDropped(event ->{
                if(!row.isEmpty()){
                    Dragboard db = event.getDragboard();
                    if (db.getString().startsWith("EmissionFactor:")){
                        String ids = db.getString();
                        ids = ids.replace("EmissionFactor:", "");
                        long id = Long.parseLong(ids);
                        EmissionFactorService efs = EmissionFactorService.getINSTANCE();
                        EmissionFactor ef = efs.findEmissionFactor(id);
                        EmissionFactorInstance efi = row.getItem();
                        if (efi != null){
                            efi.setEmissionFactor(ef);
                        }
                    }
                    tableEmissionFactors.refresh();
                    event.setDropCompleted(true);
                    event.consume();
                }
            
            });
            return row;
        
        });
        colParamDropDown.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EmissionFactorInstance, Parameter>,ObservableValue<Parameter>>(){
            @Override
            public ObservableValue<Parameter> call(TableColumn.CellDataFeatures<EmissionFactorInstance, Parameter> p) {
                ObservableValue<Parameter> ov = new ReadOnlyObjectWrapper<>(p.getValue().getParameter());
                return ov;
            }
        
        });
        
        colParamDropDown.setCellFactory(ComboBoxTableCell.forTableColumn(new ParameterStringConverter(),params));
        colParamDropDown.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<EmissionFactorInstance, Parameter>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<EmissionFactorInstance, Parameter> t){
                EmissionFactorInstance efi = ((EmissionFactorInstance) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                efi.setParameter(t.getNewValue());
            }
        });
        
        colScalingFactor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EmissionFactorInstance, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EmissionFactorInstance, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                NumberFormat nf = DecimalFormat.getInstance();
                s.setValue(nf.format(p.getValue().getScalingFactor()));
                return s;
            }
        
        });
        colScalingFactor.setCellFactory(TextFieldTableCell.forTableColumn());
        colScalingFactor.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<EmissionFactorInstance, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<EmissionFactorInstance, String> t){
                EmissionFactorInstance c = ((EmissionFactorInstance) t.getTableView().getItems().get(
                        t.getTablePosition().getRow()));
                NumberFormat nf = DecimalFormat.getInstance();
                
                try {
                    String newValue = t.getNewValue();
                    c.setScalingFactor(nf.parse(newValue).doubleValue());
                    tableEmissionFactors.refresh();
                } catch (ParseException ex) {
                    c.setScalingFactor(0);
                    tableEmissionFactors.refresh();
                }
            }
        });
        colEmissionFactor.setEditable(false);
        colEmissionFactor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EmissionFactorInstance, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<EmissionFactorInstance, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                if (p.getValue().getEmissionFactor()!= null){
                    s.setValue(p.getValue().getEmissionFactor().getName());
                } else {
                    s.setValue("");
                }
                return s;
            }
        });
        
        
    }
    
    private void reCalculate(){
        if (params.size()==1){
            params.get(0).setValue(1.0);
        } else {
            List<Parameter> pars = new ArrayList<>();
            Parameter basicParameter = null;
            for (Parameter par:params){
                if (par.isBalance()){
                    basicParameter = par;
                }
            }
            pars.addAll(params);
            pars.remove(basicParameter);
            double sum = 0.0;
            for (Parameter par:pars){
                sum = sum + par.getValue();
            }
            basicParameter.setValue(1-sum);
        }
    }
    
    private void initializeComboboxCountry(){
        CountryService cs = CountryService.getINSTANCE();
        this.countries.clear();
        this.countries.addAll(cs.findAllCountries());
        this.cbCountry.setItems(countries);
        this.cbCountry.setConverter(new CountryStringConverter());
    }
    
    @FXML
    private void addParameter(ActionEvent ae){
        params.add(new Parameter());
        if (params.size() == 1){
            params.get(0).setBalance(true);
            reCalculate();
            tableParams.refresh();
        }
    }
    
    @FXML
    private void deleteParameter(ActionEvent ae){
        Parameter p = tableParams.getSelectionModel().getSelectedItem();
        if (p != null){
            params.remove(p);
            if (p.isBalance()){
                if (params.size()>0){
                    Parameter pbal = params.get(0);
                    pbal.setValue(0.0);
                    pbal.setBalance(true);
                }
            }
            reCalculate();
            tableParams.refresh();
        }
    }
    
    @FXML
    private void addLine(ActionEvent ae){
        EmissionFactorInstance efi = new EmissionFactorInstance();
        efis.add(efi);
        tableEmissionFactors.refresh();
    }
    
    @FXML
    private void deleteLine(ActionEvent ae){
        EmissionFactorInstance efi = tableEmissionFactors.getSelectionModel().getSelectedItem();
        if (efi != null){
            efis.remove(efi);
        }
        tableEmissionFactors.refresh();
    }
    

    @FXML
    private void save(ActionEvent event) {
        this.process.setName(tfName.getText());
        this.process.setEmissionFactorInstances(efis);
        this.process.setParams(params);
        this.process.setCountry(cbCountry.getSelectionModel().getSelectedItem());
        this.process.serialize();
        EmissionProcessService eps = EmissionProcessService.getINSTANCE();
        eps.addOrEditEmissionProcess(process);
        eps.refresh();
    }
    
}
