
package remote;

import java.net.InetAddress;
import java.net.UnknownHostException;
import netzwerk.Connector;
import netzwerk.messages.Message;

/**
 *
 * @author arnaud
 */
public class EchoClient extends Connector {
    public EchoClient(String name) {
        super(name);
        try {
            connect(InetAddress.getLocalHost().getAddress(),2323);
        } catch (UnknownHostException ex) {
        }
    }
    @Override
    protected synchronized void onReceive(Message message) {
        String messageType = message.getClass().getSimpleName();
        if(messageType.equals("ConnectionRequest")) {
            acceptConnection(true);
        } else if(messageType.equals("ConnectionClosure") || messageType.equals("ConnectionAnswer") || messageType.equals("UserListUpdate")) {
            // do nothing
        } else {
            sendMessage(message);
        }
    }
}
