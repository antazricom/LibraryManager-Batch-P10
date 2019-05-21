package com.antazri.batch;

import com.antazri.batch.job.SendMailJob;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * LibrarymanagerBatchApplication est la classe principale du Batch. Sa méthode main permet de charger les ficheirs XML
 * de configuration Spring puis de lancer l'exécution du Batch et du Job SendMailJob.
 */
public class LibrarymanagerBatchApplication {

    public static void main(String[] args) {

        Logger logger = LogManager.getLogger(LibrarymanagerBatchApplication.class);

        ApplicationContext context =
                new ClassPathXmlApplicationContext("classpath:/com/antazri/batch/spring/applicationContext-batch.xml");

        JobLauncher jobLauncher = (JobLauncher) context.getBean("jobLauncher");
        SendMailJob sendMailJob = (SendMailJob) context.getBean("sendMailJob");
        JobParameters parameters = new JobParameters();

        try {
            JobExecution execution = jobLauncher.run(sendMailJob.doBatchJob(), parameters);
        } catch (Exception pE) {
            logger.fatal("Le Batch n'a pas pu se lancer : " + pE.getCause());
        }

    }

}
