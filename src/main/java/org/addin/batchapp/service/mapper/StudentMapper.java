package org.addin.batchapp.service.mapper;

import org.addin.batchapp.domain.*;
import org.addin.batchapp.service.dto.StudentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Student and its DTO StudentDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface StudentMapper extends EntityMapper <StudentDTO, Student> {
    
    @Mapping(target = "classGroups", ignore = true)
    Student toEntity(StudentDTO studentDTO); 
    default Student fromId(Long id) {
        if (id == null) {
            return null;
        }
        Student student = new Student();
        student.setId(id);
        return student;
    }
}
