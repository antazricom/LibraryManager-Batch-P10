package com.antazri.batch.mail.sender;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

/**
 * La classe MailSenderConfig est utilisée afin de fournir un client SMTP à l'ensemble du batch pour l'envoi des mails
 */
public class MailSenderConfig {

    private String host;
    private String port;
    private String username;
    private String password;

    public MailSenderConfig(String host, String port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    /**
     * La méthode getMailSender fournit à la classe un Getter pour l'utilisation du MailSenderConfig instancié
     * @return
     */
    public JavaMailSender getMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(this.host);
        mailSender.setPort(Integer.valueOf(this.port));
        mailSender.setUsername(this.username);
        mailSender.setPassword(this.password);

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.debug", "true");

        return mailSender;
    }

}
