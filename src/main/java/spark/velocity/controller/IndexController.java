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
    
    private static final String MODEL_FAILED = "authenticationFailed";
    private static final String MODEL_SUCCESS = "authenticationSucceeded";
    private static final String ADMIN = "/admin";
    private static final String ADMIN_LOGIN = "adminLogin";
    private static final String USER = "/user";
    private static final String USER_LOGIN = "userLogin";
    private static final String CURRENT_USER = "currentUser";

    
    /**
     * Diese Variable beinhaltet eine Route, die die Indexseite rendert.
     */
    public static Route serveIndexPage = (Request request, Response response) -> {
        Map<String, Object> model = new HashMap<>();               
        model.put(CURRENT_USER, getQueryUsername(request));
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
            model.put(MODEL_FAILED, true);
            return new VelocityTemplateEngine().render(new ModelAndView(model, Path.T_INDEX));
        }
        
        model.put(MODEL_SUCCESS, true);       
        request.session().attribute(CURRENT_USER, getQueryUsername(request));
        if (getQueryLoginRedirect(request) != null) {
            response.redirect(getQueryLoginRedirect(request));
        }
        // Ist  der Benutzername "admin" wird er zur Adminseite weiter geleitet.
        if(getQueryUsername(request).equals("admin")) {
            response.redirect(ADMIN);
            model.put(ADMIN_LOGIN, true);
            return AdminController.serveAdminPage.handle(request, response);
        }
        // Wird zur Benutzerseite weitergeleitet.
        response.redirect(USER);
        model.put(USER_LOGIN, true);
        return UserController.serveUserPage.handle(request, response);
    };
}
