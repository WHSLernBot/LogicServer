/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.velocity.controller;

import Entitys.Aufgabe;
import Entitys.Modul;
import Entitys.Pruefungsperiode;
import Entitys.Thema;
import java.sql.Date;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.velocity.util.Path;
import static spark.velocity.util.RequestUtil.*;
import spark.velocity.util.VelocityTemplateEngine;

/**
 *
 * @author Sebastian
 */
public class UserController {

    private static ArrayList ar = new ArrayList();
    private static HashMap hm = new HashMap();
    private static short uniID;
    private static String name = null;
    /**
     * Diese Variable beinhaltet eine Route, die neue Datensätze in die
     * Datenbank einträgt.
     */
    public static Route handleModulPost = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            if (request == null) {
                System.out.println("null");
            }
            ar = (ArrayList) DAO.DAO.getModule(uniID);
            Map<String, Object> model = new HashMap<>();
            model.put("module", ar);

            //Fuege Modul hinzu.
            if (!getQueryModul(request).isEmpty() && !getQueryKuerzel(request).isEmpty() && !ar.contains(getQueryModul(request))) {
                ar.add(getQueryKuerzel(request));
                DAO.DAO.addModul(uniID, getQueryModul(request), getQueryKuerzel(request));
            }
            //Fuege einem Modul ein Thema mit Anteil hinzu.
            if (!getQueryThemaModul(request).isEmpty() && !getQueryThema(request).isEmpty() && !getQueryThemaAnteil(request).isEmpty()) {
                //Modul in DB schreiben.
                short anteil = Short.parseShort(getQueryThemaAnteil(request));
                DAO.DAO.addThema(getQueryKuerzel(request), uniID, getQueryThema(request), anteil);
                model.put("themen", DAO.DAO.getThemen(uniID, getQueryThemaModul(request)));
            }
            //Fuege eine Frage mit Antworten hinzu.
            HashMap th = (HashMap) DAO.DAO.getThemen(uniID, name);
            Thema thema = (Thema) th.get(getQueryThema(request));
            if (!getQueryFrage(request).equals("") && !getQueryAntwort1(request).equals("")
                    && !getQueryAntwort2(request).equals("") && !getQueryAntwort3(request).equals("")
                    && !getQueryAntwort4(request).equals("") && !getQueryPunkte(request).equals("")) {
                Aufgabe auf = DAO.DAO.addAufgabe(thema, getQueryFrage(request), getQueryHinweis(request), getQueryVerweis(request), Integer.parseInt(getQueryPunkte(request)));
                DAO.DAO.addAntwort(auf.getAufgabenID(), getQueryAntwort1(request), !(getQueryAntwort1Richtig(request) == null));
                DAO.DAO.addAntwort(auf.getAufgabenID(), getQueryAntwort2(request), !(getQueryAntwort2Richtig(request) == null));
                DAO.DAO.addAntwort(auf.getAufgabenID(), getQueryAntwort3(request), !(getQueryAntwort3Richtig(request) == null));
                DAO.DAO.addAntwort(auf.getAufgabenID(), getQueryAntwort4(request), !(getQueryAntwort4Richtig(request) == null));
            }
            //Fuege eine Pruefungsperiode hinzu.
            if (!getQueryJahr(request).equals("") && !getQueryAnmeldeBeginn(request).equals("")
                    && !getQueryAnfangPP(request).equals("") && !getQueryEndePP(request).equals("")) {
                DateFormat format = new SimpleDateFormat("MMMM d, yyyy");
                Date date = (Date) format.parse(getQueryAnfangPP(request));
                Date anfang = new Date(date.getDay(), date.getMonth(), date.getYear());
                date = (Date) format.parse(getQueryEndePP(request));
                Date ende = new Date(date.getDay(), date.getMonth(), date.getYear());
                date = (Date) format.parse(getQueryAnmeldeBeginn(request));
                Date anmeldebeginn = new Date(date.getDay(), date.getMonth(), date.getYear());                
                DAO.DAO.addPruefungsphase(uniID, Short.parseShort(getQueryJahr(request)), Short.parseShort(getQueryPhase(request)), anfang, ende, anmeldebeginn);
            }
            HashMap<String,Pruefungsperiode> per = (HashMap) DAO.DAO.gibUni(uniID).getPruefungsperiode();
            model.put("pp",per);
            // Fuege eine Klausur hinzu.
            if(!getQueryUhrzeit(request).equals("") && !getQueryDatum(request).equals("")
                    && !getQueryOrt(request).equals("") && !getQueryHilfsmittel(request).equals("")
                    && !getQueryTyp(request).equals("") && !getQueryDauer(request).equals("")) {
            DateFormat format = new SimpleDateFormat("MMMM d, yyyy");
            Date datum = (Date) format.parse(getQueryDatum(request));
            Time uhrzeit = new Time(format.parse(getQueryUhrzeit(request)).getTime());
            HashMap<String,Modul> hm =  (HashMap) DAO.DAO.getModule(uniID);
            
            DAO.DAO.addKlausur(per.get(getQueryPeriode(request)),hm.get(getQueryModulKlausur(request))
                    , datum, uhrzeit, Short.parseShort(getQueryDauer(request))
                    , getQueryOrt(request), getQueryHilfsmittel(request), getQueryTyp(request));
            
            }
            return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
        }
    };
    /**
     * Diese Variable beinhaltet eine Route, die die Userseite rendert.
     */
    public static Route serveUserPage = (Request request, Response response) -> {
        if (request == null) {
            System.out.println("null");
        }
        if (name == null) {
            name = getQueryUsername(request);
            uniID = DAO.DAO.getUniID(getQueryUsername(request));
        }
        ar = (ArrayList) DAO.DAO.getModule(uniID);
        Map<String, Object> model = new HashMap<>();
        model.put("module", ar);
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
    };

}
