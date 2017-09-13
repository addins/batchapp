package org.addin.batchapp.service.mapper;

import org.addin.batchapp.domain.*;
import org.addin.batchapp.service.dto.ClassGroupDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity ClassGroup and its DTO ClassGroupDTO.
 */
@Mapper(componentModel = "spring", uses = {StudentMapper.class, })
public interface ClassGroupMapper extends EntityMapper <ClassGroupDTO, ClassGroup> {
    
    
    default ClassGroup fromId(Long id) {
        if (id == null) {
            return null;
        }
        ClassGroup classGroup = new ClassGroup();
        classGroup.setId(id);
        return classGroup;
    }
}
