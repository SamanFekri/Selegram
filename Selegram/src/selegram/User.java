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
public class User {

    private String username, fName, lName, birthday, password, email, bio, phone, pic64;
    private boolean registered;
    private ArrayList<Friend> Friends = new ArrayList<>();
    private ArrayList<Groups> Groups = new ArrayList<>();
    private ArrayList<Channel> channels= new ArrayList<>();
    private ArrayList<Private> pchats = new ArrayList<>();
    private ArrayList<Unknown> unknowns = new ArrayList<>();

    public User(String username) {
        this.username = username;
        registered = false;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPic64() {
        return pic64;
    }

    public void setPic64(String pic64) {
        this.pic64 = pic64;
    }

    public ArrayList<Friend> getFriends() {
        return Friends;
    }

    public void setFriends(ArrayList<Friend> Friends) {
        this.Friends = Friends;
    }

    public ArrayList<Groups> getGroups() {
        return Groups;
    }

    public void setGroups(ArrayList<Groups> Groups) {
        this.Groups = Groups;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public void setChannels(ArrayList<Channel> channels) {
        this.channels = channels;
    }

    public ArrayList<Private> getPchats() {
        return pchats;
    }

    public void setPchats(ArrayList<Private> pchats) {
        this.pchats = pchats;
    }

    public ArrayList<Unknown> getUnknowns() {
        return unknowns;
    }

    public void setUnknowns(ArrayList<Unknown> unknowns) {
        this.unknowns = unknowns;
    }

   
}
