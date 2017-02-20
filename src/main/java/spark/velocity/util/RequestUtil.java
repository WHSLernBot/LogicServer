/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.velocity.util;

import spark.Request;

/**
 *
 * @author Sebastian
 */
public class RequestUtil {
    public static String getQueryModulFrage(Request request) {
        return request.queryParams("modulFrage");
    }
    public static String getQueryThemaFrage(Request request) {
        return request.queryParams("themaFrage");
    }
    public static String getQueryAntwort4Richtig(Request request) {
        return request.queryParams("antwort4Richtig");
    }
    public static String getQueryAntwort4(Request request) {
        return request.queryParams("antwort4");
    }
    public static String getQueryAntwort3Richtig(Request request) {
        return request.queryParams("antwort3Richtig");
    }
    public static String getQueryAntwort3(Request request) {
        return request.queryParams("antwort3");
    }
    public static String getQueryAntwort2Richtig(Request request) {
        return request.queryParams("antwort2Richtig");
    }
    public static String getQueryAntwort2(Request request) {
        return request.queryParams("antwort2");
    }
    public static String getQueryAntwort1Richtig(Request request) {
        return request.queryParams("antwort1Richtig");
    }
    public static String getQueryAntwort1(Request request) {
        return request.queryParams("antwort1");
    }
    public static String getQueryHinweis(Request request) {
        return request.queryParams("hinweis");
    }
    public static String getQueryVerweis(Request request) {
        return request.queryParams("verweis");
    }
    public static String getQueryFrage(Request request) {
        return request.queryParams("frage");
    }
    public static String getQueryPunkte(Request request) {
        return request.queryParams("punkte");
    }
    public static String getQueryThemaAnteil(Request request) {
        return request.queryParams("anteil");
    }
    public static String getQueryModul(Request request) {
        return request.queryParams("modul");
    }
    public static String getQueryThemaModul(Request request) {
        return request.queryParams("themaModul");
    }
    public static String getQueryKuerzel(Request request) {
        return request.queryParams("kuerzel");
    }
    public static String getQueryThema(Request request) {
        return request.queryParams("tkuerzel");
    }
    public static String getQueryUsername(Request request) {
        return request.queryParams("username");
    }
    public static String getQueryUsernameAnlegen(Request request) {
        return request.queryParams("usernameAnlegen");
    }
    public static String getQueryPassword(Request request) {
        return request.queryParams("password");
    }
    public static String getQueryPasswordErst(Request request) {
        return request.queryParams("passwordErst");
    }
    public static String getQueryPasswordWdh(Request request) {
        return request.queryParams("passwordWdh");
    }
    public static String getQueryLoginRedirect(Request request) {
        return request.queryParams("loginRedirect");
    }
    
    public static String getSessionCurrentUser(Request request) {
        return request.session().attribute("currentUser");
    }

    public static boolean removeSessionAttrLoggedOut(Request request) {
        Object loggedOut = request.session().attribute("loggedOut");
        request.session().removeAttribute("loggedOut");
        return loggedOut != null;
    }

    public static String removeSessionAttrLoginRedirect(Request request) {
        String loginRedirect = request.session().attribute("loginRedirect");
        request.session().removeAttribute("loginRedirect");
        return loginRedirect;
    }

    public static boolean clientAcceptsHtml(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("text/html");
    }

    public static boolean clientAcceptsJson(Request request) {
        String accept = request.headers("Accept");
        return accept != null && accept.contains("application/json");
    }
}
