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
    public static String getQueryLocale(Request request) {
        return request.queryParams("locale");
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

    public static String getSessionLocale(Request request) {
        return request.session().attribute("locale");
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
