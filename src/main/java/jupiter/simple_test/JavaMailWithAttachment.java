package jupiter.simple_test;

/**
 * Created by dextry on 2017/5/10.
 */

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class JavaMailWithAttachment {

    private MimeMessage message;
    private Session session;
    private Transport transport;


    public static String sender_username = "float01234@163.com";
    public static String sender_password = "123zbletsgo";
    public static String mailHost = "smtp.163.com";

    /*
     * 初始化方法
     */
    public JavaMailWithAttachment(boolean debug) {

        Properties props = new Properties();                    // 参数配置
        props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
        props.setProperty("mail.smtp.host", mailHost);   // 发件人的邮箱的 SMTP 服务器地址
        props.setProperty("mail.smtp.auth", "true");            // 需要请求认证
        final String smtpPort = "465";
        props.setProperty("mail.smtp.port", smtpPort);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", smtpPort);


        session = Session.getDefaultInstance(props);
        session.setDebug(debug);// 开启后有调试信息
        message = new MimeMessage(session);
    }

    /**
     * 发送邮件
     *
     * @param subject     邮件主题
     * @param sendHtml    邮件内容
     * @param receiveUser 收件人地址
     * @param attachment  附件
     */
    public void doSendHtmlEmail(String subject, String sendHtml, String receiveUser, File attachment) {
        try {
            // 发件人
            InternetAddress from = new InternetAddress(sender_username);
            message.setFrom(from);

            // 收件人
            InternetAddress to = new InternetAddress(receiveUser);
            message.setRecipient(Message.RecipientType.TO, to);

            // 邮件主题
            message.setSubject(subject);

            // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
            Multipart multipart = new MimeMultipart();

            // 添加邮件正文
            BodyPart contentPart = new MimeBodyPart();
            contentPart.setContent(sendHtml, "text/html;charset=UTF-8");
            multipart.addBodyPart(contentPart);

            // 添加附件的内容
            if (attachment != null) {
                BodyPart attachmentBodyPart = new MimeBodyPart();
                DataSource source = new FileDataSource(attachment);
                attachmentBodyPart.setDataHandler(new DataHandler(source));

                // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
                // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
                //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
                //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");

                //MimeUtility.encodeWord可以避免文件名乱码
                attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
                multipart.addBodyPart(attachmentBodyPart);
            }

            // 将multipart对象放到message中
            message.setContent(multipart);
            // 保存邮件
            message.saveChanges();

            transport = session.getTransport("smtp");
            // smtp验证，就是你用来发邮件的邮箱用户名密码
            transport.connect(mailHost, sender_username, sender_password);
            // 发送
            transport.sendMessage(message, message.getAllRecipients());

            System.out.println("send success!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (transport != null) {
                try {
                    transport.close();
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        String path = args[0];
        String filename = args[1];
        String email = args[2];
        File affix = new File(path  + File.separator + filename + ".csv");
        JavaMailWithAttachment se = new JavaMailWithAttachment(true);

        se.doSendHtmlEmail("自动报表", "<br>hi,all: </br>" +
                "<br> &nbsp&nbsp这是来自pg_system的自动化报表:"+filename+",SEND TIME: "+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())+",请查收.</br>", email, affix);
    }


}