package dbbot;

import dao.DAO;
import entitys.BeAufgabe;
import entitys.Modul;
import entitys.Teilnahme;
import entitys.Thema;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse stellt das Herz der Aufgabenberechnung dar. Dabei berechnet 
 * Sie wie hoch die Einzelnen Aufgaben eines Themas priorisiert sind, um sie
 * dann in die Datenbank als ZuAufgabe einzupflegen.
 * @author Seve
 */
public class DeepThoughtPrio {
    
    private final StatMap stats;
    
    private final Timestamp heute;
    
    private final Modul modul;
    
    private final boolean datenerfassen;
    
    private final HashMap<Long,StatMatrix> ergebnisse;
    
    private final Set<Long> nichtErfolgreich;
    
    private final long[] themen;
    
    private final int[] prozent;
    
    /**
     * Erstellt einen neuen Priorisierer fuer ein Modul.
     * 
     * @param m
     * @param heute Zeitpunkt auf den die Berechnung bezogen werden soll.
     * (Meistens jetzt)
     */
    public DeepThoughtPrio(Modul m, Timestamp heute) {
        
        this.heute = heute;
        modul = m;
        
        stats = new StatMap(m);
        
        datenerfassen = DAO.wurdeVeraendert(m);
        
        ergebnisse = new HashMap<>();
        
        Collection<Thema> t = m.getThemen();
        themen = new long[t.size()];
        prozent = new int[t.size()];
        
        int i = 0;
        for(Thema te : t) {
            themen[i] = te.getId();
            prozent[i] = te.getAnteil();
            i++;
        }
        
        nichtErfolgreich = new HashSet<>();
    }
    
    /**
     * Berechnet die einzelnen Prioritaeten der Aufgaben und gib den gesamten
     * erfolgswert fuer den LernStatus zuruek
     * 
     * @param aufgaben alle Aufgaben und wie sie beantwortet wurden
     * @return Der Erfolgswert des Lernstatus. Wert zwischen 0 und 1000.
     */
    public int berechnePrioritaet(List<AufgabenItem> aufgaben) {
        
        long eineWoche = (1000 * 60 * 60 * 24 * 7);
        
        long heuteMs = heute.getTime();
        
        long lsPunkte = 0;

        for(AufgabenItem item : aufgaben) {
            
            int punkte = item.getAufgabe().getPunkte();
            
            long aufgabenPunkte = 0;
            
            long beantwortetPunkte = 0;
            
            for(BeantwortetItem beItem : item.gibBeantwortet()) {
                
                long woche = (heuteMs - beItem.getDatum().getTime()) / eineWoche;
                
                int anteilPunkte = stats.gibStat((short) woche).getAnteil() * punkte;
                
                aufgabenPunkte += anteilPunkte;
                
                if(beItem.isRichtig()) {
                    beantwortetPunkte += (beItem.isHinweis()) ? anteilPunkte / 2 : anteilPunkte;
                }
                
            }
            
            int prioPunkte = 0;
            
            if(aufgabenPunkte != 0) {
                prioPunkte = (int)((beantwortetPunkte * 1000) / aufgabenPunkte);
            }
            item.setPunkte(prioPunkte);
            
            lsPunkte += prioPunkte;
        }
        
        return (int) lsPunkte / aufgaben.size();
        
    }
    
    /**
     * Gibt an, ob daten zur neuberechnung der Statistiken erfasst werden solln.
     * @return true, falls alle daten erfasst werden solln.
     */
    public boolean sollDatenErfassen() {
        return datenerfassen;
    }
    
    /**
     * Berechnet die Statistiken eines Moduls neu aus und setzt die Ergebnisse
     * in die Datenbank ein.
     */
    public void rechnenStatistiken() {
       
        if(this.themen.length < 2) {
            if(themen.length == 1) {
                prozent[0] = 100;
            }
            
            DAO.setzeAnteil(themen,prozent);
            
            return;
        }
        
        ArrayList<StatMatrix> noten = new ArrayList<>();
          
        noten.addAll(ergebnisse.values());
        
        if(noten.size() < 2) {
            //Hier vielleicht andere Teilnehmer benachrichtigen um Note einzutragen.
            return;
        
        }
        Collections.sort(noten);
        
        //Nun liegen alle Statistiken in nach Noten sortierter Reichenfolge vor.
        
        
        Iterator<StatMatrix> it = noten.iterator();
        
        StatMatrix sm = it.next();
        StatMatrix pre = sm;
        StatMatrix next;
        
        while(it.hasNext()) {
            next = it.next();
            
            pre.setNext(next);
            pre = next;
        }
        
        //Hier werden nun die Anteiel der Themen berechnet.
        sm.berechneVerhaeltnisse();
        int zaehler = 0;
        while(zaehler < 200 && sm.berechneThemen(prozent)) {
            zaehler++;
        }
        
        DAO.setzeAnteil(themen,prozent);
        
        sm.setzeAnteilThemen(prozent);
        
        //Jetzt werden die wochen berechnet
        zaehler = 0;
        while(zaehler < 200 && sm.berechneWochen(stats)) {
            zaehler++;
        }
        
        //Die neuen Statistiekn abspeichern
        this.stats.merge();
        
        //Fertig!
        sm.speichereProzent();
    }
    

    /**
     * Fuegt dem Modul eine Bearbeitete Aufgabe hinzu um diese dann fuer die
     * Auswertung der Klausuren zu beruecksichtigen.
     * @param be 
     */
    public void addAntwort(BeAufgabe be) {
        
        Long bid = be.getLernStatus().getBenutzer().getId();
        
        if(ergebnisse.containsKey(bid)) {
            ergebnisse.get(bid).addAntwort(be);
        } else {
            
            if(!nichtErfolgreich.contains(bid)) {
                //Bissher werden nur erfolgreiche pruefungen betrachtet.
                Teilnahme t = DAO.gibTeilnahme(bid,modul.getKuerzel());
            
                if(t != null) {
                    StatMatrix mx = new StatMatrix(t,themen);

                    ergebnisse.put(bid, mx);
                } else {
                    nichtErfolgreich.add(bid);
                }
            }
            
            
        }
        
    }
    
}
