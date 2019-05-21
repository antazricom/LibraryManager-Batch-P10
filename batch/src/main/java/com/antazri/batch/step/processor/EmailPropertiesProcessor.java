package com.antazri.batch.step.processor;

import com.antazri.batch.utils.EmailProperties;
import com.antazri.generated.batch.Loan;
import org.springframework.batch.item.ItemProcessor;

/**
 * La classe EmailPropertiesProcessor implémente l'interface ItemProcessor et utilise sa méthode process afin de
 * générer des items EmailProperties à partir d'objets {@link com.antazri.generated.batch.Loan}
 */
public class EmailPropertiesProcessor implements ItemProcessor<Loan, EmailProperties> {

    @Override
    public EmailProperties process(Loan loan) throws Exception {
        EmailProperties vProperties = new EmailProperties();
        vProperties.setLoanId(loan.getId());
        vProperties.setEmail(loan.getMember().getEmail());
        vProperties.setFirstname(loan.getMember().getFirstname());
        vProperties.setLastname(loan.getMember().getLastname());
        vProperties.setBookTitle(loan.getBook().getTitle());
        vProperties.setDateEnd(loan.getDateEnd().toString());

        return vProperties;
    }
}
