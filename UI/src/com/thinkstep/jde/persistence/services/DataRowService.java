/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.DataRow;
import com.thinkstep.jde.persistence.entities.preferences.LogisticServiceProvider;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.Preferences;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.eclipse.persistence.sessions.Session;
import org.openide.util.NbPreferences;

/**
 *
 * @author forell
 */
public class DataRowService {
    private static final DataRowService INSTANCE = new DataRowService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    
    private DataRowService(){
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
    
    public static DataRowService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteDataRow(DataRow c){
        EntityManagerProvider emp = EntityManagerProvider.getInstance();
        EntityManager em =  emp.getEntityManager();
        em.getTransaction().begin();
        DataRow cdel = em.find(DataRow.class, c.getId());
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
        emp.doVaccum();
    }
    
    public synchronized void deleteDataRowByLsp(LogisticServiceProvider lsp){
        EntityManagerProvider emp = EntityManagerProvider.getInstance();
        EntityManager em =  emp.getEntityManager();
        em.getTransaction().begin();
        LogisticServiceProvider cdel = em.find(LogisticServiceProvider.class, lsp.getId());
        Query q = em.createQuery("Delete from DataRow d where d.lsp = :lsp");
        q.setParameter("lsp", cdel);
        q.executeUpdate();
        em.getTransaction().commit();
        em.close();
        emp.doVaccum();
    }
    
    public synchronized void deleteAllDataRows(){
        EntityManagerProvider emp = EntityManagerProvider.getInstance();
        EntityManager em = emp.getEntityManager();
        em.getTransaction().begin();
        Query q = em.createQuery("Delete from DataRow d");
        q.executeUpdate();
        em.getTransaction().commit();
        em.close();
        emp.doVaccum();
    }
    
    public synchronized void deleteDataRowByLsps(List<LogisticServiceProvider> lsps){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        for (LogisticServiceProvider lsp:lsps){
            LogisticServiceProvider cdel = em.find(LogisticServiceProvider.class, lsp.getId());
            Query q = em.createQuery("Delete from DataRow d where d.lsp = :lsp");
            q.setParameter("lsp", cdel);
            q.executeUpdate();
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditDataRow(DataRow c){
        c.serialize();
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        if (c.getId()== 0){
            em.persist(c);
        } else {
            em.find(DataRow.class, c);
            em.merge(c);
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditDataRows(List<DataRow> cs){
        
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        for (DataRow c:cs){
            c.serialize();
            if (c.getId()== 0){
                em.persist(c);
            } else {
                em.find(DataRow.class, c.getId());
                em.merge(c);
            }
        }
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized List<DataRow> findAllDataRows(){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        List<DataRow> result = em.createQuery("SELECT c FROM DataRow c order by c.name asc").getResultList();
        for (DataRow dr:result){
            dr.deSerialize();
        }
        em.close();
        return result;
    }
    
    public synchronized List<DataRow> findAllDataRowsByLsp(LogisticServiceProvider lsp){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        Query query = em.createQuery("SELECT c FROM DataRow c WHERE c.lsp = :lsp");
        query.setParameter("lsp", lsp);
        
        List<DataRow> result = query.getResultList();
        for (DataRow dr:result){
            dr.deSerialize();
        }
        em.close();
        return result;
    }
    
    
    
}
