
package remote.ipSync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
/*
 */
public class IPsync {
    private static Timer updater;
    public static final long period = 1800000;
    private static Mailer mailer;
    
    public static void main(String[] args) throws IOException {
        runDaemon();
        System.out.println("enter 'q' to quit ...");
        while(System.in.read()!='q');
    }
    public static String getMyIP() {
        URL whatismyip;
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
            return in.readLine();
        } catch (IOException e) {
            System.err.println(e);
        }
        return null;
    }
    public static void runDaemon() {
        mailer = new Mailer("telecompact11@gmail.com","pop.gmail.com","smtp.gmail.com","pipotron");
        
        updater = new Timer(true);
        updater.schedule(new TimerTask() {
            @Override
            public void run() {
                updateIP();
            }
            
        }, 100l,period);
    }
    private static void updateIP() {
        String IP = getMyIP();
        mailer.sendToMyself("ip: " + IP);
        System.out.println("Server : send ip " + IP);
    }
}
