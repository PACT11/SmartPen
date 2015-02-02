
package remote.messages;

import remote.RemotePen;

/*
 * Message qui represente une commande
 */
public class Command extends ServerPassiveMessage {
    private String command;
    
    public Command(String cmd) {
        command = cmd;
    }
    
    @Override
    public void onClientReceive(RemotePen client) {
        
    }
}
