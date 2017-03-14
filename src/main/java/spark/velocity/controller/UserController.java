/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.velocity.controller;

import DBBot.AufgabenBot;
import Entitys.Aufgabe;
import Entitys.Modul;
import Entitys.Pruefungsperiode;
import Entitys.Thema;
import java.sql.Date;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import main.ChatBotManager;
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
    private static ArrayList th = new ArrayList();
    private static ArrayList periode = new ArrayList();
    private static short uniID;
    private static String name = null;;

    private static String FEHLER_DATUM = "Falsches Format, bitte geben Sie das Datum im Format:'dd.mm.yy' an!";
    private static String FEHLER_PUNKTE = "Bitte geben Sie eine ganzzahlige Punktzahl ein!";
     
    /**
     * Diese Variable beinhaltet eine Route, die neue Datensätze in die
     * Datenbank einträgt.
     */
    public static Route handleModulPost = new Route() {
        @Override
        public Object handle(Request request, Response response) throws Exception {
            
            Map<String, Object> model = new HashMap<>();
            model.put("module", ar);
            model.put("themen", th);
            ArrayList<Pruefungsperiode> per = (ArrayList) DAO.DAO.gibUni(uniID).getPruefungsperiode();

            for (Pruefungsperiode peri : per) {
                if (!periode.contains(peri.getAnfang())) {
                    periode.add(peri.getAnfang());
                }
            }
            model.put("pp", periode);
            //Fuege Modul hinzu.
            if (!getQueryModul(request).isEmpty() && !getQueryKuerzel(request).isEmpty() && !ar.contains(getQueryModul(request))) {
                if (!ar.contains(getQueryKuerzel(request))) {
                    ar.add(getQueryKuerzel(request));
                    DAO.DAO.addModul(uniID, getQueryModul(request), getQueryKuerzel(request));
                }
            }
            //Fuege einem Modul ein Thema mit Anteil hinzu.
            if (!getQueryThemaModul(request).isEmpty() && !getQueryThema(request).isEmpty() && !getQueryThemaAnteil(request).isEmpty()) {
                //Modul in DB schreiben.
                short anteil = Short.parseShort(getQueryThemaAnteil(request));
                System.out.println(getQueryKuerzel(request) + ": " + uniID + ": " + getQueryThema(request) + ": " + anteil);
                DAO.DAO.addThema(getQueryThemaModul(request), uniID, getQueryThema(request), anteil);
                ArrayList<Thema> the = (ArrayList) DAO.DAO.getThemen(uniID, getQueryThemaModul(request));
                for (Thema them : the) {
                    if (!th.contains(them.getName())) {
                        th.add(them.getName());
                    }
                }
                model.put("themen", th);
            }
            try {
                //Fuege eine Frage mit Antworten hinzu.
                ArrayList<Thema> themen = (ArrayList) DAO.DAO.getThemen(uniID, getQueryModulFrage(request));
                Thema thema = null;
                for (Thema th : themen) {
                    if (th.getName().equals(getQueryThemaFrage(request))) {
                        thema = th;
                        break;
                    }
                }
                if (thema != null && !getQueryFrage(request).equals("") && !getQueryAntwort1(request).equals("")
                        && !getQueryAntwort2(request).equals("") && !getQueryAntwort3(request).equals("")
                        && !getQueryAntwort4(request).equals("") && !getQueryPunkte(request).equals("")) {
                    Aufgabe auf = DAO.DAO.addAufgabe(thema, getQueryFrage(request), getQueryHinweis(request), getQueryVerweis(request), Integer.parseInt(getQueryPunkte(request)));
                    DAO.DAO.addAntwort(auf.getAufgabenID(), getQueryAntwort1(request), !(getQueryAntwort1Richtig(request) == null));
                    DAO.DAO.addAntwort(auf.getAufgabenID(), getQueryAntwort2(request), !(getQueryAntwort2Richtig(request) == null));
                    DAO.DAO.addAntwort(auf.getAufgabenID(), getQueryAntwort3(request), !(getQueryAntwort3Richtig(request) == null));
                    DAO.DAO.addAntwort(auf.getAufgabenID(), getQueryAntwort4(request), !(getQueryAntwort4Richtig(request) == null));

                }
            } catch (Exception e) {
                model.put("punkte", FEHLER_PUNKTE);
            }
            //Fuege eine Pruefungsperiode hinzu.
            if (!getQueryJahr(request).equals("") && !getQueryAnmeldeBeginn(request).equals("")
                    && !getQueryAnfangPP(request).equals("") && !getQueryEndePP(request).equals("")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(getQueryAnfangPP(request)));
                    Date anfang = new Date(cal.getTimeInMillis());
                    cal.setTime(sdf.parse(getQueryEndePP(request)));
                    Date ende = new Date(cal.getTimeInMillis());
                    cal.setTime(sdf.parse(getQueryAnmeldeBeginn(request)));
                    Date anmeldebeginn = new Date(cal.getTimeInMillis());
                    DAO.DAO.addPruefungsphase(uniID, Short.parseShort(getQueryJahr(request)), Short.parseShort(getQueryPhase(request)),
                            anfang, ende, anmeldebeginn);
                } catch (Exception e) {
                    model.put("datum", FEHLER_DATUM);
                }
            }

//          Fuege eine Klausur hinzu.
            ArrayList<Pruefungsperiode> pruf = (ArrayList) DAO.DAO.gibUni(uniID).getPruefungsperiode();
            Pruefungsperiode peri = null;
            for (Pruefungsperiode p : pruf) {
                if (p.getAnfang().toString().equals(getQueryPeriode(request))) {
                    peri = p;
                }
            }
            ArrayList<Modul> module1 = (ArrayList) DAO.DAO.getModule(uniID);
            Modul module2 = null;
            for (Modul m : module1) {
                if (m.getKuerzel().equals(getQueryModulKlausur(request))) {
                    module2 = m;
                }
            }
            if (!getQueryUhrzeit(request).equals("") && !getQueryDatum(request).equals("")
                    && !getQueryOrt(request).equals("") && !getQueryHilfsmittel(request).equals("")
                    && !getQueryTyp(request).equals("") && !getQueryDauer(request).equals("")) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(sdf.parse(getQueryDatum(request)));
                    Date datum = new Date(cal.getTimeInMillis());
                    LocalTime lt = LocalTime.parse(getQueryUhrzeit(request));
                    Time uhrzeit = new Time(lt.getHour(), lt.getMinute(), lt.getSecond());

                    DAO.DAO.addKlausur(peri, module2, datum, uhrzeit, Short.parseShort(getQueryDauer(request)),
                            getQueryOrt(request), getQueryHilfsmittel(request), getQueryTyp(request));
                } catch (Exception e) {
                    model.put("datum", FEHLER_DATUM);
                }
            }

            if (!getQueryModulBerechnen(request).equals(" ")) {
                ArrayList<Modul> mod = (ArrayList) DAO.DAO.getModule(uniID);
                Modul mo = null;
                for (Modul modu : mod) {
                    if (modu.getKuerzel().equals(getQueryModulBerechnen(request))) {
                        mo = modu;
                        break;
                    }
                }
                if (mo != null) {
                    ChatBotManager.getInstance().gibBotPool().berechneNeu(mo);
                }

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
            System.out.println(name);
        }
        uniID = DAO.DAO.getUniID(name);
        ArrayList<Modul> mod = (ArrayList) DAO.DAO.getModule(uniID);

        for (Modul mo : mod) {
            if (!ar.contains(mo.getKuerzel())) {
                ar.add(mo.getKuerzel());
            }
        }
        ArrayList<Thema> the = (ArrayList) DAO.DAO.getThemen(uniID, ar.get(0).toString());
        for (Thema them : the) {
            if (!th.contains(them.getName())) {
                th.add(them.getName());
            }
        }
        ArrayList<Pruefungsperiode> per = (ArrayList) DAO.DAO.gibUni(uniID).getPruefungsperiode();

        for (Pruefungsperiode peri : per) {
            if (!periode.contains(peri.getJahr())) {
                periode.add(peri.getAnfang());
            }
        }
        Map<String, Object> model = new HashMap<>();
        model.put("module", ar);
        model.put("themen", th);
        model.put("pp", periode);
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
    };
}
