/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.ui.RuleBuilder;


import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ParentConnector;
import com.thinkstep.jde.persistence.entities.Rules.Rule;
import com.thinkstep.jde.persistence.entities.graphics.MappingItemGraphics;
import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
import com.thinkstep.jde.persistence.services.ActionService;
import com.thinkstep.jde.persistence.services.DataKeyService;
import com.thinkstep.jde.persistence.services.MappingItemGraphicsService;
import com.thinkstep.jde.persistence.services.RuleService;
import java.util.ArrayList;
import java.util.List;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;


/**
 *
 * @author forell
 */
public class RuleBuilder extends AnchorPane implements Plane{
    private final Group group = new Group();
    List<MappingItem> items;
    List<DataKey> dataKeys = new ArrayList<>();
    protected List<Class<? extends RuleBuilderActionType>> actionTypes;
    private final LogisticServiceProvider lsp;
    protected MappingType mappingType = MappingType.FUELTYPE;
    private final Circle cir;
    protected Label label;
    protected Button btn;
    
        public RuleBuilder(LogisticServiceProvider lsp){
            this.btn = new Button();
            this.btn.setText("Copy View");
            AnchorPane.setRightAnchor(btn, 14.0);
            AnchorPane.setTopAnchor(btn, 14.0);
            this.getChildren().add(btn);
            actionTypes = new ArrayList<>();
            this.lsp = lsp;
            label = new Label("RuleBuilder");
            AnchorPane.setLeftAnchor(label, 14.0);
            AnchorPane.setTopAnchor(label, 14.0);
            this.getChildren().add(label);
            ScrollPane sc = new ScrollPane();
            AnchorPane.setLeftAnchor(sc, 0.0);
            AnchorPane.setTopAnchor(sc, 44.0);
            AnchorPane.setBottomAnchor(sc, 44.0);
            AnchorPane.setRightAnchor(sc, 0.0);
            this.getChildren().add(sc);
            sc.setContent(group);
            Button b = new Button("save");
            AnchorPane.setBottomAnchor(b, 10.0);
            AnchorPane.setRightAnchor(b, 14.0);
            this.getChildren().add(b);
            b.setOnAction(f->save());
            items = new ArrayList<>();
            cir = new Circle();
            cir.setCenterX(250);
            cir.setCenterY(20);
            cir.setRadius(10);
            cir.setFill(Color.BLACK);
            group.getChildren().add(cir);
            this.btn.setOnAction(new EventHandler<ActionEvent>(){
                @Override
                public void handle(ActionEvent event){
                    final Clipboard cb = Clipboard.getSystemClipboard();
                    WritableImage snapshot = group.snapshot(new SnapshotParameters(), null);
                    ClipboardContent content = new ClipboardContent();
                    content.putImage(snapshot);
                    cb.setContent(content);
                }
            
            });
            
            
            ContextMenu cm  =new ContextMenu();
            MenuItem addCondition = new MenuItem("Add Condition");
            addCondition.setOnAction(ev->{
                SimpleDoubleProperty y = new SimpleDoubleProperty();
                y.bind(cir.centerYProperty().add(cir.radiusProperty()));
                Dock dockStart = new Dock();
                dockStart.xProperty.bind(cir.centerXProperty());
                dockStart.yProperty.bind(cir.centerYProperty().add(cir.radiusProperty()));
                addCondition(dockStart, null, ParentConnector.ALL);
            });
            
            MenuItem addAction = new MenuItem("Add Action");
            addAction.setOnAction(ev->{
                SimpleDoubleProperty y = new SimpleDoubleProperty();
                y.bind(cir.centerYProperty().add(cir.radiusProperty()));
                Dock dockStart = new Dock();
                dockStart.xProperty.bind(cir.centerXProperty());
                dockStart.yProperty.bind(cir.centerYProperty().add(cir.radiusProperty()));
                addAction(dockStart, null, ParentConnector.ALL);
            });
            
            
            cm.getItems().addAll(addCondition,addAction);
            cir.setOnMouseClicked(t->{
                if(t.getButton().toString().equals("SECONDARY"))
                cm.show(cir,Side.RIGHT,0,0);
            });
            loadKeys();
        }
        
    @Override
    public MappingItem addCondition(Dock dockStart, MappingItem parent, ParentConnector parentConnector){
        Dock dockEnd = new Dock(dockStart.xProperty.doubleValue(), dockStart.yProperty.doubleValue()+100);
        RuleBuilderIfCondition ifCondition = new RuleBuilderIfCondition(this, dockStart, dockEnd, (IfConditionInterface) parent, parentConnector,lsp, mappingType, dataKeys);
        items.add(ifCondition);
        group.getChildren().add(ifCondition);
        return ifCondition;
    }
    
    @Override
    public MappingItem addAction(Dock dockStart, MappingItem parent, ParentConnector parentConnector){
        Dock dockEnd = new Dock(dockStart.xProperty.doubleValue(), dockStart.yProperty.doubleValue()+100);
        RuleBuilderAction action = new RuleBuilderAction(this, dockStart, dockEnd, (IfConditionInterface) parent, parentConnector,lsp, mappingType, dataKeys);
        action.AddActionTypes(actionTypes);
        items.add(action);
        group.getChildren().add(action);
        return action;
    }
    
    @Override
    public void remove(MappingItem parent){
        List<MappingItem> siblings = findAllSibling(parent);
        for (MappingItem sibling:siblings){
            group.getChildren().remove(sibling.getGroup());
        }
        group.getChildren().remove(parent);
        items.removeAll(siblings);
        items.remove(parent);
    }
    
    private List<MappingItem> findAllSibling(MappingItem parent){
        if (parent == null){
            return items;
        } else {
        List<MappingItem> siblings = new ArrayList<>();
            for (MappingItem item:items){
                if (parent.equals(item.getParentItem())){
                    siblings.add(item);
                }
            }
            List<MappingItem> subsiblings = new ArrayList<>();
            for (MappingItem sibling:siblings){
                subsiblings.addAll(findAllSibling(sibling));
            }
            siblings.addAll(subsiblings);
            return siblings;
        }
    }
    
    private List<MappingItem> findRoots(){
        List<MappingItem> roots = new ArrayList<>();
        for (MappingItem item:items) {
            if (item.getParentItem()== null){
                roots.add(item);
            }
        }
        return roots;
    }
    
    private void save(){
        boolean save = checkSave();
        if (save){
            MappingItemGraphicsService migs = MappingItemGraphicsService.getINSTANCE();
            RuleService ruleService = RuleService.getINSTANCE();
            ActionService actionService = ActionService.getINSTANCE();

            migs.deleteMappingItemGraphicsByLspByMappingType(lsp, mappingType);
            ruleService.deleteRulesByLspAndMappingTypeAndParent(lsp, mappingType);
            actionService.deleteActionsByLspAndMappingType(lsp, mappingType);

            List<MappingItem> parentItems = findRoots();
            for (MappingItem parentItem:parentItems){
                MappingItemGraphics mig = parentItem.getMappingItemGraphics();
                migs.addOrEditMappingItemGraphics(mig);
                if (parentItem instanceof IfConditionInterface){
                    IfConditionInterface cond = (IfConditionInterface) parentItem;
                    Rule r = cond.getRule();
                    r.setMappingItemId(mig.getId());
                    ruleService.addOrEditRule(r);
                }
                if (parentItem instanceof ActionInterface){
                    ActionInterface action = (ActionInterface) parentItem;
                    Action a = action.getAction();
                    a.setMappingItemId(mig.getId());
                    actionService.addOrEditAction(a);
                }

                saveChildren(parentItem);

            }
            
        } else {
            Alert al = new Alert(AlertType.WARNING);
            al.setHeaderText("Cannot save changes");
            al.setContentText("Not all items are completely defined, the changes cannot be saved.");
            al.show();
        }
        
        
        
    }
    
    private void saveChildren(MappingItem parent){
        List<MappingItem> siblings = new ArrayList<>();
        for (MappingItem item:items){
            if (parent.equals(item.getParentItem())){
                siblings.add(item);
            }
        }
        MappingItemGraphicsService migs = MappingItemGraphicsService.getINSTANCE();
        RuleService ruleService = RuleService.getINSTANCE();
        ActionService actionService = ActionService.getINSTANCE();
        for (MappingItem sibling:siblings){
            MappingItemGraphics mig = sibling.getMappingItemGraphics();
            migs.addOrEditMappingItemGraphics(mig);
            if (sibling instanceof IfConditionInterface){
                IfConditionInterface cond = (IfConditionInterface) sibling;
                Rule r = cond.getRule();
                r.setMappingItemId(mig.getId());
                ruleService.addOrEditRule(r);
            }
            if (sibling instanceof ActionInterface) {
                ActionInterface action = (ActionInterface) sibling;
                Action a = action.getAction();
                a.setMappingItemId(mig.getId());
                actionService.addOrEditAction(a);
            }
            saveChildren(sibling);
        }
    }
    
    private void loadParents(){
        MappingItemGraphicsService migservice = MappingItemGraphicsService.getINSTANCE();
        RuleService rService = RuleService.getINSTANCE();
        ActionService aService = ActionService.getINSTANCE();
        List<MappingItemGraphics> migs= migservice.findAllLMappingItemGraphicsByLspByMappingType(lsp, mappingType);
        List<MappingItemGraphics> parents = new ArrayList<>();
        for (MappingItemGraphics mig:migs){
            if (mig.getMappingItemGraphics() == null) parents.add(mig);
        }
        for (MappingItemGraphics parent:parents){
        MappingItem mi = null;
            if (parent != null){
                SimpleDoubleProperty y = new SimpleDoubleProperty();
                y.bind(cir.centerYProperty().add(cir.radiusProperty()));
                Dock dockStart = new Dock();
                dockStart.xProperty.bind(cir.centerXProperty());
                dockStart.yProperty.bind(cir.centerYProperty().add(cir.radiusProperty()));
                switch (parent.getItemType()){
                    case IFCONDITION:
                        Rule r = rService.findRulesMappingItemId(parent.getId());
                        if (r!=null) mi = addCondition(dockStart, null, r.getParentConnector());
                        else mi = addCondition(dockStart, null, null);
                        mi.shift(new Dock(parent.getLayoutX(),parent.getLayoutY()));
                        mi.setMappingItemGraphics(parent);
                        IfConditionInterface cond = (IfConditionInterface) mi;
                        cond.setRule(r);
                        migs.remove(parent);
                        loadChildren(mi, parent, migs);
                        break;
                    case ACTION:
                        Action a = aService.findActionMappingItemId(parent.getId());
                        if (a !=null) mi = addAction(dockStart, null, a.getParentConnector());
                        else mi = addAction(dockStart, null, null);
                        
                        mi.shift(new Dock(parent.getLayoutX(),parent.getLayoutY()));
                        mi.setMappingItemGraphics(parent);
                        ActionInterface action = (ActionInterface) mi;
                        action.setAction(a);
                        migs.remove(parent);
                        break;
                }

            }
        }
    }
    
    private void loadChildren(MappingItem pi, MappingItemGraphics parent, List<MappingItemGraphics> migs){
         RuleService rService = RuleService.getINSTANCE();
        ActionService aService = ActionService.getINSTANCE();
        List<MappingItemGraphics> children = new ArrayList<>();
        for (MappingItemGraphics item:migs){
            if (parent.equals(item.getMappingItemGraphics())){
                children.add(item);
            }
        }
        for (MappingItemGraphics child:children){
            Dock dockStart = new Dock(child.getDockLayoutX(),child.getDockLayoutY());
            dockStart = pi.getDockStart(dockStart);
            MappingItem ci = null;
            switch (child.getItemType()){
                case IFCONDITION:
                    Rule r = rService.findRulesMappingItemId(child.getId());
                    ci = addCondition(dockStart, pi, r.getParentConnector());
                    ci.setMappingItemGraphics(child);
                    ci.shift(new Dock(child.getLayoutX(),child.getLayoutY()));
                    IfConditionInterface cond = (IfConditionInterface) ci;
                    cond.setRule(r);
                    loadChildren(ci,child,migs);
                    break;
                case ACTION:
                    Action a = aService.findActionMappingItemId(child.getId());
                    ci = addAction(dockStart, pi, a.getParentConnector());
                    ci.setMappingItemGraphics(child);
                    ci.shift(new Dock(child.getLayoutX(),child.getLayoutY()));
                    ActionInterface action = (ActionInterface) ci;
                    action.setAction(a);
                    break;
            }
        }
        
        
    
        
    }
    
    private void loadKeys(){
        DataKeyService dkService = DataKeyService.getINSTANCE();
        dataKeys = dkService.findDataKeysByLsp(lsp);
    }
    
    public void load(){
        loadParents();
    }

    private boolean checkSave() {
        
        boolean save = true;
        
        List<MappingItem> parentItems = findRoots();
        for (MappingItem parentItem:parentItems){
            if (parentItem instanceof IfConditionInterface){
                IfConditionInterface cond = (IfConditionInterface) parentItem;
                save = save & cond.checkSave();
            }
            if (parentItem instanceof ActionInterface){
                ActionInterface action = (ActionInterface) parentItem;
                save = save & action.checkSave();
            }
            save = save & checkSaveChildren(parentItem);
            
        }
        return save;
    }

    private boolean checkSaveChildren(MappingItem parent) {
        boolean save = true;
        
        List<MappingItem> siblings = new ArrayList<>();
        for (MappingItem item:items){
            if (parent.equals(item.getParentItem())){
                siblings.add(item);
            }
        }
        for (MappingItem sibling:siblings){
            if (sibling instanceof IfConditionInterface){
                IfConditionInterface cond = (IfConditionInterface) sibling;
                save = save & cond.checkSave();
            }
            if (sibling instanceof ActionInterface) {
                ActionInterface action = (ActionInterface) sibling;
                save = save & action.checkSave();
            }
            save = save & checkSaveChildren(sibling);
        }
        return save;
    }
    
}
