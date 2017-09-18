package org.addin.batchapp.service.batch.studentimport;

import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

public class StudentCSVLineMapper extends DefaultLineMapper<StudentImportDTO> {

    public StudentCSVLineMapper() {
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(new String[] {"regNum","firstName","lastName","gender"});
        this.setLineTokenizer(tokenizer);
        BeanWrapperFieldSetMapper<StudentImportDTO> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(StudentImportDTO.class);
        this.setFieldSetMapper(fieldSetMapper);
    }
}
