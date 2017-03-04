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
    /**
     * Diese Variable beinhaltet eine Route, die die Indexseite rendert.
     */
    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();       
        model.put("loggedOut", removeSessionAttrLoggedOut(request));
        model.put("loginRedirect", removeSessionAttrLoginRedirect(request));
        System.out.println("IndexPage");
        model.put("currentUser", getQueryUsername(request));
        return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_INDEX));       
    };
    /**
     * Diese Variable beinhaltet eine Route, die einen Post zum 
     * Login des Benutzers verwaltet.
     */
    public static Route handleLoginPost = (Request request, Response response) -> {
        if(request == null){
            System.out.println("null");
        }
        Map<String, Object> model = new HashMap<>();
        if(!LoginController.authenticate(getQueryUsername(request), getQueryPassword(request))){
            model.put("authenticationFailed", true);            
            return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_INDEX));
        }
        
        model.put("authenticationSucceeded", true);       
        request.session().attribute("currentUser", getQueryUsername(request));
        if (getQueryLoginRedirect(request) != null) {
            response.redirect(getQueryLoginRedirect(request));
        }
        // Ist  der Benutzername "admin" wird er zur Adminseite weiter geleitet.
        if(getQueryUsername(request).equals("admin")) {
            response.redirect("/admin");
            model.put("adminLogin", true);
            return AdminController.serveAdminPage.handle(request, response);
        }
        // Wird zur Benutzerseite weitergeleitet.
        response.redirect("/user");
        model.put("userLogin", true);
        return UserController.serveUserPage.handle(request, response);
    };
}
