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

import org.addin.batchapp.domain.Student;
import org.addin.batchapp.domain.*; // for static metamodels
import org.addin.batchapp.repository.StudentRepository;
import org.addin.batchapp.service.dto.StudentCriteria;

import org.addin.batchapp.service.dto.StudentDTO;
import org.addin.batchapp.service.mapper.StudentMapper;
import org.addin.batchapp.domain.enumeration.Gender;

/**
 * Service for executing complex queries for Student entities in the database.
 * The main input is a {@link StudentCriteria} which get's converted to {@link Specifications},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {%link StudentDTO} or a {@link Page} of {%link StudentDTO} which fulfills the criterias
 */
@Service
@Transactional(readOnly = true)
public class StudentQueryService extends QueryService<Student> {

    private final Logger log = LoggerFactory.getLogger(StudentQueryService.class);


    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;
    public StudentQueryService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

    /**
     * Return a {@link List} of {%link StudentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StudentDTO> findByCriteria(StudentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specifications<Student> specification = createSpecification(criteria);
        return studentMapper.toDto(studentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {%link StudentDTO} which matches the criteria from the database
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StudentDTO> findByCriteria(StudentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specifications<Student> specification = createSpecification(criteria);
        final Page<Student> result = studentRepository.findAll(specification, page);
        return result.map(studentMapper::toDto);
    }

    /**
     * Function to convert StudentCriteria to a {@link Specifications}
     */
    private Specifications<Student> createSpecification(StudentCriteria criteria) {
        Specifications<Student> specification = Specifications.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildSpecification(criteria.getId(), Student_.id));
            }
            if (criteria.getRegNum() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRegNum(), Student_.regNum));
            }
            if (criteria.getFirstName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFirstName(), Student_.firstName));
            }
            if (criteria.getLastName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastName(), Student_.lastName));
            }
            if (criteria.getGender() != null) {
                specification = specification.and(buildSpecification(criteria.getGender(), Student_.gender));
            }
        }
        return specification;
    }

}
