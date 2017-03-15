package DBBot;

import DAO.DAO;
import DAO.EMH;
import Entitys.Aufgabe;
import Entitys.BeAufgabe;
import Entitys.LernStatus;
import Entitys.Modul;
import Entitys.Thema;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import main.CBBenutzer;
import main.ChatBotManager;

/**
 * Dieser Thread laeuft entweder ueber alle Lernstadi eines Benutzers oder
 * eines Moduls, wertet sie aus und legt neue zu bearbeitende Aufgaben an.
 * 
 * @author Seve
 */
public class AufgabenBot implements Runnable {

    private final CBBenutzer benutzer;
    
    private final Modul modul;
    
    private final LernStatus lernStatus;
    
    private final Timestamp heute;
    
    /**
     * Erstellt einen neuen Aufgabenbot, der ueber die Lernstadi eines Benutzers
     * laeuft.
     * 
     * @param benutzer 
     */
    public AufgabenBot(CBBenutzer benutzer) {
        this.benutzer = benutzer;
        this.modul = null;
        this.lernStatus = null;
        benutzer.gain();
        heute = ChatBotManager.getInstance().jetzt();
    }
     
    /**
     * Erstellt einen neuen Aufgabenbot, der ueber die Lernstadi eines Moduls
     * laeuft.
     * 
     * @param modul 
     */
    public AufgabenBot(Modul modul) {
        this.benutzer = null;
        this.modul = modul;
        lernStatus = null;
        heute = ChatBotManager.getInstance().jetzt();
    }
    
    public AufgabenBot(LernStatus ls) {
        this.lernStatus = ls;
        this.benutzer = null;
        this.modul = null;
        heute = ChatBotManager.getInstance().jetzt();
    }
    
    /**
     * Startet den Bot.
     */
    @Override
    public void run() {
        
        Collection<LernStatus> stadi;
        
        if(lernStatus != null) {
            stadi = new LinkedList<>();
            stadi.add(lernStatus);
            berechne(stadi,null);
        } else if(benutzer != null) {
            stadi = benutzer.getBenutzer().getLernStadi();
            berechne(stadi,null);
        } else {
            DeepThoughtPrio rechner = new DeepThoughtPrio(modul,heute);
            
            for(Thema t : modul.getThemen()) {
                stadi = t.getLernStadi();
                berechne(stadi,rechner);
            }
        }  
        
        if(benutzer != null) {
            //evet. hier nachricht an den Benutzer senden
            benutzer.release();
        }
        
        EMH.closeEntityManager();
    }
    
//        public void runsingle() {
//        
//        Collection<LernStatus> stadi;
//        
//        if(lernStatus != null) {
//            stadi = new LinkedList<>();
//            stadi.add(lernStatus);
//            berechne(stadi,null);
//        } else if(benutzer != null) {
//            stadi = benutzer.getBenutzer().getLernStadi();
//            berechne(stadi,null);
//        } else {
//            DeepThoughtPrio rechner = new DeepThoughtPrio(modul,heute);
//            
//            for(Thema t : modul.getThemen()) {
//                stadi = t.getLernStadi();
//                berechne(stadi,rechner);
//            }
//        }  
//        
//        if(benutzer != null) {
//            //evet. hier nachricht an den Benutzer senden
//            benutzer.release();
//        }
//    }
    
    
    /**
     * Berechnet zu den Angegebenen Lernstadi die entsprechenden Aufgaben und
     * setzt die Punkte des Lernstatus.
     * 
     * @param stadi Die zu berechnendnen Lernstadi.
     * @param rechner Der zu verwendende DeepThoughtPrio rechner. Dieser sollte
     * angegeben werden, wenn die Lernstadi alle vom selben Modul sind. 
     * Ansonsten null eintragen.
     */
    private void berechne(Collection<LernStatus> stadi, DeepThoughtPrio rechner) {
        Collection<Aufgabe> aufgaben;
        
        Collection<BeAufgabe> beAufgaben;
        
        List<aufgabenItem> zuAufgaben = new LinkedList<>();
        
        HashMap<Long,aufgabenItem> infos = new HashMap<>();
        
        Thema thema;
       
        for(LernStatus ls : stadi) {

            boolean neuberechen = (ls.istAktiv() && ls.isVeraendert());
            
            if(rechner != null && rechner.sollDatenErfassen() && !neuberechen) {
                //Einsetztn falls nicht neu berechnet werden muss
                
                for(BeAufgabe b : ls.getBeAufgaben()) {
                    rechner.addAntwort(b);
                }
            }
            
            // Gucken ob man neu berechnen muss
            
            if(neuberechen) {
                
                thema = ls.getThema();

                aufgaben = thema.getAufgaben();
                // Alle Aufgaben holen 
                for(Aufgabe a : aufgaben) {
                    
                    infos.put(a.getAufgabenID(), new aufgabenItem(a));  
                    
                }
                
                beAufgaben = ls.getBeAufgaben();
                
                //Zu allen  Aufgaben mappen wann zuletzt und wie oft insgesamt beantwortet
                for(BeAufgabe b : beAufgaben) {
                    if(b.istBeantwortet()) {
                        long id = b.getAufgabe().getAufgabenID();
                        aufgabenItem item = infos.get(id);
                        
                        item.addAntwort(b.getDatum(), b.istRichtig(), b.getHinweis());
                        
                        if(rechner != null && rechner.sollDatenErfassen()) {
                            // Einsetzt in matrix
                            rechner.addAntwort(b);
                        }
                    }                   
                }

                //Aufgaben nach prioritaet sortieren
                
                
                
                int punkteLs;
                
                for(aufgabenItem ai : infos.values()) {
                    zuAufgaben.add(ai);
                }
                
                if(!zuAufgaben.isEmpty()) {
                    if(rechner == null) {
                        punkteLs = new DeepThoughtPrio(thema.getModul(),heute).berechnePrioritaet(zuAufgaben);
                    } else {
                        punkteLs = rechner.berechnePrioritaet(zuAufgaben);
                    }

                    Collections.sort(zuAufgaben);

                    //Aufgaben in Datenbankliste einfuegen , alte vorher loeschen

                    //Hier auch dynamisch schauen wie viele Aufgaben ueberhaubt gemacht werden sollen. 
                    //Zuerst allerdings einfach alle moeglich in der Reihenfolge einfuegen.
                    //wer macht das? Das DAO.

                    DAO.setztZuAufgaben(ls,zuAufgaben);

                    DAO.setzeLSPunkte(ls,punkteLs);
                }
                //loeschen
                
                aufgaben.clear();
                beAufgaben.clear();
                zuAufgaben.clear();
                infos.clear();
                
            }
        }   
        if(rechner != null && rechner.sollDatenErfassen()) {
            rechner.rechnenStatistiken();
        } 
    }
    
}
