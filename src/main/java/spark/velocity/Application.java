/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.velocity;

import spark.Spark;
import static spark.Spark.*;
import spark.velocity.controller.IndexController;
import spark.velocity.util.Path;

public class Application {
    public static void main(String[] args) {
        Spark.staticFileLocation("/public");
        get(Path.W_INDEX , IndexController.serveIndexPage);
        post(Path.W_INDEX, IndexController.handleLoginPost);
    }
}
