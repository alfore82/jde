/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import com.thinkstep.jde.persistence.entities.rawdata.DataKey;
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
public class DataKeyService {
    private static final DataKeyService INSTANCE = new DataKeyService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private DataKeyService(){
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
    
    public static DataKeyService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteDataKey(DataKey c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        DataKey cdel = em.find(DataKey.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditDataKey(DataKey c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        if (c.getId()== 0){
            em.persist(c);
        } else {
            em.find(DataKey.class, c);
            em.merge(c);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditLspDataKey(List<DataKey> cs){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        for (DataKey c:cs){
            if (c.getId()== 0){
                em.persist(c);
            } else {
                //em.find(DataKey.class, c);
                em.merge(c);
            }
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized List<DataKey> findAllDataKeys(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<DataKey> result = em.createQuery("SELECT c FROM DataKey c order by c.key").getResultList();
        em.close();
        return result;
    }
    
    public synchronized List<DataKey> findDataKeysByLsp(LogisticServiceProvider lsp){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM DataKey c where c.lsp = :lsp order by c.key");
        query.setParameter("lsp", lsp);
        List<DataKey> result = query.getResultList();
        em.close();
        return result;
    }
    
    public synchronized DataKey findDataKeysByLspAndName(LogisticServiceProvider lsp, String name){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM DataKey c where c.lsp = :lsp  and c.key LIKE :name order by c.key");
        query.setParameter("lsp", lsp);
        query.setParameter("name", name);
        List<DataKey> result = query.getResultList();
        em.close();
        if (result.size()>0){
            return result.get(0);
        } else {
            return null;
        }
        
    }
    
}
