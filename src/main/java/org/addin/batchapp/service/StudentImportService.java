package org.addin.batchapp.service;

import org.addin.batchapp.config.ImportStudentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class StudentImportService {

    private final Logger log = LoggerFactory.getLogger(StudentImportService.class);

    private final JobLauncher jobLauncher;
    private final Job job;

    public StudentImportService(JobLauncher jobLauncher,@Qualifier(ImportStudentConfiguration.JOB_IMPORT_STUDENT_FROM_CSV) Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    public void importFromFile(File csvFile) {

        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString("filePath", csvFile.getAbsolutePath());
        try {
            jobLauncher.run(job, jobParametersBuilder.toJobParameters());
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }
}
