package org.addin.batchapp.service.batch.studentimport;

import org.addin.batchapp.domain.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class StudentItemProcessor implements ItemProcessor<StudentImportDTO, Student> {

    private final Logger log = LoggerFactory.getLogger(StudentItemProcessor.class);

    @Override
    public Student process(StudentImportDTO studentDTO) throws Exception {
        log.debug("Processing StudentImportDTO: {}", studentDTO);
        return new Student().firstName(studentDTO.getFirstName())
            .lastName(studentDTO.getLastName())
            .regNum(studentDTO.getRegNum())
            //.gender(Gender.valueOf(studentDTO.getGender()));
            .gender(studentDTO.getGender());
    }
}
