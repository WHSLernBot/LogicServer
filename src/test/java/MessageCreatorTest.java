///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//
//import Entitys.Aufgabe;
//import Entitys.Klausur;
//import Entitys.Modul;
//import Entitys.Pruefungsperiode;
//import Entitys.Thema;
//import Entitys.Uni;
//import Message.MessageCreator;
//import com.google.gson.JsonObject;
//import java.sql.Date;
//import java.sql.Time;
//import java.util.ArrayList;
//import java.util.GregorianCalendar;
//
////import static org.junit.Assert.*;
////import org.junit.Test;
//
//
///**
// *
// * @author Sebastian
// */
//public class MessageCreatorTest {
// 
//    private final JsonObject jErwarteteAufgabe1;
//    private final JsonObject jErwarteteAufgabe2;
//    private final JsonObject jErwarteteAufgabe3;
//    
//    private final  JsonObject jErwarteteUni1;
//    
//    private final  JsonObject jErwarteteKlausur1;
//    
//    private ArrayList<Uni> unis = new ArrayList<>();
//    
//    public MessageCreatorTest() {
//        jErwarteteAufgabe1 = new JsonObject();
//        jErwarteteAufgabe2 = new JsonObject();
//        jErwarteteAufgabe3 = new JsonObject();
//        
//        jErwarteteUni1 = new JsonObject();
//
//        jErwarteteKlausur1 = new JsonObject();        
//        
//        erzeugeErwarteteAufgaben();
//        erzeugeErwarteteUnis();
//        erzeugeErwarteteKlausur();
//    }
//    
//    private void erzeugeErwarteteAufgaben(){
//        JsonObject jUser = new JsonObject();
//        JsonObject jAufgabe = new JsonObject();        
//        jUser.addProperty("id", 510);
//        jUser.addProperty("plattform", 2);
//        
//        jAufgabe.addProperty("frage", "Wie heißen die fünf Media Typen in CSS?");
//        jAufgabe.addProperty("hinweis", "Bildschirm");
//        jAufgabe.addProperty("verweis", "Skript Seite 10 in CSS");
//        
//        jErwarteteAufgabe1.add("user", jUser);
//        jErwarteteAufgabe1.add("aufgabe",jAufgabe);
//        jErwarteteAufgabe1.addProperty("nachricht", "Hier ist eine neue Aufage: ");
//        
//        
//        jUser = new JsonObject();
//        jAufgabe = new JsonObject();  
//        jUser.addProperty("id", 50);
//        jUser.addProperty("plattform", 1);
//        
//        jAufgabe.addProperty("frage", "Was ist eine DTD?");
//        jAufgabe.addProperty("hinweis", "XML");
//        jAufgabe.addProperty("verweis", "Skript Seite 124 in XML");
//        
//        jErwarteteAufgabe2.add("user", jUser);
//        jErwarteteAufgabe2.add("aufgabe",jAufgabe);
//        jErwarteteAufgabe2.addProperty("nachricht", "Hier ist eine neue Aufage: ");
//        
//        
//        jUser = new JsonObject();
//        jAufgabe = new JsonObject();  
//        jUser.addProperty("id", 1);
//        jUser.addProperty("plattform", 4);
//        
//        jAufgabe.addProperty("frage", "Wofür ist das XYZ Tag in HTML verantwortlich?");
//        jAufgabe.addProperty("hinweis", "HTML");
//        jAufgabe.addProperty("verweis", "Skript Seite 23 in HTML");
//        
//        jErwarteteAufgabe3.add("user", jUser);
//        jErwarteteAufgabe3.add("aufgabe",jAufgabe);
//        jErwarteteAufgabe3.addProperty("nachricht", "Hier ist eine neue Aufage: ");
//        
//    }
//    
//    private void erzeugeErwarteteUnis(){
//        JsonObject jUni = new JsonObject();
//        
//        JsonObject jUser = new JsonObject();
//        
//        
//        jUser.addProperty("id", 100);
//        jUser.addProperty("plattform", 1);
//        
//        unis.add(new Uni("whs"));
//        unis.add(new Uni("whs1"));
//        unis.add(new Uni("whs2"));
//        unis.add(new Uni("whs3"));
//        int i = 0;
//        for (Uni name : unis) {
//            jUni.addProperty("name" + i, name.getName());
//            //this.jUni.addProperty("nameid", name.getId());
//            i++;
//        }
//        
//        this.jErwarteteUni1.add("user", jUser);
//        this.jErwarteteUni1.add("uni", jUni);
//        this.jErwarteteUni1.addProperty("nachricht", "Das sind alle verfügbaren Uni's");
//        
//    }
//    
//    private void erzeugeErwarteteKlausur() {
//        JsonObject jUser = new JsonObject();
//        JsonObject jKlausurinfo = new JsonObject();
//        
//        
//        jUser.addProperty("id", 158);
//        jUser.addProperty("plattform", 3);
//        
//        jKlausurinfo.addProperty("hilfsmittel","Keine Hilfsmittel");
//        jKlausurinfo.addProperty("ort","A4.1.04");
//        
//        this.jErwarteteKlausur1.add("user", jUser);
//        this.jErwarteteKlausur1.add("klausurinfo", jKlausurinfo);     
//        this.jErwarteteKlausur1.addProperty("nachricht", "Hier hast du die gewünschten Info's!");
//    }
//    
//    
//    @Test
//    public void erzeugeAufgabeJsonTest(){
//        MessageCreator mc = new MessageCreator();
//       
//        assertEquals(jErwarteteAufgabe1, mc.erstelleAufgabenJson(510, 2, new Aufgabe(
//                new Thema(
//                        new Modul(
//                                new Uni("uni")
//                                ,"modul1","modul2")
//                        ,"",1)
//                ,"Wie heißen die fünf Media Typen in CSS?",3,"Bildschirm","Skript Seite 10 in CSS")));
//        
//        assertEquals(jErwarteteAufgabe2, mc.erstelleAufgabenJson(50, 1, new Aufgabe(
//                new Thema(
//                        new Modul(
//                                new Uni("uni")
//                                ,"modul1","modul2")
//                        ,"",1)
//                ,"Was ist eine DTD?",3,"XML","Skript Seite 124 in XML")));
//        
//        assertEquals(jErwarteteAufgabe3, mc.erstelleAufgabenJson(1, 4, new Aufgabe(
//                new Thema(
//                        new Modul(
//                                new Uni("uni")
//                                ,"modul1","modul2")
//                        ,"",1)
//                ,"Wofür ist das XYZ Tag in HTML verantwortlich?",3,"HTML","Skript Seite 23 in HTML")));
//        
//    }
//
//    @Test
//    public void erzeugeUniJsonTest(){
//        MessageCreator mc = new MessageCreator();
//        assertEquals(this.jErwarteteUni1, mc.erstelleUniJason(100, 1, unis));
//    }
//    
//    @Test
//    public void erzeugeKlausurInfoJsonTest(){
//        GregorianCalendar cal = new GregorianCalendar();
//        Date date = new java.sql.Date(2016,4, 23);
//        Time time = new Time(3,2,1);
//        MessageCreator mc = new MessageCreator();
//        assertEquals(this.jErwarteteKlausur1, mc.erstelleKlausurInfoJson(158, 3
//                , new Klausur(new Modul(
//                                new Uni("uni")
//                                ,"modul1","modul2")
//                ,new Pruefungsperiode(new Uni("uni"),2016,3,date,date,date),date,time,time,"A4.1.04","Keine Hilfsmittel")));
//    }
//
//    
//}
