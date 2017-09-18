package org.addin.batchapp.service.batch.studentimport;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

import java.io.File;

public class CSVStudentItemReader extends FlatFileItemReader<StudentImportDTO> {

    private File file;

    public CSVStudentItemReader() {
        setLineMapper(new DefaultLineMapper<StudentImportDTO>(){{
            setLineTokenizer(new DelimitedLineTokenizer(){{
                setNames(new String[] {"regNum","firstName","lastName","gender"});
            }});
            setFieldSetMapper(new BeanWrapperFieldSetMapper<StudentImportDTO>(){{
                setTargetType(StudentImportDTO.class);
            }});
            setLinesToSkip(1);
        }});
    }

    public void setFile(File file) {
        this.file = file;
        this.setResource(new FileSystemResource(file));
    }
}
