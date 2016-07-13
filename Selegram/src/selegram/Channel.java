/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selegram;

import java.util.ArrayList;

/**
 *
 * @author SKings
 */
public class Channel {
    public String name;
    public String chatid;
    public ArrayList<String> participants;
    public String admin;

    public Channel(String name, String chatid, ArrayList<String> participants,String admin) {
        this.name = name;
        this.chatid = chatid;
        this.participants = participants;
        this.admin = admin;
    }

    public Channel(String name, String chatid, String admin) {
        this.name = name;
        this.chatid = chatid;
        this.participants = new ArrayList<>();
        this.admin = admin;
    }
}
