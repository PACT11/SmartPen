
package remote.messages;

import netzwerk.messages.ServerPassiveMessage;
import netzwerk.Connector;

/*
 * Message qui represente une commande
 */
public class Command extends ServerPassiveMessage {
    private String command;
    private static remote.listeners.CommandReceiveListener listener;
    public Command(String cmd) {
        command = cmd;
    }
    
    @Override
    public void onClientReceive(Connector client) {
        if(listener != null)
            listener.commandReceived(command);
    }
    
    public static void setListener(remote.listeners.CommandReceiveListener listener) {
        Command.listener = listener;
    }
}
