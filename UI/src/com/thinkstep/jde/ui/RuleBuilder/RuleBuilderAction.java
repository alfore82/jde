/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;

import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ActionType;
import com.thinkstep.jde.persistence.entities.Rules.ParentConnector;
import com.thinkstep.jde.persistence.entities.graphics.ItemType;
import com.thinkstep.jde.persistence.entities.graphics.MappingItemGraphics;
import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.paint.Color;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.openide.util.Exceptions;

/**
 *
 * @author forell
 */
public class RuleBuilderAction extends Group implements ActionInterface{

    private SimpleDoubleProperty width = new SimpleDoubleProperty(500.0);
    private SimpleDoubleProperty height = new SimpleDoubleProperty(60.0);
    private SimpleStringProperty actionDisplay = new SimpleStringProperty("Action");
    
    private final ObservableList<Class<? extends RuleBuilderActionType>> actionTypes;
    private RuleBuilderActionType actionType;
            
    private double orgSceneX, orgSceneY;
    
    
    private final Group GROUP; 
    private final Connector C; 
    private final Plane PLANE;
    private final IfConditionInterface PARENT;
    private final ParentConnector PARENTCONNECTOR;
    private MappingItemGraphics graphics;
    private ContextMenu m;
    private Action action;
    private LogisticServiceProvider lsp;
    private MappingType mappingType;
    private List<DataKey> keys;
    
    public RuleBuilderAction(Plane plane, Dock dockStart, Dock dockEnd, IfConditionInterface parent, ParentConnector parentConnector, LogisticServiceProvider lsp, MappingType mappingType, List<DataKey> keys) {
        
        this.keys = keys;
        this.lsp = lsp;
        this.mappingType = mappingType;
        this.PARENTCONNECTOR = parentConnector;
        
        graphics = new MappingItemGraphics();
        graphics.setLsp(lsp);
        graphics.setMappingType(mappingType);
        graphics.setItemType(ItemType.ACTION);
        
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
        m = new ContextMenu();
        
        
        
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
        actionTypes = FXCollections.observableArrayList();
        initComponents();
    }
    
    
    

    private void initComponents() {
        this.GROUP.getChildren().add(drawLeftBox());
        this.GROUP.getChildren().add(drawContentBox());
        this.GROUP.getChildren().add(drawLabel());
        this.GROUP.getChildren().add(drawLabelAction());
    }
    
    
    private Path drawLeftBox(){
        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(0.0f);
        moveTo.setY(0.0f);
        LineTo LineTo1 = new LineTo();
        LineTo1.setX(30);
        LineTo1.yProperty().bind(height.divide(2));
        LineTo LineTo2 = new LineTo();
        LineTo2.setX(0);
        LineTo2.yProperty().bind(height);
        VLineTo vLineTo1 = new VLineTo();
        vLineTo1.setY(0);
        
        path.getElements().add(moveTo);
        path.getElements().add(LineTo1);
        path.getElements().add(LineTo2);
        path.getElements().add(vLineTo1);
        
        path.setFill(Color.LIGHTBLUE);
        path.setStroke(null);
        return path;
    }
    
    private Path drawContentBox(){
        Path path = new Path();
        MoveTo moveTo = new MoveTo();
        moveTo.setX(0.0f);
        moveTo.setY(0.0f);
        LineTo LineTo1 = new LineTo();
        LineTo1.setX(30);
        LineTo1.yProperty().bind(height.divide(2));
        LineTo LineTo2 = new LineTo();
        LineTo2.setX(0);
        LineTo2.yProperty().bind(height);
        HLineTo hLineTo1 = new HLineTo();
        hLineTo1.xProperty().bind(width);
        VLineTo vLineTo1 = new VLineTo();
        vLineTo1.setY(0);
        HLineTo hLineTo2 = new HLineTo();
        hLineTo2.setX(0);
        
        path.getElements().add(moveTo);
        path.getElements().add(LineTo1);
        path.getElements().add(LineTo2);
        path.getElements().add(hLineTo1);
        path.getElements().add(vLineTo1);
        path.getElements().add(hLineTo2);
        
        path.setFill(Color.web("0xEEEEEE",1.0));
        path.setStroke(null);
        path.setCursor(Cursor.MOVE);
        
        return path;

    }
    
    private Label drawLabel(){
        Label l = new Label("do");
        l.setLayoutX(5);
        l.layoutYProperty().bind(height.divide(2).subtract(l.heightProperty().divide(2)));
        l.setContextMenu(m);
        return l;
    }
    
    private Label drawLabelAction(){
        Label labelAction = new Label();
        labelAction.textProperty().bind(actionDisplay);
        labelAction.setLayoutX(35);
        labelAction.layoutYProperty().bind(height.divide(2).subtract(labelAction.heightProperty().divide(2)));
        return labelAction;
    }
    
    public void AddActionType(Class<? extends RuleBuilderActionType> actionType){
        this.actionTypes.add(actionType);
        buildContextMenu();
    }
    
    public void AddActionTypes(List<Class<? extends RuleBuilderActionType>> actionTypes){
        this.actionTypes.clear();
        this.actionTypes.addAll(actionTypes);
        buildContextMenu();
        
    }
    
    private void buildContextMenu(){
        m.getItems().clear();
        for (Class<? extends RuleBuilderActionType> a:actionTypes){
            try {
                Method method = a.getMethod("getDisplayName");
                
                MenuItem mi = new MenuItem((String) method.invoke(m, null));
                mi.setOnAction(ev->{
                    
                    if (actionType == null || !actionType.getClass().equals(a)){
                        try {
                            actionType = a.newInstance();
                        } catch (InstantiationException ex) {
                            Exceptions.printStackTrace(ex);
                        } catch (IllegalAccessException ex) {
                            Exceptions.printStackTrace(ex);
                        }
                    }
                    Stage stage = new Stage();
                    stage.setScene(actionType.openMenu());
                    actionType.setDataKeys(keys);
                    stage.initStyle(StageStyle.UTILITY);
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.setAlwaysOnTop(true);
                    stage.showAndWait();
                    actionDisplay.setValue(actionType.displayedValue());
                    
                });
                m.getItems().add(mi);
            } catch (NoSuchMethodException ex) {
                Exceptions.printStackTrace(ex);
            } catch (SecurityException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        MenuItem mi2 = new MenuItem("Delete");
        mi2.setOnAction(ev->{
            PLANE.remove(this);
        });
        m.getItems().add(mi2);
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void shift(Dock dockEnd) {
        GROUP.setLayoutX(dockEnd.xProperty.getValue());
        GROUP.setLayoutY(dockEnd.yProperty.getValue());
    }

    @Override
    public Action getAction() {
        actionType.persist();
        if (this.action == null){
            this.action = new Action();
        }
        
        try {
            Method method = actionType.getClass().getMethod("getActionType");
            action.setActionType((ActionType) method.invoke(null,null));
        } catch (NoSuchMethodException ex) {
            Exceptions.printStackTrace(ex);
        } catch (SecurityException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IllegalArgumentException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InvocationTargetException ex) {
            Exceptions.printStackTrace(ex);
        }
        action.setActionId(actionType.persist());
        action.setLsp(lsp);
        action.setMappingType(mappingType);
        if (PARENT != null) {
            action.setParent(PARENT.getRule());
        } else {
            action.setParent(null);
        }
        action.setParentConnector(PARENTCONNECTOR);
        return action;
        
    }

    @Override
    public void setAction(Action a) {
        this.action = a;
        for (Class<? extends RuleBuilderActionType> act:actionTypes){
            ActionType at = null;
            try {
                Method method = act.getMethod("getActionType");
                at = (ActionType) method.invoke(null,null);
            } catch (NoSuchMethodException ex) {
                Exceptions.printStackTrace(ex);
            } catch (SecurityException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalAccessException ex) {
                Exceptions.printStackTrace(ex);
            } catch (IllegalArgumentException ex) {
                Exceptions.printStackTrace(ex);
            } catch (InvocationTargetException ex) {
                Exceptions.printStackTrace(ex);
            }
            
            if (at.equals(action.getActionType())){
                try {
                    this.actionType = act.newInstance();
                    this.actionType.load(this.action.getActionId());
                } catch (InstantiationException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
                
            }
        }
        
        this.lsp = action.getLsp();
        actionDisplay.setValue(actionType.displayedValue());
    }

    
}
