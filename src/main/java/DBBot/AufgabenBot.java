package DBBot;

import DAO.DAO;
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
        
        heute = ChatBotManager.getInstance().jetzt();
    }
    
    /**
     * Startet den Bot.
     */
    @Override
    public void run() {
        
        Collection<LernStatus> stadi;
        
        if(modul == null) {
            stadi = benutzer.getBenutzer().getLernStadi();
            berechne(stadi,null);
        } else {
            for(Thema t : modul.getThemen()) {
                stadi = t.getLernStadi();
                berechne(stadi,new DeepThoughtPrio(modul,heute));
            }
        }  
        
        if(benutzer != null) {
            //evet. hier nachricht an den Benutzer senden
            benutzer.release();
        }
    }
    
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
        
        List<AufgabenItem> zuAufgaben = new LinkedList<>();
        
        HashMap<Long,AufgabenItem> infos = new HashMap<>();
        
        Thema thema;
        
        int sumPunkte = 0;
        
        for(LernStatus ls : stadi) {
            // Gucken ob man neu berechnen muss
            
            if(ls.istAktiv() && ls.isVeraendert()) {
                
                thema = ls.getThema();

                aufgaben = thema.getAufgaben();
                // Alle Aufgaben holen 
                for(Aufgabe a : aufgaben) {
                    sumPunkte += a.getPunkte();
                    
                    infos.put(a.getAufgabenID(), new AufgabenItem(a));             
                    
                }
                
                beAufgaben = ls.getBeAufgaben();
                
                //Zu allen  Aufgaben mappen wann zuletzt und wie oft insgesamt beantwortet
                for(BeAufgabe b : beAufgaben) {
                    if(b.istBeantwortet()) {
                        long id = b.getAufgabe().getAufgabenID();
                        AufgabenItem item = infos.get(id);
                        
                        item.addAntwort(b.getDatum(), b.istRichtig(), b.getHinweis());
                    }                   
                }

                //Aufgaben nach prioritaet sortieren
                
                int punkteLs;
                
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
                
                //loeschen
                
                aufgaben.clear();
                beAufgaben.clear();
                zuAufgaben.clear();
                infos.clear();
                
            }
        }    
    }
    
}
