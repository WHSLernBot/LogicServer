/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.velocity.controller;

/**
 *
 * @author Sebastian
 */
public class LoginController {
    public static boolean authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
        short uni = DAO.DAO.getUniID(username);
        return DAO.DAO.pruefePassword(uni, password) && DAO.DAO.getUsername(uni).equals(username);
    }
}
