/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.blacklist.blacklistvalidator;

import java.util.List;

/**
 *
 * @author hcadavid
 */
public class Main {
    /**
     * Funcion principal
     * @param args
     */
    public static void main(String args[]){
        HostBlackListsValidator hblv=new HostBlackListsValidator();
        List<Integer> blackListOcurrences = null;
        try {
            blackListOcurrences=hblv.checkHost("200.24.34.55",50);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("The host was found in the following blacklists:"+blackListOcurrences);
        
    }
    
}