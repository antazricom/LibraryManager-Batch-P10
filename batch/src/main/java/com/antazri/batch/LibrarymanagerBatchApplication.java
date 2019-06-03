package com.antazri.batch;

import com.antazri.batch.job.SendMailJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * LibrarymanagerBatchApplication est la classe principale du Batch. Sa méthode main permet de charger les ficheirs XML
 * de configuration Spring puis de lancer l'exécution du Batch et du Job SendMailJob.
 */
@EnableScheduling
public class LibrarymanagerBatchApplication {

    private static final Logger logger = LogManager.getLogger(LibrarymanagerBatchApplication.class);

    public static void main(String[] args) {
        try {
            runJob();
        } catch (Exception pE) {
            logger.fatal("Le Batch n'a pas pu se lancer : " + pE.getCause());
        }
    }

    public static void runJob() throws Exception {
        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:/com/antazri/batch/spring/applicationContext-batch.xml");

        SendMailJob sendMailJob = (SendMailJob) context.getBean("sendMailJob");

        try {
            sendMailJob.runJob();
        } catch (Exception pE) {
            logger.fatal("Le Batch n'a pas pu se lancer : " + pE.getCause());
        }
    }

}
