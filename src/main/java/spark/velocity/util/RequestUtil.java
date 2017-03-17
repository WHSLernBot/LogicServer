package spark.velocity.util;

import spark.Request;

/**
 * Diese Klassen stellt Methoden bereit, um die Request auszulesen, welche
 * mit den Werten der Webseite gefuellt ist.
 */
public class RequestUtil {
    /**
     * Gibt den Wert für den Parameter "moduleBerechnen" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "moduleBerechnen" als String.
     */
    public static String getQueryModulBerechnen(Request request) {
        return request.queryParams("modulBerechnen");
    }

    //----------------Methoden---Klausur----------------//
    
    /**
     * Gibt den Wert für den Parameter "periode" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "periode" als String.
     */
    public static String getQueryPeriode(Request request) {
        return request.queryParams("periode");
    }
    /**
     * Gibt den Wert für den Parameter "modulKlausur" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "modulKlausur" als String.
     */
    public static String getQueryModulKlausur(Request request) {
        return request.queryParams("modulKlausur");
    }
    /**
     * Gibt den Wert für den Parameter "datum" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "datum" als String.
     */
    public static String getQueryDatum(Request request) {
        return request.queryParams("datum");
    }
    /**
     * Gibt den Wert für den Parameter "uhrzeit" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "uhrzeit" als String.
     */
    public static String getQueryUhrzeit(Request request) {
        return request.queryParams("uhrzeit");
    }
    /**
     * Gibt den Wert für den Parameter "dauer" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "dauer" als String.
     */
    public static String getQueryDauer(Request request) {
        return request.queryParams("dauer");
    }
    /**
     * Gibt den Wert für den Parameter "hilfsmittel" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "hilfsmittel" als String.
     */
    public static String getQueryHilfsmittel(Request request) {
        return request.queryParams("hilfsmittel");
    }
    /**
     * Gibt den Wert für den Parameter "ort" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "ort" als String.
     */
    public static String getQueryOrt(Request request) {
        return request.queryParams("ort");
    }
    /**
     * Gibt den Wert für den Parameter "typ" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "typ" als String.
     */
    public static String getQueryTyp(Request request) {
        return request.queryParams("typ");
    }
    
    //----------------Methoden---Pruefungsperiode----------------// 
    /**
     * Gibt den Wert für den Parameter "beginn" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "beginn" als String.
     */
    public static String getQueryAnmeldeBeginn(Request request) {
        return request.queryParams("beginn");
    }
    /**
     * Gibt den Wert für den Parameter "ende" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "ende" als String.
     */
    public static String getQueryEndePP(Request request) {
        return request.queryParams("ende");
    }
    /**
     * Gibt den Wert für den Parameter "anfang" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "anfang" als String.
     */
    public static String getQueryAnfangPP(Request request) {
        return request.queryParams("anfang");
    }
    /**
     * Gibt den Wert für den Parameter "phase" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "phase" als String.
     */
    public static String getQueryPhase(Request request) {
        return request.queryParams("phase");
    }
    /**
     * Gibt den Wert für den Parameter "jahr" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "jahr" als String.
     */
    public static String getQueryJahr(Request request) {
        return request.queryParams("jahr");
    }

    //----------------Methoden---Frage----------------//
    /**
     * Gibt den Wert für den Parameter "modulFrage" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "modulFrage" als String.
     */
    public static String getQueryModulFrage(Request request) {
        return request.queryParams("modulFrage");
    }
    /**
     * Gibt den Wert für den Parameter "themaFrage" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "themaFrage" als String.
     */
    public static String getQueryThemaFrage(Request request) {
        return request.queryParams("themaFrage");
    }
    /**
     * Gibt den Wert für den Parameter "antwort4Richtig" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "antwort4Richtig" als String.
     */
    public static String getQueryAntwort4Richtig(Request request) {
        return request.queryParams("antwort4Richtig");
    }
    /**
     * Gibt den Wert für den Parameter "antwort4" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "antwort4" als String.
     */
    public static String getQueryAntwort4(Request request) {
        return request.queryParams("antwort4");
    }
    /**
     * Gibt den Wert für den Parameter "antwort3Richtig" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "antwort3Richtig" als String.
     */
    public static String getQueryAntwort3Richtig(Request request) {
        return request.queryParams("antwort3Richtig");
    }
    /**
     * Gibt den Wert für den Parameter "antwort3" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "antwort3" als String.
     */
    public static String getQueryAntwort3(Request request) {
        return request.queryParams("antwort3");
    }
    /**
     * Gibt den Wert für den Parameter "antwort2Richtig" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "antwort2Richtig" als String.
     */
    public static String getQueryAntwort2Richtig(Request request) {
        return request.queryParams("antwort2Richtig");
    }
    /**
     * Gibt den Wert für den Parameter "antwort2" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "antwort2" als String.
     */
    public static String getQueryAntwort2(Request request) {
        return request.queryParams("antwort2");
    }
    /**
     * Gibt den Wert für den Parameter "antwort1Richtig" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "antwort1Richtig" als String.
     */
    public static String getQueryAntwort1Richtig(Request request) {
        return request.queryParams("antwort1Richtig");
    }
    /**
     * Gibt den Wert für den Parameter "antwort1" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "antwort1" als String.
     */
    public static String getQueryAntwort1(Request request) {
        return request.queryParams("antwort1");
    }
    /**
     * Gibt den Wert für den Parameter "hinweis" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "hinweis" als String.
     */
    public static String getQueryHinweis(Request request) {
        return request.queryParams("hinweis");
    }
    /**
     * Gibt den Wert für den Parameter "verweis" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "verweis" als String.
     */
    public static String getQueryVerweis(Request request) {
        return request.queryParams("verweis");
    }
    /**
     * Gibt den Wert für den Parameter "frage" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "frage" als String.
     */
    public static String getQueryFrage(Request request) {
        return request.queryParams("frage");
    }
    /**
     * Gibt den Wert für den Parameter "punkte" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "punkte" als String.
     */
    public static String getQueryPunkte(Request request) {
        return request.queryParams("punkte");
    }

    //----------------Methoden---Thema----------------//
    
    /**
     * Gibt den Wert für den Parameter "anteil" wieder.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "anteil" als String.
     */
    public static String getQueryThemaAnteil(Request request) {
        return request.queryParams("anteil");
    }
    /**
     * Gibt den Wert für den Parameter "themaModul" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "themaModul" als String.
     */
    public static String getQueryThemaModul(Request request) {
        return request.queryParams("themaModul");
    }
/**
     * Gibt den Wert für den Parameter "tkuerzel" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "tkuerzel" als String.
     */
    public static String getQueryThema(Request request) {
        return request.queryParams("tkuerzel");
    }

    //----------------Methoden---Modul----------------//
    /**
     * Gibt den Wert für den Parameter "kuerzel" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "kuerzel" als String.
     */
    public static String getQueryKuerzel(Request request) {
        return request.queryParams("kuerzel");
    }
    /**
     * Gibt den Wert für den Parameter "modul" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "modul" als String.
     */
    public static String getQueryModul(Request request) {
        return request.queryParams("modul");
    }

    //----------------Methoden---Login----------------//
    /**
     * Gibt den Wert für den Parameter "username" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "username" als String.
     */
    public static String getQueryUsername(Request request) {
        return request.queryParams("username");
    }
    /**
     * Gibt den Wert für den Parameter "password" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "password" als String.
     */
    public static String getQueryPassword(Request request) {
        return request.queryParams("password");
    }

    //----------------Methoden---Benutzer-Anlegen----------------//
    /**
     * Gibt den Wert für den Parameter "usernameAnlegen" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "usernameAnlegen" als String.
     */
    public static String getQueryUsernameAnlegen(Request request) {
        return request.queryParams("usernameAnlegen");
    }
    /**
     * Gibt den Wert für den Parameter "passwordErst" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "passwordErst" als String.
     */
    public static String getQueryPasswordErst(Request request) {
        return request.queryParams("passwordErst");
    }
    /**
     * Gibt den Wert für den Parameter "passwordWdh" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "passwortWdh" als String.
     */
    public static String getQueryPasswordWdh(Request request) {
        return request.queryParams("passwordWdh");
    }
    /**
     * Gibt den Wert für den Parameter "loginRedirect" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "loginRedirect" als String.
     */
    public static String getQueryLoginRedirect(Request request) {
        return request.queryParams("loginRedirect");
    }
    /**
     * Gibt den Wert für den Parameter "currentUser" wieder, welcher in der
     * Request gespeichert ist.
     * @param request Uebergebene Werte.
     * @return Wert des Parameters "currentUser" als String.
     */
    public static String getSessionCurrentUser(Request request) {
        return request.session().attribute("currentUser");
    }
}
