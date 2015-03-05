
package remote.ipSync;

import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {
    final private String myAddress;
    final private String password;
    private String popHost;
    private String smtpHost;
    private Properties props;
    private Session mailSession;
    
    public Mailer(String myMailAddress, String popServer, String smtpServer, String mailPassword) {
        myAddress = myMailAddress;
        password = mailPassword;
        smtpHost = smtpServer;
        popHost = popServer;
        
        // create mail properties
        props = new Properties();
        props.put("mail.pop3.host", popHost);
        props.put("mail.pop3.port", "995");
        props.put("mail.pop3.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", smtpHost);
        props.put("mail.smtp.port", "25");
        
        // create a session object
        mailSession = Session.getInstance(props, new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication(myAddress, password);
	   }
        });
    }
    public Message[] check() {
        Message[] messages = null;
        try {
            //create the POP3 store object and connect with the pop server
            Store store = mailSession.getStore("pop3s");
            store.connect(popHost, myAddress, password);

            //create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);

            // retrieve the messages from the folder in an array and print it
            messages = emailFolder.getMessages();
        } catch (Exception e) {
            System.err.println(e);
        }
        return messages;
    }
    public void sendToMyself(String text) {
        sendMessage(myAddress, text);
    }
    public void sendMessage(String to, String text) {
        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(mailSession);
	
            // Set From: header field of the header.
            message.setFrom(new InternetAddress(myAddress));
	
            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
	
            // Set Subject: header field
            message.setSubject(text);
            message.setText(text);
            
            // Send message
            Transport transport = mailSession.getTransport("smtps");
            transport.connect(smtpHost, 465, myAddress, password);
            transport.sendMessage(message,message.getAllRecipients());
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
