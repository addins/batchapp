package org.addin.batchapp.config;

import org.addin.batchapp.domain.Student;
import org.addin.batchapp.repository.StudentRepository;
import org.addin.batchapp.service.batch.studentimport.CSVStudentItemReader;
import org.addin.batchapp.service.batch.studentimport.StudentImportDTO;
import org.addin.batchapp.service.batch.studentimport.StudentItemProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

import java.io.File;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@EnableBatchProcessing
public class ImportStudentConfiguration {

    public static final String JOB_IMPORT_STUDENT_FROM_CSV = "importStudentFromCSV";
    public static final String STEP_CONVERT_STUDENT_IMPORT_DTO_TO_STUDENT = "convertStudentImportDTOToStudent";
    public static final String CSV_FILE_DIR = "/tmp/batchapp";

    private Logger log = LoggerFactory.getLogger(ImportStudentConfiguration.class);

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final StudentRepository studentRepository;

    public ImportStudentConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, StudentRepository studentRepository) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.studentRepository = studentRepository;
    }


    @EventListener(ApplicationReadyEvent.class)
    public void createCSVFileDir() {
        File file = new File(CSV_FILE_DIR);
        if (!file.isDirectory()) {
            file.mkdir();
        }
    }

    public static String createAbsoluteCSVFileName() {
        return CSV_FILE_DIR + File.separator + ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT) + "_student.csv";
    }

    // tag::readerwriterprocessor[]
    @Bean
    public StudentItemProcessor processor() {
        return new StudentItemProcessor();
    }

    @Bean() @StepScope
    public FlatFileItemReader<StudentImportDTO> reader(@Value("#{jobParameters['filePath']}") String filePath) {
        CSVStudentItemReader itemReader = new CSVStudentItemReader();
        if (filePath != null && !filePath.isEmpty()) {
            itemReader.setFile(new File(filePath));
        }
        return itemReader;
    }

    @Bean
    public ItemWriter<Student> writer() {
        return new ItemWriter<Student>() {
            @Override
            public void write(List<? extends Student> list) throws Exception {
                studentRepository.save(list);
            }
        };
    }
    // end::readerwriterprocessor[]

    // tag::stepjob[]
    @Bean(name = STEP_CONVERT_STUDENT_IMPORT_DTO_TO_STUDENT)
    public Step step(FlatFileItemReader<StudentImportDTO> reader) {
        return stepBuilderFactory.get(STEP_CONVERT_STUDENT_IMPORT_DTO_TO_STUDENT)
            .<StudentImportDTO, Student>chunk(10)
            .reader(reader)
            .processor(processor())
            .writer(writer()).build();
    }

    @Bean(name = JOB_IMPORT_STUDENT_FROM_CSV)
    public Job job(Step step) {
        return jobBuilderFactory.get(JOB_IMPORT_STUDENT_FROM_CSV)
            .incrementer(new RunIdIncrementer()).flow(step).end().build();
    }
    // end::stejob[]
}
