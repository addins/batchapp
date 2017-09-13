package org.addin.batchapp.service;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import org.addin.batchapp.domain.ClassGroup;
import org.addin.batchapp.domain.*; // for static metamodels
import org.addin.batchapp.repository.ClassGroupRepository;
import org.addin.batchapp.service.dto.ClassGroupCriteria;

import org.addin.batchapp.service.dto.ClassGroupDTO;
import org.addin.batchapp.service.mapper.ClassGroupMapper;
import org.addin.batchapp.domain.enumeration.SchoolType;

/**
 * Service for executing complex queries for ClassGroup entities in the database.
 * The main input is a {@link ClassGroupCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link ClassGroupDTO} or a {@link Page} of {%link ClassGroupDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class ClassGroupQueryService extends QueryService<ClassGroup> {

    private final Logger log = LoggerFactory.getLogger(ClassGroupQueryService.class);


    private final ClassGroupRepository classGroupRepository;

    private final ClassGroupMapper classGroupMapper;
    public ClassGroupQueryService(ClassGroupRepository classGroupRepository, ClassGroupMapper classGroupMapper) {
        this.classGroupRepository = classGroupRepository;
        this.classGroupMapper = classGroupMapper;
    }

    /**
     * Return a {@link List} of {%link ClassGroupDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ClassGroupDTO> findByCriteria(ClassGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<ClassGroup> specification = createSpecification(criteria);
        return classGroupMapper.toDto(classGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link ClassGroupDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ClassGroupDTO> findByCriteria(ClassGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<ClassGroup> specification = createSpecification(criteria);
        final Page<ClassGroup> result = classGroupRepository.findAll(specification, page);
        return result.map(classGroupMapper::toDto);
    }

    /**
     * Function to convert ClassGroupCriteria to a {@link Specifications}
     */
    private Specifications<ClassGroup> createSpecification(ClassGroupCriteria criteria) {
        Specifications<ClassGroup> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), ClassGroup_.id));
            }
            if (criteria.getClassCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getClassCode(), ClassGroup_.classCode));
            }
            if (criteria.getSchoolType() != null) {
                specification = specification.and(buildSpecification(criteria.getSchoolType(), ClassGroup_.schoolType));
            }
            if (criteria.getClassLevel() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClassLevel(), ClassGroup_.classLevel));
            }
        }
        return specification;
    }

}
