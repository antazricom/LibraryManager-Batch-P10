package com.antazri.batch.step.reader;

import com.antazri.generated.batch.DoReminderRequest;
import com.antazri.generated.batch.DoReminderResponse;
import com.antazri.generated.batch.Loan;
import com.antazri.generated.batch.ReminderException;
import com.antazri.service.impl.ReminderService;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * La classe WebServiceReader est créée pour fournir un accès au WebService et aux informations fournies par ses services
 */
public class WebserviceReader {

    @Autowired
    private ReminderService reminderService;

    /**
     * La méthode findLateLoansFromWebService permet de requêter au Web Service l'ensemble des informations
     * nécessaires à l'envoi de mails aux utilisateurs via le ReminderService fournit via la génération des
     * éléments depuis le WSDL
     * @return
     */
    public List<Loan> findLateLoansFromWebService() {
        DoReminderResponse vResponse = new DoReminderResponse();

        try {
            vResponse = reminderService.getReminderPort().doReminder(new DoReminderRequest());
        } catch (ReminderException e) {
            e.printStackTrace();
        }

        return vResponse.getLoans();
    }
}
