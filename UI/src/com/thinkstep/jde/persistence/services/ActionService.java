/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.Country;
import com.thinkstep.jde.persistence.entities.Rules.Action;
import com.thinkstep.jde.persistence.entities.Rules.ParentConnector;
import com.thinkstep.jde.persistence.entities.Rules.Rule;
import com.thinkstep.jde.persistence.entities.graphics.MappingType;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.openide.util.NbPreferences;

/**
 *
 * @author forell
 */
public class ActionService {
    private static final ActionService INSTANCE = new ActionService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private ActionService(){
        listeners = new HashSet<>();
        
        pref = NbPreferences.forModule(NbPreferences.class);
        pref.addPreferenceChangeListener((PreferenceChangeEvent evt) -> {
            if ((evt.getKey().equals("dbrefresh") && evt.getNewValue().equals("true")) ||
                    (evt.getKey().equals("importnotification") && evt.getNewValue().equals("true"))){
                refresh();
            }
        } 
        );
    }
    
    private synchronized void refresh(){
        listeners.forEach(listener->listener.refresh());
    }
    
    public static ActionService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteAction(Action c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        Action cdel = em.find(Action.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditAction(Action c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        if (c.getId()== 0){
            em.persist(c);
        } else {
            em.merge(c);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized List<Action> findAllActions(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<Action> result = em.createQuery("SELECT c FROM Action c ").getResultList();
        em.close();
        return result;
    }
    
    public synchronized List<Action> findAllActionsByLspAndMappingType(LogisticServiceProvider lsp, MappingType mappingType){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM Action c where c.lsp = :lsp and c.mappingType = :mappingType");
        query.setParameter("lsp", lsp);
        query.setParameter("mappingType", mappingType);
        List<Action> result = query.getResultList();
        em.close();
        return result;
    }
    
    public synchronized void deleteActionsByLspAndMappingType(LogisticServiceProvider lsp, MappingType mappingType){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM Action c where c.lsp = :lsp and c.mappingType = :mappingType");
        query.setParameter("lsp", lsp);
        query.setParameter("mappingType", mappingType);
        List<Action> actions = query.getResultList();
        for (Action a:actions){
            switch (a.getActionType()) {
                case ASSIGNRAWDATAVALUE:
                    ValueActionService vaService = ValueActionService.getINSTANCE();
                    vaService.deleteValueActionById(a.getActionId());
                    break;
                case ASSIGNCONSTANT:
                    ConstantActionService caService = ConstantActionService.getINSTANCE();
                    caService.deleteConstantActionById(a.getActionId());
                    break;
                case VEHICLETYPE:
                    VehicleTypeActionService vtaService = VehicleTypeActionService.getINSTANCE();
                    vtaService.deleteVehicleTypeActionById(a.getActionId());
                    break;
                case FUELTYPE:
                    FuelTypeActionService ftaService = FuelTypeActionService.getINSTANCE();
                    ftaService.deleteFuelTypeActionById(a.getActionId());
                    break;
                case UNDEFINED:
            }
        }
        em.getTransaction().begin();
        for (Action a:actions){
            em.remove(a);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized List<Action> findAllActionsByLspAndMappingTypeAndParent(LogisticServiceProvider lsp, MappingType mappingType, Rule parent){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query;
        if (parent == null){
            query = em.createQuery("SELECT c FROM Action c where c.lsp = :lsp and c.mappingType = :mappingType and c.parent IS NULL");
        } else {
            query = em.createQuery("SELECT c FROM Action c where c.lsp = :lsp and c.mappingType = :mappingType and c.parent = :parent");
            query.setParameter("parent", parent);
        }
        query.setParameter("lsp", lsp);
        query.setParameter("mappingType", mappingType);
        
        List<Action> result = query.getResultList();
        em.close();
        return result;
    }
    
    public synchronized List<Action> findAllActionsByLspAndMappingTypeAndParent(LogisticServiceProvider lsp, MappingType mappingType, Rule parent, ParentConnector pc){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query;
        if (parent == null){
            query = em.createQuery("SELECT c FROM Action c where c.lsp = :lsp and c.mappingType = :mappingType and c.parent IS NULL and c.parentConnector = :pc");
        } else {
            query = em.createQuery("SELECT c FROM Action c where c.lsp = :lsp and c.mappingType = :mappingType and c.parent = :parent and c.parentConnector = :pc");
            query.setParameter("parent", parent);
        }
        query.setParameter("lsp", lsp);
        query.setParameter("mappingType", mappingType);
        query.setParameter("pc", pc);
        
        List<Action> result = query.getResultList();
        em.close();
        return result;
    }
    
    
    public synchronized Action findActionMappingItemId(long id){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM Action c where c.mappingItemId = :id");
        query.setParameter("id", id);
        List<Action> results = query.getResultList();
        em.close();
        if (results.size() > 0){
            return results.get(0);
        }        
        return null;
    }
    
}
