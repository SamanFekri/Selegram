/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net;

/**
 *
 * @author SKings
 */
public class ClientHandler {
    private String IP;
    private int port;

    public ClientHandler(String IP, int port) {
        this.IP = IP;
        this.port = port;
    }
    
    public String getAnswer(String request){
        request = addNewlineToRequest(request);
        Client client = new Client(request, IP, port);
        return client.getAnswer();
    }
    
    public String addNewlineToRequest(String text){
        return text+="\n";
    }
}
