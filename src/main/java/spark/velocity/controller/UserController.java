/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spark.velocity.controller;

import Entitys.Uni;

/**
 *
 * @author Sebastian
 */
public class UserController {
    public static boolean authenticate(String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            return false;
        }
//        Uni user = DAO.DAO.gibUni((short)0);
        String user = "admin";
        String pw = "123";
        String user1 = "whs";
        String pw1  = "111";
//        if (user == null) {
//            return false;
//        }
        return user.equals(username) && pw.equals(password)
                || user1.equals(username) && pw1.equals(password);
    }
}
