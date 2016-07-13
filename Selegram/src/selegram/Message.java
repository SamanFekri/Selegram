/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selegram;

import java.util.Date;

/**
 *
 * @author SKings
 */
public class Message {
    private String username;
    private String text;
    private Date date;
    
    public Message(String username, String text, Date date) {
        this.username = username;
        this.text = text;
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public String getText() {
        return text;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    
}
