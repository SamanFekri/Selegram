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
public class Groups {
    public String name;
    public String chatid;
    public boolean mentioned;
    public ArrayList<String> participants;

    public Groups(String name, String chatid, ArrayList<String> participants, boolean mentioned) {
        this.name = name;
        this.chatid = chatid;
        this.mentioned = mentioned;
        this.participants = participants;
    }

    public Groups(String name, String chatid) {
        this.name = name;
        this.chatid = chatid;
        this.participants = new ArrayList<>();
    }
    
    
}
