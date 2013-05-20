package com.mando.mailer;

import javax.activation.DataHandler;   
import javax.activation.DataSource;   
import javax.activation.FileDataSource;
import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.Message;   
import javax.mail.PasswordAuthentication;   
import javax.mail.Session;   
import javax.mail.Transport;   
import javax.mail.internet.InternetAddress;   
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;   
import javax.mail.internet.MimeMultipart;

import android.util.Log;

import com.sun.mail.imap.protocol.MailboxInfo;

import java.io.ByteArrayInputStream;   
import java.io.IOException;   
import java.io.InputStream;   
import java.io.OutputStream;   
import java.security.Security;   
import java.util.Properties;   

public class Mailer extends javax.mail.Authenticator {   
    private String mailhost = "smtp.gmail.com";   
    private String user;   
    private String password;   
    private Session session;   

    static {   
        Security.addProvider(new JSSEProvider());   
    }  

    public Mailer(EmailSettings em) {   
        this.user = em.username;   
        this.password = em.password;   

        if (em.server.equals(EmailServerType.GMail)) {
            em.serverAddr = "smtp.gmail.com";
            em.port = "465";
            em.isTLS = false;
            em.isSSL = true;
        }        
        Log.e("mando", "send via: " + em.serverAddr + ":" + em.port);
        Properties props = new Properties();   
        props.setProperty("mail.transport.protocol", "smtp");
            
        props.setProperty("mail.host", em.serverAddr);   
        props.put("mail.smtp.auth", "true");
            
        props.put("mail.smtp.port", em.port);   
        props.put("mail.smtp.socketFactory.port", em.port);
        if (em.isTLS) {
            props.put("mail.smtp.starttls.enable","true");
            props.put("mail.smtp.socketFactory.class",   
                    "javax.net.ssl.SSLSocketFactory");   
        }
        if (em.isSSL) {
            props.put("mail.smtp.socketFactory.class",   
                    "javax.net.ssl.SSLSocketFactory");   
        }
        props.put("mail.smtp.socketFactory.fallback", "false");   
        props.setProperty("mail.smtp.quitwait", "false");   
        
        session = Session.getDefaultInstance(props, this);
    }   

    protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(user, password);   
    }   

    public synchronized void sendMail(String subject, String body, String sender, String recipients, String attachment) throws Exception {   
        MimeMessage message = new MimeMessage(session);
        MimeMultipart mp = new MimeMultipart();
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(attachment);
        
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(attachment);
        mp.addBodyPart(messageBodyPart);
        BodyPart msgbp = new MimeBodyPart();
        msgbp.setText(body);
        mp.addBodyPart(msgbp);
        message.setSender(new InternetAddress(sender));   
        message.setSubject(subject);   
        
        message.setContent(mp);
        
        if (recipients.indexOf(',') > 0)   
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipients));   
        else  
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipients));
        Log.i("mando", "Prep complete. Sending..");
        Transport.send(message);
    }   

    public class ByteArrayDataSource implements DataSource {   
        private byte[] data;   
        private String type;   

        public ByteArrayDataSource(byte[] data, String type) {   
            super();   
            this.data = data;   
            this.type = type;   
        }   

        public ByteArrayDataSource(byte[] data) {   
            super();   
            this.data = data;   
        }   

        public void setType(String type) {   
            this.type = type;   
        }   

        public String getContentType() {   
            if (type == null)   
                return "application/octet-stream";   
            else  
                return type;   
        }   

        public InputStream getInputStream() throws IOException {   
            return new ByteArrayInputStream(data);   
        }   

        public String getName() {   
            return "ByteArrayDataSource";   
        }   

        public OutputStream getOutputStream() throws IOException {   
            throw new IOException("Not Supported");   
        }   
    }   
}  