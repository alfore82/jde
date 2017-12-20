/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder.FuelTypeAction;

import com.thinkstep.jde.persistence.entities.Rules.AssignFuelTypeAction;
import com.thinkstep.jde.persistence.entities.Rules.FieldName;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionProcess;
import com.thinkstep.jde.persistence.entities.emissioncalculation.Parameter;
import com.thinkstep.jde.persistence.services.EmissionProcessService;
import com.thinkstep.jde.ui.Converters.FieldNameStringConverter;
import com.thinkstep.jde.ui.Converters.FuelTypeStringConverter;
import java.net.URL;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author forell
 */
public class FuelTypeActionController implements Initializable {

    @FXML
    private ComboBox<EmissionProcess> cbFuelType;
    @FXML
    private TableColumn<Parameter, String> colParameter;
    @FXML
    private TableColumn<Parameter, String> colValue;
    @FXML
    private TableColumn<Parameter, Boolean> colBalance;
    @FXML
    private TableView<Parameter> tableParams;

    private ObservableList<Parameter> parameters = FXCollections.observableArrayList();
    private AssignFuelTypeAction action;
    private RuleBulderFuelTypeAction rb;
    @FXML
    private ComboBox<FieldName> cbField;
    
    private ObservableList<FieldName> fields = FXCollections.observableArrayList();
    private FieldName field;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbField.setItems(fields);
        cbField.setConverter(new FieldNameStringConverter());
        cbFuelType.getItems().addAll(EmissionProcessService.getINSTANCE().findAllEmissionProcesses());
        cbFuelType.setConverter(new FuelTypeStringConverter());
        cbFuelType.setOnAction(ev->{
            EmissionProcess ep = cbFuelType.getValue();
            if (ep != null){
                createParameterMatrix(ep);
            }
        });
        initializeTable();
    }    

    @FXML
    private void btnClose(ActionEvent event) {
        if (createEmissionFactorInstance()){
            rb.setFuelTypeAction(action);
            Stage stage = (Stage) this.cbFuelType.getScene().getWindow();
            stage.close();
        }
        
    }

    private void createParameterMatrix(EmissionProcess ep) {
        parameters.clear();
        ep.unserialize();
        for (Parameter p:ep.getParams()){
            Parameter par = new Parameter();
            par.setName(p.getName());
            par.setValue(p.getValue());
            par.setBalance(p.isBalance());
            parameters.add(par);
        }
    
    }
    
    private void initializeTable(){
        tableParams.setItems(parameters);
        tableParams.setEditable(true);
        colParameter.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parameter, String>,ObservableValue<String>>(){
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Parameter, String> p) {
                SimpleStringProperty s = new SimpleStringProperty();
                s.setValue(p.getValue().getName());
                return s;
            }
        
        });
        colParameter.setCellFactory(TextFieldTableCell.forTableColumn());
        colParameter.setEditable(false);
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
        
        
        colBalance.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Parameter, Boolean>, ObservableValue<Boolean>>() {
 
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<Parameter, Boolean> param) {
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
                            parameters.forEach(par->{
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
        colBalance.setEditable(false);
    }
    
    private void reCalculate(){
        if (parameters.size()==1){
            parameters.get(0).setValue(1.0);
        } else {
            List<Parameter> pars = new ArrayList<>();
            Parameter basicParameter = null;
            for (Parameter par:parameters){
                if (par.isBalance()){
                    basicParameter = par;
                }
            }
            pars.addAll(parameters);
            pars.remove(basicParameter);
            double sum = 0.0;
            for (Parameter par:pars){
                sum = sum + par.getValue();
            }
            basicParameter.setValue(1-sum);
        }
    }

    private boolean createEmissionFactorInstance() {
        EmissionProcess ep = cbFuelType.getValue();
        field = cbField.getSelectionModel().getSelectedItem();
        if (ep != null && field != null) {
            action = new AssignFuelTypeAction();
            action.setEmissionProcess(ep);
            action.setParameters(parameters);
            action.setName(field);
            action.serialize();
            return true;
        } else {
            action = null;
            return false;
        }
    }
    
    public void setAction(AssignFuelTypeAction action){
        this.action = action;
        createFromAction();
        this.cbField.getSelectionModel().select(action.getName());
    }
    
    public void setFieldNames(FieldName... fieldNames){
        fields.clear();
        fields.addAll(fieldNames);
         if (fields.size() > 0) cbField.getSelectionModel().select(0);
    }
    
    public  void setRuleBulderFuelTypeAction(RuleBulderFuelTypeAction rb){
        this.rb = rb;
    }

    private void createFromAction() {
        if (action.getEmissionProcess() != null){
            this.cbFuelType.setValue(action.getEmissionProcess());
            action.unserialize();
            this.parameters.clear();
            this.parameters.addAll(action.getParameters());
        }
        
        
        
    }
    
}
