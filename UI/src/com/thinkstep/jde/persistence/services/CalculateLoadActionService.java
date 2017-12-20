/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.Rules.AssignConstantAction;
import com.thinkstep.jde.persistence.entities.Rules.AssignVehicleTypeAction;
import com.thinkstep.jde.persistence.entities.Rules.CalculateLoadAction;
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
public class CalculateLoadActionService {
    private static final CalculateLoadActionService INSTANCE = new CalculateLoadActionService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private CalculateLoadActionService(){
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
    
    public static CalculateLoadActionService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteCalculateLoadAction(CalculateLoadAction c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        CalculateLoadAction cdel = em.find(CalculateLoadAction.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void deleteCalculateLoadActionId(long id){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        CalculateLoadAction cdel = em.find(CalculateLoadAction.class, id);
        if (cdel != null){
            em.remove(cdel);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditCalculateLoadAction(CalculateLoadAction c){
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
    
    public synchronized List<CalculateLoadAction> findCalculateLoadAction(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<CalculateLoadAction> result = em.createQuery("SELECT c FROM CalculateLoadAction c ").getResultList();
        em.close();
        return result;
    }
    
    public synchronized CalculateLoadAction findCalculateLoadActionById(long id){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM CalculateLoadAction c WHERE c.id = :id");
        query.setParameter("id", id);
        CalculateLoadAction result = (CalculateLoadAction) query.getSingleResult();
        em.close();
        return result;
    }
    
}
