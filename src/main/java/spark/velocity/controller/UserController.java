/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.velocity.controller;

import Entitys.Aufgabe;
import Entitys.Thema;
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

    public static Route handleModulPost = (Request request, Response response) -> {
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

        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
    };

    public static Route serveUserPage = (Request request, Response response) -> {
        if (request == null) {
            System.out.println("null");
        }
        if (name == null) {
            name = getQueryUsername(request);
            uniID = DAO.DAO.getUniID(getQueryUsername(request));
        }
        ar = (ArrayList) DAO.DAO.getModule(uniID);

        ar.add("modul1");
        ar.add("modul2");
        ar.add("modul3");
        ar.add("modul4");
        ar.add("modul5");
        Map<String, Object> model = new HashMap<>();
        model.put("module", ar);
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
    };

}
