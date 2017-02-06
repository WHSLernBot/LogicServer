package spark.velocity.controller;

import java.util.HashMap;
import java.util.Map;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Route;
import static spark.velocity.util.RequestUtil.*;
import spark.velocity.util.Path;
import spark.velocity.util.VelocityTemplateEngine;

/**
 *
 * @author Sebastian
 */
public class IndexController {

    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();
        
//        model.put("loggedOut", removeSessionAttrLoggedOut(request));
//        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_INDEX));
    };

    public static Route handleLoginPost = (Request request, Response response) -> {
        if(request == null){
            System.out.println("null");
        }
        Map<String, Object> model = new HashMap<>();
        if(!UserController.authenticate(getQueryUsername(request), getQueryPassword(request))){
            model.put("authenticationFailed", true);
            return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_INDEX));
        }
        
        model.put("authenticationSucceeded", true);
        request.session().attribute("currentUser", getQueryUsername(request));
        if (getQueryLoginRedirect(request) != null) {
            response.redirect(getQueryLoginRedirect(request));
        }
        if(getQueryUsername(request).equals("admin")) {
            return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_ADMIN));
        }
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_USER));
    };
}
