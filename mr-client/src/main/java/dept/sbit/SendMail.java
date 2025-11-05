package dept.sbit;

/**
 *
 * @author vova
 */

import by.march8.ecs.application.modules.warehouse.external.shipping.services.SaleDocumentHistoryService;
import common.ProgressBar;
import dept.MyReportsModule;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
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
import java.io.IOException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/*
  To use this program, change values for the following three constants,

    SMTP_HOST_NAME -- Has your SMTP Host Name
    SMTP_AUTH_USER -- Has your SMTP Authentication UserName
    SMTP_AUTH_PWD  -- Has your SMTP Authentication Password

  Next change values for fields

  emailMsgTxt  -- Message Text for the Email
  emailSubjectTxt  -- Subject for email
  emailFromAddress -- Email Address whose name will appears as "from" address

  Next change value for "emailList".
  This String array has List of all Email Addresses to Email Email needs to be sent to.


  Next to run the program, execute it as follows,

  SendMailUsingAuthentication authProg = new SendMailUsingAuthentication();

*/

public class SendMail extends SwingWorker<Void, Void> {

    private static final String emailSubjectTxt = "ОАО \"8 Марта\"";
    private static String SMTP_HOST_NAME;
    private static String SMTP_AUTH_USER;
    private static String SMTP_AUTH_PWD;
    private static String emailMsgTxt = "";
    private static String emailFromAddress;
    ProgressBar pb = null;
    Properties prop = new Properties();
    private boolean createApt;
    private String emailList = new String();
    private Vector v = new Vector();
    private boolean isCustomAttach;

    public SendMail(Vector v, String text, String mail, boolean createApt, boolean isCustomAttach) {
        try {
            this.createApt = createApt;
            this.isCustomAttach = isCustomAttach;
            File configfile = new File(MyReportsModule.confPath + "Conf.properties");
            prop.load(new FileInputStream(configfile));
            SMTP_AUTH_USER = prop.getProperty("email.address");
            emailFromAddress = prop.getProperty("email.address");
            SMTP_AUTH_PWD = prop.getProperty("email.password");
            SMTP_HOST_NAME = prop.getProperty("email.host");
        } catch (Exception e) {
            System.err.println("Ошибка при попытке загрузить файл с конфигурациями Conf.properties: " + e);
            JOptionPane.showMessageDialog(null, "Ошибка при попытке загрузить файл с конфигурациями Conf.properties", "Ошибка!!!", JOptionPane.ERROR_MESSAGE);
        }
        try {
            //dialog = new WaitForm(new JDialog(), true);
            pb = new ProgressBar(new JDialog(), false, "Отправка письма");
            emailList = new String(mail);
            this.v = v;
            this.emailMsgTxt = text;
        } catch (Exception e) {
            System.err.println(e);
            JOptionPane.showMessageDialog(null, "Ошибка при отправке письма.", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }


    protected Void doInBackground() throws Exception {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //dialog.setVisible(true);
                pb.setVisible(true);
            }
        });

        try {
            postMail(emailList, emailSubjectTxt, emailMsgTxt, emailFromAddress);
        } catch (Exception e) {
            System.err.println(emailList + " - " + e);
            JOptionPane.showMessageDialog(null, "Ошибка при отправке письма.\n Проверьте правильность электронного адреса", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    @Override
    protected void done() {
        try {
            Iterator i = v.iterator();

            String path = new String("");
            if (!isCustomAttach) {
                path += MyReportsModule.confPath + "/Enakl";
            } else {
                path += MyReportsModule.confPath + "/temp";
            }

            SaleDocumentHistoryService historyService = SaleDocumentHistoryService.getInstance();


            while (i.hasNext()) {
                String docName = i.next().toString();
                File oldF = new File(path + "/" + docName + ".dbf");
                if (oldF.exists()) oldF.delete();
                oldF = new File(path + "/" + docName + ".ods");
                if (oldF.exists()) oldF.delete();

                historyService.historySendEmail(docName, emailList);
            }
        } catch (Exception e) {
            System.out.println("Ошибка при очистке накладных" + e);
            JOptionPane.showMessageDialog(null, "Ошибка при очистке накладных", "Ошибка!!!", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
        pb.setVisible(false);
        pb.dispose();
    }

    public void postMail(String recipients, String subject, String message, String from) throws MessagingException, IOException {
        boolean debug = false;

        //Set the host smtp address
        Properties props = new Properties();
        props.put("mail.smtp.host", SMTP_HOST_NAME);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.transport.protocol", "smtps");

        //mail.smtp.starttls.enable="true"
        //mail.transport.protocol="smtps"


        Authenticator auth = new SMTPAuthenticator();
        Session session = Session.getDefaultInstance(props, auth);

        session.setDebug(debug);

        // create a message
        Message msg = new MimeMessage(session);

        // set the from and to address
        InternetAddress addressFrom = new InternetAddress(from);
        msg.setFrom(addressFrom);

        InternetAddress[] addressTo = new InternetAddress[1];//[recipients.length];
        addressTo[0] = new InternetAddress(recipients);

        msg.setRecipients(Message.RecipientType.TO, addressTo);


        // Setting the Subject and Content Type
        msg.setSubject(subject);

        Multipart mp = new MimeMultipart();

        MimeBodyPart mbpT = new MimeBodyPart();
        mbpT.setContent(emailMsgTxt + "\nПрикреплённые файлы можно открыть при помощи программы Excel", "text/plain;charset=\"UTF-8\"");
        mp.addBodyPart(mbpT);

        Iterator i = v.iterator();
        //String path = new String((new File("")).getCanonicalPath().replace('\\', '/'));
        String path = new String("");
        if (isCustomAttach) {
            path += MyReportsModule.confPath + "/temp";
        } else {
            path += MyReportsModule.confPath + "/Enakl";
        }
        while (i.hasNext()) {
            String docName = i.next().toString();

            MimeBodyPart mbp1 = new MimeBodyPart();
            String filename = docName + ".dbf";

            if (isCustomAttach) {
                filename = docName + ".xls";
            }

            mbp1.attachFile(path + "/" + filename);
            System.out.println("Отправка файла: " + path + "/" + filename);
            mbp1.setFileName(MimeUtility.encodeText(filename, "UTF-8", null));
            mp.addBodyPart(mbp1);

            // Если было выбрано формирование приложения - прикрепляем к письму
            if (createApt) {
                MimeBodyPart mbp2 = new MimeBodyPart();
                filename = docName + ".ods";
                mbp2.attachFile(path + "/" + filename);
                mbp2.setFileName(MimeUtility.encodeText(filename, "UTF-8", null));
                mp.addBodyPart(mbp2);
            }

        }

        msg.setContent(mp);

        Transport.send(msg);
    }


    /**
     * SimpleAuthenticator is used to do simple authentication
     * when the SMTP server requires it.
     */
    private class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = SMTP_AUTH_USER;
            String password = SMTP_AUTH_PWD;
            return new PasswordAuthentication(username, password);
        }
    }

}

