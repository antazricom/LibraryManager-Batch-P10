package com.antazri.batch.mail.service;

import com.antazri.batch.utils.EmailProperties;
import com.antazri.batch.mail.sender.MailSenderConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * La classe MailService permet de créer un service utilisant une seule méthode afin d'enoyer un mail
 * via Un SimpleMailMessage fourni par Spring EMail.
 */
@Component
public class MailService {

    @Autowired
    private MailSenderConfig mailSender;

    /**
     * La méthode sendSimpleMail utilise un objet EmailProperties afin de récupérer toutes les informations nécessaires
     * à l'envoi d'un mail par le Job du Batch
     * @param emailProperties est un objet {@link com.antazri.batch.utils.EmailProperties} créé par l'ItemReader
     *                        {@link com.antazri.batch.step.reader.WebserviceReader}
     */
    public void sendSimpleMail(EmailProperties emailProperties) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(emailProperties.getEmail());
        message.setSubject("LibraryManager : Retard du prêt n°" + emailProperties.getLoanId());
        message.setText(
                "Votre prêt pour le livre " + emailProperties.getBookTitle() +
                "n\'a pas été rendu le " + emailProperties.getDateEnd());

        mailSender.getMailSender().send(message);
    }


}
