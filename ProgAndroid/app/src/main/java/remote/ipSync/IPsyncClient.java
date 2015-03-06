
package remote.ipSync;

import javax.mail.Message;
import javax.mail.MessagingException;

/*
 */
public class IPsyncClient {
    public static byte[] getIP() {
        System.out.println("Remote: retrieving IP ...");
        System.out.print("Remote: connecting to mail server ...");
        Mailer mailer = new Mailer("telecompact11@gmail.com","pop.gmail.com","smtp.gmail.com","pipotron");
        System.out.println(" done");
        System.out.print("Remote: downloading mails ...");
        Message[] messages = mailer.check();
        System.out.println(" done");
        String IP=null;
        try {
            for(int i=messages.length;i>0;i--) {
                if(messages[i-1].getSubject().startsWith("ip: ")) {
                    IP = messages[i-1].getSubject().replaceFirst("ip: ", "");
                    break;
                }
            }
        } catch (MessagingException ex) {
            System.err.println("Remote: error while reading IP");
        }
        if(IP!=null)
            return stringToIP(IP);
        else
            return null;
    }
    private static byte[] stringToIP(String IP) {
        String[] subs = IP.split("\\.");
        byte[] ipArray = new byte[4];
        for(int i=0;i<4;i++)
            ipArray[i]=(byte)Integer.parseInt(subs[i]);
        return ipArray;
    }
    public static void main(String[] args) {
        byte[] ip = getIP();
        System.out.println((ip[0]&0xFF) + "." + (ip[1]&0xFF) + "." + (ip[2]&0xFF) + "." + (ip[3]&0xFF));
    }
}
