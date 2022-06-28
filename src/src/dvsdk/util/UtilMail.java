package dvsdk.util;

/**
 *
 * @author develop-dvs
 * // TODO: Hmmm.... need simple smtp server!
 */
public class UtilMail {
/*
    public static String getEmail() {
        return (GlobalConfig.MASTER_EMAIL.trim().isEmpty()) ? "report@localhost" : GlobalConfig.MASTER_EMAIL.trim();
    }

    public static boolean sendBackMail(String from, String subject, String msg) {
        return sendSimpleMail(from, from, GlobalConfig.GLOBAL_MULIS_FEEDBACK_EMAIL, GlobalConfig.GLOBAL_MULIS_FEEDBACK_EMAIL_NAME, subject, " " + msg, Util.buildHostName());
    }

    public static boolean sendSimpleMail(String from, String to, String subject, String msg) {
        return sendSimpleMail(from, from, to, to, subject, msg, Util.buildHostName());
    }

    public static boolean sendSimpleMail(String from, String to, String subject, String msg, String hostName) {
        return sendSimpleMail(from, from, to, to, subject, msg, hostName);
    }

    public static boolean sendSimpleMail(String from, String fromName, String to, String toName, String subject, String msg, String hostName) {
        try {
            SimpleEmail email = new SimpleEmail();
            email.setHostName(GlobalConfig.GLOBAL_MULIS_SMTP_HOST);
            email.setSmtpPort(GlobalConfig.GLOBAL_MULIS_SMTP_PORT);
            email.addTo(to, toName);
            email.setFrom(from, fromName);
            email.setSubject(subject);
            email.setMsg(msg);
            email.send();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void startSMPTServer() {
        try {
            MyMessageHandlerFactory myFactory = new MyMessageHandlerFactory();
            GlobalConfig.smtpServer = new SMTPServer(myFactory);
            GlobalConfig.smtpServer.setPort(GlobalConfig.GLOBAL_MULIS_SMTP_PORT);
            GlobalConfig.smtpServer.start();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void stopSMPTServer() {
        if (GlobalConfig.smtpServer != null) {
            GlobalConfig.smtpServer.stop();
        }
    }*/
}