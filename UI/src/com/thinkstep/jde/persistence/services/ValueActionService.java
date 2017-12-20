/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.Rules.AssignValueAction;
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
public class ValueActionService {
    private static final ValueActionService INSTANCE = new ValueActionService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private ValueActionService(){
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
    
    public static ValueActionService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteValueAction(AssignValueAction c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        AssignValueAction cdel = em.find(AssignValueAction.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void deleteValueActionById(long id){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        AssignValueAction cdel = em.find(AssignValueAction.class, id);
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditValueAction(AssignValueAction c){
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
    
    public synchronized List<AssignValueAction> findAllValueActions(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<AssignValueAction> result = em.createQuery("SELECT c FROM AssignValueAction c ").getResultList();
        em.close();
        return result;
    }
    
    public synchronized AssignValueAction findValueActionById(long id){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM AssignValueAction c WHERE c.id = :id");
        query.setParameter("id", id);
        AssignValueAction result = (AssignValueAction) query.getSingleResult();
        em.close();
        return result;
    }
    
}
