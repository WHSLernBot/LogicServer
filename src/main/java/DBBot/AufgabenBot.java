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
    
    private void berechne(Collection<LernStatus> stadi, DeepThoughtPrio rechner) {
        Collection<Aufgabe> aufgaben;
        
        Collection<BeAufgabe> beAufgaben;
        
        List<aufgabenItem> zuAufgaben = new LinkedList<>();
        
        HashMap<Long,aufgabenItem> infos = new HashMap<>();
        
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
                    
                    infos.put(a.getAufgabenID(), new aufgabenItem(a));             
                    
                }
                
                beAufgaben = ls.getBeAufgaben();
                
                //Zu allen  Aufgaben mappen wann zuletzt und wie oft insgesamt beantwortet
                for(BeAufgabe b : beAufgaben) {
                    if(b.istBeantwortet()) {
                        long id = b.getAufgabe().getAufgabenID();
                        aufgabenItem item = infos.get(id);
                        
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
                
                DAO.setzteLSPunkte(ls,punkteLs);
                
                //loeschen
                
                aufgaben.clear();
                beAufgaben.clear();
                zuAufgaben.clear();
                infos.clear();
                
            }
        }    
    }
    
}
