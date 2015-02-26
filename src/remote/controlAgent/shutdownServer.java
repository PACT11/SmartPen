/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote.controlAgent;

import remote.RemotePen;

/**
 *
 * @author arnaud
 */
public class shutdownServer {
    public static void main(String[] args) throws InterruptedException {
        RemotePen pen = new RemotePen("controlAgent");
        pen.connect();
        pen.sendMessage(new KillServer("pipotron"));
        Thread.sleep(500);
        pen.close();
    }
}
