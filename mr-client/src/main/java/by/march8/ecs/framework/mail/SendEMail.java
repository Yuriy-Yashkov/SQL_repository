package by.march8.ecs.framework.mail;

import dept.MyReportsModule;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class SendEMail {

    private static String SMTP_HOST_NAME;
    private static String SMTP_AUTH_USER;
    private static String SMTP_AUTH_PWD;

    private String messageTitle;
    private String senderEmail;

    private List<File> attachList;
    private List<String> addressList;

    public SendEMail(String title) {
        try {
            File configFile = new File(MyReportsModule.confPath + "Conf.properties");
            Properties properties = new Properties();
            properties.load(new FileInputStream(configFile));
            SMTP_AUTH_USER = properties.getProperty("email.address");
            senderEmail = properties.getProperty("email.address");
            SMTP_AUTH_PWD = properties.getProperty("email.password");
            SMTP_HOST_NAME = properties.getProperty("email.host");
            addressList = new ArrayList<>();
            attachList = new ArrayList<>();
            if (title == null) {
                messageTitle = "ОАО \"8 Марта\"";
            } else {
                messageTitle = title;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка при попытке загрузить файл с конфигурациями Conf.properties", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
    }

    public SendEMail() {
        this(null);
    }

    public boolean send() {
        return postMail(messageTitle);
    }


    private boolean postMail(String mailHeaderText) {
        try {
            Properties properties = new Properties();
            properties.put("mail.smtp.host", SMTP_HOST_NAME);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");
            properties.put("mail.transport.protocol", "smtps");

            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getDefaultInstance(properties, auth);
            session.setDebug(false);

            Message message = new MimeMessage(session);

            InternetAddress senderAddress = new InternetAddress(senderEmail);
            message.setFrom(senderAddress);

            InternetAddress[] addressTo = new InternetAddress[addressList.size()];
            for (int i = 0; i < addressList.size(); i++) {
                String address = addressList.get(i);
                if (address != null) {
                    addressTo[i] = new InternetAddress(address);
                } else {
                    addressTo[i] = new InternetAddress();
                }
            }

            message.setRecipients(Message.RecipientType.TO, addressTo);
            message.setSubject(mailHeaderText);

            Multipart content = new MimeMultipart();

            MimeBodyPart partInformation = new MimeBodyPart();
            partInformation.setContent("Прикреплённые файлы можно открыть при помощи программы Excel", "text/plain;charset=\"UTF-8\"");
            content.addBodyPart(partInformation);


            for (File file : attachList) {
                if (file.exists()) {
                    MimeBodyPart part = new MimeBodyPart();
                    part.attachFile(file);
                    part.setFileName(MimeUtility.encodeText(file.getName(), "UTF-8", null));
                    content.addBodyPart(part);
                }
            }

            message.setContent(content);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public List<File> getAttachList() {
        if (attachList == null) {
            attachList = new ArrayList<>();
        }
        return attachList;
    }

    public List<String> getAddressList() {
        if (addressList == null) {
            addressList = new ArrayList<>();
        }
        return addressList;
    }

    private class SMTPAuthenticator extends Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }

    }
}

