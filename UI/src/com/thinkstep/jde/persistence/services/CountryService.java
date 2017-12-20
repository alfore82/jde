/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thinkstep.jde.persistence.services;

import com.thinkstep.jde.persistence.EntityManagerProvider;
import com.thinkstep.jde.persistence.entities.Country;
import java.util.ArrayList;
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
public class CountryService {
    private static final CountryService INSTANCE = new CountryService();
    private final Preferences pref;
    private final Set<UpdateNotification> listeners;
    private final List<Country> countries;
    
    private CountryService(){
        listeners = new HashSet<>();
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        countries = em.createQuery("SELECT c FROM Country c order by c.name asc").getResultList();
        em.close();
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
    
    public static CountryService getINSTANCE(){
       return INSTANCE;
    }
    
    public synchronized void addListener(UpdateNotification listener){
        this.listeners.add(listener);
    }
    
    public synchronized void removeListener(UpdateNotification listener){
        this.listeners.remove(listener);
    }
    
    public synchronized void deleteCountry(Country c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        Country cdel = em.find(Country.class, c.getId());
        countries.remove(c);
        em.remove(cdel);
        em.getTransaction().commit();
        em.close();
    }
    
    public synchronized void addOrEditCountry(Country c){
        EntityManager em = EntityManagerProvider.getInstance().getEntityManager();
        em.getTransaction().begin();
        if (c.getId()== 0){
            em.persist(c);
        } else {
            em.merge(c);
        }
        em.getTransaction().commit();
        countries.add(c);
        em.close();
    }
    
    public synchronized List<Country> findAllCountries(){
        
        
        return countries;
    }
    
    public synchronized Country findCountryByCode(String code){
        for (Country c:countries){
            if (c.getCountryCode().toLowerCase().equals(code.toLowerCase()) ||
                    c.getCountryCodeISO().toLowerCase().equals(code.toLowerCase()) ||
                    c.getName().toLowerCase().equals(code.toLowerCase())){
                return c;
            }
            
        }
        
        return null;
        
    }
    
}
