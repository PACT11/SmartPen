/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author arnaud
 */
public class ConnectionMaker extends Thread {
    private ServerSocket connectionSocket;
    private boolean running;
    
    public ConnectionMaker(ServerSocket socket) {
        connectionSocket = socket;
    }
    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                Socket newSocket = connectionSocket.accept();
                ServerClient.add(newSocket);
                System.out.println("Server : new client connected !");
            } catch (IOException ex) {
                if(connectionSocket.isClosed()) {
                    System.out.println("Server : stop connection making");
                } else {
                    System.err.println("Server : failed in creating a connection");
                }
            } 
        }
    }
    public void close() {
        running = false;
    }
}
