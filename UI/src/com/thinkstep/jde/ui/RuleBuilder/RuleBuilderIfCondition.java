/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;

import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import com.thinkstep.jde.persistence.entities.Rules.ParentConnector;
import com.thinkstep.jde.persistence.entities.Rules.Rule;
import com.thinkstep.jde.persistence.entities.Rules.RuleType;
import com.thinkstep.jde.persistence.entities.graphics.ItemType;
import com.thinkstep.jde.persistence.entities.graphics.MappingItemGraphics;
import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.ui.Converters.DataKeyStringConverter;
import com.thinkstep.jde.ui.Converters.RuleTypeStringConverter;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.scene.text.TextAlignment;

/**
 *
 * @author forell
 */
public class RuleBuilderIfCondition extends Group implements IfConditionInterface{
    private SimpleDoubleProperty width = new SimpleDoubleProperty(500.0);
    private SimpleDoubleProperty height = new SimpleDoubleProperty(60.0);
    
    private double orgSceneX, orgSceneY;
    
    
    private final Group GROUP; 
    private final Connector C; 
    private final Plane PLANE;
    private final IfConditionInterface PARENT;
    private final ParentConnector PARENTCONNECTOR;
    private ComboBox<DataKey> cbParam;
    TextField choice;
    private ComboBox<RuleType> cond;
    private MappingItemGraphics graphics;
    private Rule rule;
    private LogisticServiceProvider lsp;
    List<DataKey> dataKeys;
    private MappingType mappingType;
    
    
    RuleBuilderIfCondition(Plane plane, Dock dockStart, Dock dockEnd, IfConditionInterface parent, ParentConnector parentConnector, LogisticServiceProvider lsp, MappingType mappingType, List<DataKey> dataKeys){
        this.lsp = lsp;
        this.dataKeys = dataKeys;
        this.mappingType = mappingType;
        this.PARENTCONNECTOR = parentConnector;
        
        graphics = new MappingItemGraphics();
        graphics.setLsp(lsp);
        graphics.setMappingType(mappingType);
        graphics.setItemType(ItemType.IFCONDITION);
        
        this.PLANE = plane;
        this.GROUP = new Group();
        this.C = new Connector(dockStart, dockEnd);
        this.PARENT = parent;
        this.getChildren().add(C);
        this.getChildren().add(GROUP);
        GROUP.setLayoutX(dockEnd.xProperty.getValue()-width.getValue()/2);
        GROUP.setLayoutY(dockEnd.yProperty.getValue());
        dockEnd.xProperty.bind(GROUP.layoutXProperty().add(width.divide(2)));
        dockEnd.yProperty.bind(GROUP.layoutYProperty());
        
        
        
        
        GROUP.setOnMousePressed(t->{
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });
        
        GROUP.setOnMouseDragged((t) -> {

            double offsetX = t.getSceneX() - orgSceneX;
            double offsetY = t.getSceneY() - orgSceneY;
            GROUP.setLayoutX(GROUP.getLayoutX()+offsetX);
            GROUP.setLayoutY(GROUP.getLayoutY()+offsetY);
            orgSceneX = t.getSceneX();
            orgSceneY = t.getSceneY();
        });
        initComponents();
    }
    
    private void initComponents(){
        GROUP.getChildren().add(drawTopBox());
        GROUP.getChildren().add(drawLeftBox());
        GROUP.getChildren().add(drawRightBox());
        GROUP.getChildren().add(drawConditionBox());
        GROUP.getChildren().add(drawLeftLabel());
        GROUP.getChildren().add(drawRightLabel());
    }
    
    private Path drawTopBox(){
        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(0.0f);
        moveTo.setY(0.0f);
        
        HLineTo hLineTo1 = new HLineTo();
        hLineTo1.xProperty().bind(width);
        
        VLineTo vLineTo1 = new VLineTo();
        vLineTo1.yProperty().bind(height.divide(2));
        
        LineTo lineTo1 = new LineTo();
        lineTo1.yProperty().bind(height);
        lineTo1.xProperty().bind(width.divide(2));
        
        LineTo lineTo2 = new LineTo();
        lineTo2.yProperty().bind(height.divide(2));
        lineTo2.setX(0.0f);
        
        VLineTo vlineTo2 = new VLineTo();
        vlineTo2.setY(0.0f);
        
        path.getElements().add(moveTo);
        path.getElements().add(hLineTo1);
        path.getElements().add(vLineTo1);
        path.getElements().add(lineTo1);
        path.getElements().add(lineTo2);
        path.getElements().add(vlineTo2);
        path.setFill(Color.web("0xEEEEEE",1.0));
        path.setStroke(null);
        path.setCursor(Cursor.MOVE);
        return path;
    }
    
    private Path drawLeftBox(){
        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(0.0f);
        moveTo.yProperty().bind(height.divide(2));
        VLineTo vLineTo1 = new VLineTo();
        vLineTo1.yProperty().bind(height);
        HLineTo hLineTo1 = new HLineTo();
        hLineTo1.xProperty().bind(width.divide(2));
        
        path.getElements().add(moveTo);
        path.getElements().add(vLineTo1);
        path.getElements().add(hLineTo1);
        
        path.setFill(Color.LIGHTBLUE);
        path.setStroke(null);
        return path;
    }
    
    private Path drawRightBox(){
        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.xProperty().bind(width.divide(2));
        moveTo.yProperty().bind(height);
        HLineTo hLineTo1 = new HLineTo();
        hLineTo1.xProperty().bind(width);
        VLineTo vLineTo1 = new VLineTo();
        vLineTo1.yProperty().bind(height.divide(2));
        
        
        path.getElements().add(moveTo);
        path.getElements().add(hLineTo1);
        path.getElements().add(vLineTo1);
        
        path.setFill(Color.LIGHTBLUE);
        path.setStroke(null);
        return path;
    }
    
    private HBox drawConditionBox(){
        HBox hBoxTop = new HBox();
        hBoxTop.minWidthProperty().bind(width);
        hBoxTop.maxWidthProperty().bind(width);
        Pane leftBorder = new Pane();
        HBox.setHgrow(leftBorder, Priority.ALWAYS);
        Pane rightBorder = new Pane();
        HBox.setHgrow(rightBorder, Priority.ALWAYS);
        Label label = new Label("if");
        HBox.setHgrow(label, Priority.NEVER);
        HBox.setMargin(label, new Insets(5,5,5,5));
        this.cbParam = new ComboBox<>();
        cbParam.minWidthProperty().bind(width.multiply(0.3));
        cbParam.prefWidthProperty().bind(width.multiply(0.3));
        cbParam.maxWidthProperty().bind(width.multiply(0.4));
        cbParam.getItems().addAll(dataKeys);
        cbParam.setConverter(new DataKeyStringConverter());
        
        HBox.setHgrow(cbParam, Priority.NEVER);
        HBox.setMargin(cbParam, new Insets(5,5,5,5));
        cond = new ComboBox();
        cond.minWidthProperty().bind(width.multiply(0.25));
        cond.prefWidthProperty().bind(width.multiply(0.25));
        cond.maxWidthProperty().bind(width.multiply(0.25));
        cond.setConverter(new RuleTypeStringConverter());
        cond.setItems(getComparators());
        cbParam.setOnAction(ev->{
            DataKey key = cbParam.getValue();
            
        });
        HBox.setHgrow(cond, Priority.NEVER);
        HBox.setMargin(cond, new Insets(5,5,5,5));
        choice = new TextField();
        choice.minWidthProperty().bind(width.multiply(0.30));
        choice.prefWidthProperty().bind(width.multiply(0.30));
        choice.maxWidthProperty().bind(width.multiply(0.35));
        ContextMenu m = new ContextMenu();
        MenuItem mi = new MenuItem("Delete");
        mi.setOnAction(ev->{
            PLANE.remove(this);
        
        });
        m.getItems().add(mi);
        label.setContextMenu(m);
        HBox.setHgrow(choice, Priority.NEVER);
        HBox.setMargin(choice, new Insets(5,5,5,5));
        hBoxTop.getChildren().addAll(leftBorder, label, cbParam, cond, choice, rightBorder);
        return hBoxTop;
    }
    
    private Label drawLeftLabel(){
        Label l = new Label("Then");
        l.layoutYProperty().bind(height.subtract(20));
        l.setLayoutX(10);
        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Add Condition");
        mi1.setOnAction(ev->{
            DoubleProperty x = getOutConnectorThenX();
            DoubleProperty y = getOutConnectorThenY();
            
            Dock dock = new Dock();
            dock.xProperty.bind(getOutConnectorThenX());
            dock.yProperty.bind(getOutConnectorThenY());
            
            PLANE.addCondition(dock, this, ParentConnector.THEN);
        });
        MenuItem mi2 = new MenuItem("Add Action");
        mi2.setOnAction(ev->{
            DoubleProperty x = getOutConnectorThenX();
            DoubleProperty y = getOutConnectorThenY();
            
            Dock dock = new Dock();
            dock.xProperty.bind(getOutConnectorThenX());
            dock.yProperty.bind(getOutConnectorThenY());
            
            PLANE.addAction(dock, this, ParentConnector.THEN);
        
        });
        cm.getItems().addAll(mi1, mi2);
        l.setContextMenu(cm);
        return l;
    }
    
    private Label drawRightLabel(){
        Label l = new Label("Else");
        FontLoader fontLoader = Toolkit.getToolkit().getFontLoader();
        l.layoutYProperty().bind(height.subtract(20));
        double textlength = fontLoader.computeStringWidth(l.getText(), l.getFont());
        l.layoutXProperty().bind(width.subtract(10).subtract(textlength));
        l.setTextAlignment(TextAlignment.RIGHT);
        ContextMenu cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Add Condition");
        mi1.setOnAction(ev->{
            DoubleProperty x = getOutConnectorElseX();
            DoubleProperty y = getOutConnectorElseY();
            
            Dock dock = new Dock();
            dock.xProperty.bind(getOutConnectorElseX());
            dock.yProperty.bind(getOutConnectorElseY());
            
            PLANE.addCondition(dock, this, ParentConnector.ELSE);
            
        });
        MenuItem mi2 = new MenuItem("Add Action");
        mi2.setOnAction(ev->{
            DoubleProperty x = getOutConnectorElseX();
            DoubleProperty y = getOutConnectorElseY();
            
            Dock dock = new Dock();
            dock.xProperty.bind(getOutConnectorElseX());
            dock.yProperty.bind(getOutConnectorElseY());
            
            PLANE.addAction(dock, this, ParentConnector.ELSE);
        
        });
        cm.getItems().addAll(mi1, mi2);
        l.setContextMenu(cm);
        return l;
    }
    
    private ObservableList<RuleType> getComparators(){
        ObservableList<RuleType> result = FXCollections.observableArrayList();

        result.addAll(RuleType.BEGINSWITH,RuleType.ENDSWITH, RuleType.CONTAINS, RuleType.EQUALS, 
                RuleType.LARGER,RuleType.LARGEREQUAL, RuleType.IDENTITY, RuleType.SMALLEREQUAL, 
                RuleType.SMALLER, RuleType.NUMBEROFPALLETSPERTOURLARGER,
                RuleType.NUMBEROFPALLETSPERTOURLARGEREQUAL, RuleType.NUMBEROFPALLETSPERTOURSMALLEREQUAL,
                RuleType.NUMBEROFPALLETSPERTOURSMALLER, RuleType.PALLETUTILIZATIONLARGER, 
                RuleType.PALLETUTILIZATIONLARGEREQUAL, RuleType.PALLETUTILIZATIONSMALLEREQUAL,
                RuleType.PALLETUTILIZATIONSMALLER);

        return result;
    }
    
    public DoubleProperty getInConnectorX(){
        SimpleDoubleProperty s = new SimpleDoubleProperty();
        s.bind(GROUP.layoutXProperty().add(width.divide(2)));
        return s;
    }
    
    public DoubleProperty getInConnectorY(){
        SimpleDoubleProperty s = new SimpleDoubleProperty(0);
        s.bind(GROUP.layoutYProperty());
        return s;
    }
    
    public DoubleProperty getOutConnectorThenX(){
        SimpleDoubleProperty s = new SimpleDoubleProperty();
        s.bind(GROUP.layoutXProperty().add(20));
        return s;
    }
    
    public DoubleProperty getOutConnectorThenY(){
        SimpleDoubleProperty s = new SimpleDoubleProperty();
        s.bind(GROUP.layoutYProperty().add(height));
        return s;
    }
    
    public DoubleProperty getOutConnectorElseX(){
        SimpleDoubleProperty s = new SimpleDoubleProperty();
        s.bind(GROUP.layoutXProperty().add(width).subtract(20));
        return s;
    }
    
    public DoubleProperty getOutConnectorElseY(){
        SimpleDoubleProperty s = new SimpleDoubleProperty();
        s.bind(GROUP.layoutYProperty().add(height));
        return s;
    }

    @Override
    public void setMappingItemGraphics(MappingItemGraphics item) {
        this.graphics = item;
    }

    @Override
    public MappingItemGraphics getMappingItemGraphics() {
        graphics.setLayoutX(GROUP.getLayoutX());
        graphics.setLayoutY(GROUP.getLayoutY());
        graphics.setDockLayoutX(this.C.getDockStart().xProperty.getValue());
        graphics.setDockLayoutY(this.C.getDockStart().yProperty.getValue());
        if (PARENT != null) graphics.setMappingItemGraphics(PARENT.getMappingItemGraphics());
        return graphics;
    }

    @Override
    public Group getGroup() {
        return this;
    }

    @Override
    public final MappingItem getParentItem() {
        return PARENT;
    }

    @Override
    public Dock getDockStart(Dock dockStart) {
        
        Dock dockThen = new Dock();
        dockThen.xProperty.bind(getOutConnectorThenX());
        dockThen.yProperty.bind(getOutConnectorThenY());
        Dock dockElse = new Dock();
        dockElse.xProperty.bind(getOutConnectorElseX());
        dockElse.yProperty.bind(getOutConnectorElseY());
        if (dockStart.equals(dockThen)){
            return dockThen;
        }
        
        if (dockStart.equals(dockElse)){
            return dockElse;
        }
        return null;
    }

    @Override
    public void shift(Dock dockEnd) {
        GROUP.setLayoutX(dockEnd.xProperty.getValue());
        GROUP.setLayoutY(dockEnd.yProperty.getValue());
    }
    

    @Override
    public Rule getRule() {
        
        if (this.rule == null) {
            this.rule = new Rule();
        }
        DataKey key = cbParam.getValue();
        rule.setKey(key);
        rule.setRuleType(cond.getValue());
        
        switch (rule.getRuleType()){
            case LARGER:
            case LARGEREQUAL:
            case IDENTITY:
            case SMALLEREQUAL:
            case SMALLER:
            case NUMBEROFPALLETSPERTOURLARGER:
            case NUMBEROFPALLETSPERTOURLARGEREQUAL:
                
            case NUMBEROFPALLETSPERTOURSMALLEREQUAL:
            case NUMBEROFPALLETSPERTOURSMALLER:
            case PALLETUTILIZATIONLARGER:
            case PALLETUTILIZATIONLARGEREQUAL:
            case PALLETUTILIZATIONSMALLEREQUAL:
            case PALLETUTILIZATIONSMALLER:
                                                           
                NumberFormat nf = NumberFormat.getInstance();
                {
            try {
                rule.setValueDouble(nf.parse(choice.getText()).doubleValue());
            } catch (ParseException ex) {
                Alert al = new Alert(AlertType.WARNING);
                al.setHeaderText("Not possible to parse entry as Number. The Value will be changed to 0");
                choice.setText(nf.format(0.0));
                rule.setValueDouble(0.0);
            }
        }
            case BEGINSWITH:     
            case ENDSWITH:
            case CONTAINS:
            case EQUALS:
                rule.setValueString(choice.getText());
                break;
        }
        rule.setLsp(lsp);
        rule.setMappingType(mappingType);
        if (PARENT != null) {
            rule.setParent(PARENT.getRule());
        } else {
            rule.setParent(null);
        }
        rule.setParentConnector(PARENTCONNECTOR);
        return rule;
        
        
    }

    @Override
    public void setRule(Rule r) {
        this.rule = r;
        if (cbParam.getItems().contains(r.getKey())){
            cbParam.setValue(rule.getKey());
        } else {
            cbParam.getItems().add(rule.getKey());
            cbParam.setValue(rule.getKey());
        }
        RuleType ruleType = rule.getRuleType();
        
        cond.setValue(ruleType);
        
        switch (rule.getRuleType()){
            case LARGER:
            case LARGEREQUAL:
            case IDENTITY:
            case SMALLEREQUAL:
            case SMALLER:
            case NUMBEROFPALLETSPERTOURLARGER:
            case NUMBEROFPALLETSPERTOURLARGEREQUAL:
                
            case NUMBEROFPALLETSPERTOURSMALLEREQUAL:
            case NUMBEROFPALLETSPERTOURSMALLER:
            case PALLETUTILIZATIONLARGER:
            case PALLETUTILIZATIONLARGEREQUAL:
            case PALLETUTILIZATIONSMALLEREQUAL:
            case PALLETUTILIZATIONSMALLER:
                
                
                
                NumberFormat nf = NumberFormat.getInstance();
                choice.setText(nf.format(rule.getValueDouble()));
            case BEGINSWITH:     
            case ENDSWITH:
            case CONTAINS:
            case EQUALS:
                 choice.setText(rule.getValueString());
                break;
        }
    }

}
