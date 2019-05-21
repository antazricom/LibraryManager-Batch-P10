package com.antazri.batch.step.writer;

import com.antazri.batch.utils.EmailProperties;
import com.antazri.batch.mail.service.MailService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * La classe SendMailWriter implémente l'interface ItemWriter et définit la méthode d'envoi des données du Batch
 */
public class SendEmailWriter implements ItemWriter<EmailProperties> {

    @Autowired
    private MailService mailService;

    private Logger logger = LogManager.getLogger(SendEmailWriter.class);

    /**
     * La méthode write utilise la liste d'objets {@link com.antazri.batch.utils.EmailProperties} passée en paramètre
     * pour envoyer des mails via le {@link com.antazri.batch.mail.service.MailService}
     * @param list est une liste d'objets {@link com.antazri.batch.utils.EmailProperties} fournie par
     *             {@link com.antazri.batch.step.processor.EmailPropertiesProcessor}
     * @throws Exception
     */
    @Override
    public void write(List<? extends EmailProperties> list) throws Exception {
        for (EmailProperties item : list) {
            mailService.sendSimpleMail(item);
        }
    }
}
