/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.velocity.controller;

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
    public static Route handleModulPost = (Request request, Response response) -> {      
        if(request == null){
            System.out.println("null");
        }
        Map<String, Object> model = new HashMap<>();
        ArrayList ar = new ArrayList();
        ar.add("modul1");
        ar.add("modul2");
        ar.add("modul3");
        ar.add("modul4");
        ar.add("modul5");
        model.put("module", ar);
        if(getQueryModul(request) != null && getQueryKuerzel(request) != null){
            //Modul in DB schreiben.
            System.out.println(getQueryModul(request));
        }
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
    };
    
    public static Route handleThemaPost = (Request request, Response response) -> {      
        if(request == null){
            System.out.println("null");
        }
        System.out.println(getQueryThemaModul(request) + ": " + getQueryThema(request));
        Map<String, Object> model = new HashMap<>();
        if(getQueryThema(request) != null && getQueryThemaModul(request) != null){
            //Modul in DB schreiben.
            System.out.println(getQueryThemaModul(request) + ": " + getQueryThema(request));
        }
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
    };
    public static Route handleFragePost = (Request request, Response response) -> {      
        if(request == null){
            System.out.println("null");
        }
        Map<String, Object> model = new HashMap<>();
        ArrayList ar = new ArrayList();
        ar.add("modul1");
        ar.add("modul2");
        ar.add("modul3");
        ar.add("modul4");
        ar.add("modul5");
        model.put("module", ar);
        if(getQueryThema(request) != null && getQueryModul(request) != null){
            //Modul in DB schreiben.
            System.out.println(getQueryModul(request) + ": " + getQueryThema(request));
        }
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
    };
    public static Route serveUserPage= (Request request, Response response) -> {      
        if(request == null){
            System.out.println("null");
        }
        Map<String, Object> model = new HashMap<>();
        ArrayList ar = new ArrayList();
        ar.add("modul1");
        ar.add("modul2");
        ar.add("modul3");
        ar.add("modul4");
        ar.add("modul5");
        model.put("module", ar);
        
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
    };

}
