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
 *
 * @author Seve
 */
public class AufgabenBot implements Runnable {

    private final CBBenutzer benutzer;
    
    private final Modul modul;
    
    private final Timestamp heute;
    
    public AufgabenBot(CBBenutzer benutzer) {
        this.benutzer = benutzer;
        this.modul = null;
        benutzer.gain();
        heute = ChatBotManager.getInstance().jetzt();
    }
     
    public AufgabenBot(Modul modul) {
        this.benutzer = null;
        this.modul = modul;
        
        heute = ChatBotManager.getInstance().jetzt();
    }
    
    @Override
    public void run() {
        
        Collection<LernStatus> stadi;
        
        if(modul == null) {
            stadi = benutzer.getBenutzer().getLernStadi();
            berechne(stadi);
        } else {
            for(Thema t : modul.getThemen()) {
                stadi = t.getLernStadi();
                berechne(stadi);
            }
        }  
        
        if(benutzer != null) {
            //evet. hier nachricht an den Benutzer senden
            benutzer.release();
        }
    }
    
    private void berechne(Collection<LernStatus> stadi) {
        Collection<Aufgabe> aufgaben;
        
        Collection<BeAufgabe> beAufgaben;
        
        List<lsItem> zuAufgaben = new LinkedList<>();
        
        HashMap<Long,lsItem> infos = new HashMap<>();
        
        Thema thema;
        
        for(LernStatus ls : stadi) {
            // Gucken ob man neu berechnen muss
            
            if(ls.istAktiv() && ls.isVeraendert()) { //Nochmal drüber nachdenken
                
                thema = ls.getThema();

                aufgaben = thema.getAufgaben();
                // Alle Aufgaben holen 
                for(Aufgabe a : aufgaben) {
                    
                    infos.put(a.getAufgabenID(), new lsItem(a));             
                    
                }
                
                beAufgaben = ls.getBeAufgaben();
                
                //Zu allen  Aufgaben mappen wann zuletzt und wie oft insgesamt beantwortet
                for(BeAufgabe b : beAufgaben) {
                    if(b.istBeantwortet()) {
                        long id = b.getAufgabe().getAufgabenID();
                        lsItem item = infos.get(id);
                        
                        item.beantwortet(b.istRichtig());
                        
                        item.hinweis(b.getHinweis());
                        
                        item.preufeDatum(b.getDatum());    
                    }                   
                }

                // Nicht zu bearbeitende Aufgaben streichen
                
                for(long id : infos.keySet()) {
                    
                    lsItem item = infos.get(id);
                    
                    item.berechnePrio(heute);
                    if(item.gibPrio() != 0) {
                        zuAufgaben.add(item);
                    }
                    
                }

                //Aufgaben nach priorität sortieren

                Collections.sort(zuAufgaben);
                
                //Aufgaben in Datenbankliste einfügen , alte vorher löschen
                
                //Hier auch dynamisch schauen wie viele Aufgaben überhaubt gemacht werden sollen. 
                //Zuerst allerdings einfach alle möglich in der Reihenfolge einfügen.
                //wer macht das? Das DAO.
                    
                DAO.setztZuAufgaben(ls,zuAufgaben);
                
                //loeschen
                
                aufgaben.clear();
                beAufgaben.clear();
                zuAufgaben.clear();
                infos.clear();
                
            }
        }    
    }
    
}
