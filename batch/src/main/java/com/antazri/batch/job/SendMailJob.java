package com.antazri.batch.job;

import com.antazri.batch.step.processor.EmailPropertiesProcessor;
import com.antazri.batch.step.reader.WebserviceReader;
import com.antazri.batch.step.writer.SendEmailWriter;
import com.antazri.batch.utils.EmailProperties;
import com.antazri.generated.batch.Loan;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * La class SendMailJob définit le flow du Job dédié à l'envoi des mails par le Batch. Elle utilise des bean fournis
 * par Spring Framework pour la création des Step. Chaque étape permet d'effectuer une action avec les Reader,
 * Processor et Writer définis.
 */
@EnableBatchProcessing
public class SendMailJob {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private WebserviceReader webserviceReader;

    @Autowired
    private EmailPropertiesProcessor emailPropertiesProcessor;

    @Autowired
    private SendEmailWriter sendEmailWriter;

    @Autowired
    private JobLauncher jobLauncher;

    private List<Loan> loanList;

    private List<EmailProperties> emailPropertiesList;

    private static final Logger logger = LogManager.getLogger(SendMailJob.class);

    /**
     * La méthode doRequestWebService envoi une requête au WebService, via le WebServiceReader, afin de récupérer une
     * liste d'objets {@link com.antazri.generated.batch.Loan} utilisés pour générer les emails.
     * @return un objet Step contenant le résultat de l'action pour le Step suivant
     */
    public Step doRequestWebService() {
        return stepBuilderFactory.get("requestWebServiceStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        loanList = new ArrayList<>();
                        loanList = webserviceReader.findLateLoansFromWebService();
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    /**
     * La méthode doProcessMailProperties permet de générer et transférer des objets {@link com.antazri.batch.utils.EmailProperties}
     * utilisé pour agréger les informations nécessaires à la création de SimpleMailMessage et leur envoie.
     * @return un objet Step contenant le résultat de l'action pour le Step suivant
     */
    public Step doProcessMailProperties() {
        return stepBuilderFactory.get("processMailPropertiesStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        emailPropertiesList = new ArrayList<>();
                        for (Loan loan : loanList) {
                            emailPropertiesList.add(emailPropertiesProcessor.process(loan));
                        }
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    /**
     * La méthode doSendMail est la méthode utilisant la liste "emailPropertiesList" pour envoyer l'ensemble des emails
     * via le Writer {@link com.antazri.batch.step.writer.SendEmailWriter}
     * @return un objet Step contenant le résultat de l'action pour le Step suivant
     */
    public Step doSendMail() {
        return stepBuilderFactory.get("sendMailStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        sendEmailWriter.write(emailPropertiesList);
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    /**
     * La méthode doBatchJob est la méthode utilisée par le JobLauncher de la méthode main de la classe
     * {@link com.antazri.batch.LibrarymanagerBatchApplication} pour lancer l'exécution du Batch
     * @return
     */
    public Job doBatchJob() {
        logger.info("Lancement du Batch");
        return jobBuilderFactory.get("batchJob")
                .start(doRequestWebService())
                .next(doProcessMailProperties())
                .next(doSendMail())
                .build();
    }

//    @Scheduled(cron = "*/10 * * * * *")
    @Scheduled(cron = "0 0 8-10 * * *")
    public void runJob() throws Exception {
        logger.info(emailPropertiesList);
        JobParametersBuilder parameters = new JobParametersBuilder();
        parameters.addString("jobID", String.valueOf(System.currentTimeMillis()));

        try {
            JobExecution execution = jobLauncher.run(doBatchJob(), parameters.toJobParameters());
        } catch (Exception pE) {
            logger.fatal("Le Batch n'a pas pu se lancer : " + pE.getCause());
        }
    }


}
