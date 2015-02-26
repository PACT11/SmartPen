/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote.controlAgent;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import remote.RemotePen;
import remote.messages.Message;
import remote.server.Server;
import remote.server.ServerClient;

/**
 *
 * @author arnaud
 */
public class KillServer extends Message {
    private String password;
    public KillServer(String password) {
        this.password = password;
    }

    @Override
    public void onServerReceive(ServerClient client) {
        try {
            if(password.equals("pipotron")) {
                Server.shutdown();
                System.exit(0);
            }
        } catch (IOException ex) {
            Logger.getLogger(KillServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onClientReceive(RemotePen client) {
    }
    
}
