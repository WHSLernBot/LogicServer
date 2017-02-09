/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.velocity.controller;

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
public class AdminController {
    public static Route handleRegPost = (Request request, Response response) -> {
        if(request == null){
            System.out.println("null");
        }
        Map<String, Object> model = new HashMap<>();
        
        if(getQueryUsernameAnlegen(request) == null
                || getQueryUsernameAnlegen(request).equals("")){
            System.out.println("Fehler");
        }
        if(getQueryPasswordErst(request).equals(getQueryPasswordWdh(request))){
            System.out.println("DAO einfügen");
        }
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_ADMIN));
    };
    public static Route serveAdminPage= (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_ADMIN));
    };
}
