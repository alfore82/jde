/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.emissioncalculation.EmissionFactor;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import org.openide.util.NbPreferences;

/**
 *
 * @author forell
 */
public class EmissionFactorService {
    private static final EmissionFactorService INSTANCE = new EmissionFactorService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private EmissionFactorService(){
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
    
    public static EmissionFactorService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteEmissionFactor(EmissionFactor c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        EmissionFactor cdel = em.find(EmissionFactor.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditEmissionFactor(EmissionFactor c){
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
    
    public synchronized List<EmissionFactor> findAllEmissionFactor(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<EmissionFactor> result = em.createQuery("SELECT c FROM EmissionFactor c order by c.name asc", EmissionFactor.class).getResultList();
        em.close();
        return result;
    }
    
    public synchronized EmissionFactor findEmissionFactor(long id){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        EmissionFactor result = em.find(EmissionFactor.class, id);
        em.close();
        return result;
    }
    
}
