/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package selegram;

/**
 *
 * @author SKings
 */
public class CurrentChat {
    public String chatid,chatName,chatType,chatAdmin;
    public int numberMsg,selfdest;

    public CurrentChat(String chatid, String chatName, String chatType, int numberMsg) {
        this.chatid = chatid;
        this.chatName = chatName;
        this.chatType = chatType;
        this.numberMsg = numberMsg;
        this.selfdest = 0;
        this.chatAdmin = "";
    }

    public CurrentChat(String chatid, String chatName, String chatType, int numberMsg, int selfdest,String chatAdmin) {
        this.chatid = chatid;
        this.chatName = chatName;
        this.chatType = chatType;
        this.numberMsg = numberMsg;
        this.selfdest = selfdest;
        this.chatAdmin = chatAdmin;
    }
}
