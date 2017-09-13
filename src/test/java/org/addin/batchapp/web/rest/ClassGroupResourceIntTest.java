package org.addin.batchapp.web.rest;

import org.addin.batchapp.BatchappApp;

import org.addin.batchapp.domain.ClassGroup;
import org.addin.batchapp.repository.ClassGroupRepository;
import org.addin.batchapp.service.ClassGroupService;
import org.addin.batchapp.service.dto.ClassGroupDTO;
import org.addin.batchapp.service.mapper.ClassGroupMapper;
import org.addin.batchapp.web.rest.errors.ExceptionTranslator;
import org.addin.batchapp.service.dto.ClassGroupCriteria;
import org.addin.batchapp.service.ClassGroupQueryService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.addin.batchapp.domain.enumeration.SchoolType;
/**
 * Test class for the ClassGroupResource REST controller.
 *
 * @see ClassGroupResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BatchappApp.class)
public class ClassGroupResourceIntTest {

    private static final String DEFAULT_CLASS_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CLASS_CODE = "BBBBBBBBBB";

    private static final SchoolType DEFAULT_SCHOOL_TYPE = SchoolType.PRIMARY_SCHOOL;
    private static final SchoolType UPDATED_SCHOOL_TYPE = SchoolType.MIDDLE_SCHOOL;

    private static final Integer DEFAULT_CLASS_LEVEL = 1;
    private static final Integer UPDATED_CLASS_LEVEL = 2;

    @Autowired
    private ClassGroupRepository classGroupRepository;

    @Autowired
    private ClassGroupMapper classGroupMapper;

    @Autowired
    private ClassGroupService classGroupService;

    @Autowired
    private ClassGroupQueryService classGroupQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restClassGroupMockMvc;

    private ClassGroup classGroup;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClassGroupResource classGroupResource = new ClassGroupResource(classGroupService, classGroupQueryService);
        this.restClassGroupMockMvc = MockMvcBuilders.standaloneSetup(classGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ClassGroup createEntity(EntityManager em) {
        ClassGroup classGroup = new ClassGroup()
            .classCode(DEFAULT_CLASS_CODE)
            .schoolType(DEFAULT_SCHOOL_TYPE)
            .classLevel(DEFAULT_CLASS_LEVEL);
        return classGroup;
    }

    @Before
    public void initTest() {
        classGroup = createEntity(em);
    }

    @Test
    @Transactional
    public void createClassGroup() throws Exception {
        int databaseSizeBeforeCreate = classGroupRepository.findAll().size();

        // Create the ClassGroup
        ClassGroupDTO classGroupDTO = classGroupMapper.toDto(classGroup);
        restClassGroupMockMvc.perform(post("/api/class-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassGroup in the database
        List<ClassGroup> classGroupList = classGroupRepository.findAll();
        assertThat(classGroupList).hasSize(databaseSizeBeforeCreate + 1);
        ClassGroup testClassGroup = classGroupList.get(classGroupList.size() - 1);
        assertThat(testClassGroup.getClassCode()).isEqualTo(DEFAULT_CLASS_CODE);
        assertThat(testClassGroup.getSchoolType()).isEqualTo(DEFAULT_SCHOOL_TYPE);
        assertThat(testClassGroup.getClassLevel()).isEqualTo(DEFAULT_CLASS_LEVEL);
    }

    @Test
    @Transactional
    public void createClassGroupWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = classGroupRepository.findAll().size();

        // Create the ClassGroup with an existing ID
        classGroup.setId(1L);
        ClassGroupDTO classGroupDTO = classGroupMapper.toDto(classGroup);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClassGroupMockMvc.perform(post("/api/class-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ClassGroup in the database
        List<ClassGroup> classGroupList = classGroupRepository.findAll();
        assertThat(classGroupList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkClassCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classGroupRepository.findAll().size();
        // set the field null
        classGroup.setClassCode(null);

        // Create the ClassGroup, which fails.
        ClassGroupDTO classGroupDTO = classGroupMapper.toDto(classGroup);

        restClassGroupMockMvc.perform(post("/api/class-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classGroupDTO)))
            .andExpect(status().isBadRequest());

        List<ClassGroup> classGroupList = classGroupRepository.findAll();
        assertThat(classGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSchoolTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = classGroupRepository.findAll().size();
        // set the field null
        classGroup.setSchoolType(null);

        // Create the ClassGroup, which fails.
        ClassGroupDTO classGroupDTO = classGroupMapper.toDto(classGroup);

        restClassGroupMockMvc.perform(post("/api/class-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classGroupDTO)))
            .andExpect(status().isBadRequest());

        List<ClassGroup> classGroupList = classGroupRepository.findAll();
        assertThat(classGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkClassLevelIsRequired() throws Exception {
        int databaseSizeBeforeTest = classGroupRepository.findAll().size();
        // set the field null
        classGroup.setClassLevel(null);

        // Create the ClassGroup, which fails.
        ClassGroupDTO classGroupDTO = classGroupMapper.toDto(classGroup);

        restClassGroupMockMvc.perform(post("/api/class-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classGroupDTO)))
            .andExpect(status().isBadRequest());

        List<ClassGroup> classGroupList = classGroupRepository.findAll();
        assertThat(classGroupList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClassGroups() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList
        restClassGroupMockMvc.perform(get("/api/class-groups?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].classCode").value(hasItem(DEFAULT_CLASS_CODE.toString())))
            .andExpect(jsonPath("$.[*].schoolType").value(hasItem(DEFAULT_SCHOOL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].classLevel").value(hasItem(DEFAULT_CLASS_LEVEL)));
    }

    @Test
    @Transactional
    public void getClassGroup() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get the classGroup
        restClassGroupMockMvc.perform(get("/api/class-groups/{id}", classGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(classGroup.getId().intValue()))
            .andExpect(jsonPath("$.classCode").value(DEFAULT_CLASS_CODE.toString()))
            .andExpect(jsonPath("$.schoolType").value(DEFAULT_SCHOOL_TYPE.toString()))
            .andExpect(jsonPath("$.classLevel").value(DEFAULT_CLASS_LEVEL));
    }

    @Test
    @Transactional
    public void getAllClassGroupsByClassCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where classCode equals to DEFAULT_CLASS_CODE
        defaultClassGroupShouldBeFound("classCode.equals=" + DEFAULT_CLASS_CODE);

        // Get all the classGroupList where classCode equals to UPDATED_CLASS_CODE
        defaultClassGroupShouldNotBeFound("classCode.equals=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    public void getAllClassGroupsByClassCodeIsInShouldWork() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where classCode in DEFAULT_CLASS_CODE or UPDATED_CLASS_CODE
        defaultClassGroupShouldBeFound("classCode.in=" + DEFAULT_CLASS_CODE + "," + UPDATED_CLASS_CODE);

        // Get all the classGroupList where classCode equals to UPDATED_CLASS_CODE
        defaultClassGroupShouldNotBeFound("classCode.in=" + UPDATED_CLASS_CODE);
    }

    @Test
    @Transactional
    public void getAllClassGroupsByClassCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where classCode is not null
        defaultClassGroupShouldBeFound("classCode.specified=true");

        // Get all the classGroupList where classCode is null
        defaultClassGroupShouldNotBeFound("classCode.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassGroupsBySchoolTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where schoolType equals to DEFAULT_SCHOOL_TYPE
        defaultClassGroupShouldBeFound("schoolType.equals=" + DEFAULT_SCHOOL_TYPE);

        // Get all the classGroupList where schoolType equals to UPDATED_SCHOOL_TYPE
        defaultClassGroupShouldNotBeFound("schoolType.equals=" + UPDATED_SCHOOL_TYPE);
    }

    @Test
    @Transactional
    public void getAllClassGroupsBySchoolTypeIsInShouldWork() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where schoolType in DEFAULT_SCHOOL_TYPE or UPDATED_SCHOOL_TYPE
        defaultClassGroupShouldBeFound("schoolType.in=" + DEFAULT_SCHOOL_TYPE + "," + UPDATED_SCHOOL_TYPE);

        // Get all the classGroupList where schoolType equals to UPDATED_SCHOOL_TYPE
        defaultClassGroupShouldNotBeFound("schoolType.in=" + UPDATED_SCHOOL_TYPE);
    }

    @Test
    @Transactional
    public void getAllClassGroupsBySchoolTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where schoolType is not null
        defaultClassGroupShouldBeFound("schoolType.specified=true");

        // Get all the classGroupList where schoolType is null
        defaultClassGroupShouldNotBeFound("schoolType.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassGroupsByClassLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where classLevel equals to DEFAULT_CLASS_LEVEL
        defaultClassGroupShouldBeFound("classLevel.equals=" + DEFAULT_CLASS_LEVEL);

        // Get all the classGroupList where classLevel equals to UPDATED_CLASS_LEVEL
        defaultClassGroupShouldNotBeFound("classLevel.equals=" + UPDATED_CLASS_LEVEL);
    }

    @Test
    @Transactional
    public void getAllClassGroupsByClassLevelIsInShouldWork() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where classLevel in DEFAULT_CLASS_LEVEL or UPDATED_CLASS_LEVEL
        defaultClassGroupShouldBeFound("classLevel.in=" + DEFAULT_CLASS_LEVEL + "," + UPDATED_CLASS_LEVEL);

        // Get all the classGroupList where classLevel equals to UPDATED_CLASS_LEVEL
        defaultClassGroupShouldNotBeFound("classLevel.in=" + UPDATED_CLASS_LEVEL);
    }

    @Test
    @Transactional
    public void getAllClassGroupsByClassLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where classLevel is not null
        defaultClassGroupShouldBeFound("classLevel.specified=true");

        // Get all the classGroupList where classLevel is null
        defaultClassGroupShouldNotBeFound("classLevel.specified=false");
    }

    @Test
    @Transactional
    public void getAllClassGroupsByClassLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where classLevel greater than or equals to DEFAULT_CLASS_LEVEL
        defaultClassGroupShouldBeFound("classLevel.greaterOrEqualThan=" + DEFAULT_CLASS_LEVEL);

        // Get all the classGroupList where classLevel greater than or equals to UPDATED_CLASS_LEVEL
        defaultClassGroupShouldNotBeFound("classLevel.greaterOrEqualThan=" + UPDATED_CLASS_LEVEL);
    }

    @Test
    @Transactional
    public void getAllClassGroupsByClassLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);

        // Get all the classGroupList where classLevel less than or equals to DEFAULT_CLASS_LEVEL
        defaultClassGroupShouldNotBeFound("classLevel.lessThan=" + DEFAULT_CLASS_LEVEL);

        // Get all the classGroupList where classLevel less than or equals to UPDATED_CLASS_LEVEL
        defaultClassGroupShouldBeFound("classLevel.lessThan=" + UPDATED_CLASS_LEVEL);
    }


    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultClassGroupShouldBeFound(String filter) throws Exception {
        restClassGroupMockMvc.perform(get("/api/class-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(classGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].classCode").value(hasItem(DEFAULT_CLASS_CODE.toString())))
            .andExpect(jsonPath("$.[*].schoolType").value(hasItem(DEFAULT_SCHOOL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].classLevel").value(hasItem(DEFAULT_CLASS_LEVEL)));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultClassGroupShouldNotBeFound(String filter) throws Exception {
        restClassGroupMockMvc.perform(get("/api/class-groups?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingClassGroup() throws Exception {
        // Get the classGroup
        restClassGroupMockMvc.perform(get("/api/class-groups/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClassGroup() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);
        int databaseSizeBeforeUpdate = classGroupRepository.findAll().size();

        // Update the classGroup
        ClassGroup updatedClassGroup = classGroupRepository.findOne(classGroup.getId());
        updatedClassGroup
            .classCode(UPDATED_CLASS_CODE)
            .schoolType(UPDATED_SCHOOL_TYPE)
            .classLevel(UPDATED_CLASS_LEVEL);
        ClassGroupDTO classGroupDTO = classGroupMapper.toDto(updatedClassGroup);

        restClassGroupMockMvc.perform(put("/api/class-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classGroupDTO)))
            .andExpect(status().isOk());

        // Validate the ClassGroup in the database
        List<ClassGroup> classGroupList = classGroupRepository.findAll();
        assertThat(classGroupList).hasSize(databaseSizeBeforeUpdate);
        ClassGroup testClassGroup = classGroupList.get(classGroupList.size() - 1);
        assertThat(testClassGroup.getClassCode()).isEqualTo(UPDATED_CLASS_CODE);
        assertThat(testClassGroup.getSchoolType()).isEqualTo(UPDATED_SCHOOL_TYPE);
        assertThat(testClassGroup.getClassLevel()).isEqualTo(UPDATED_CLASS_LEVEL);
    }

    @Test
    @Transactional
    public void updateNonExistingClassGroup() throws Exception {
        int databaseSizeBeforeUpdate = classGroupRepository.findAll().size();

        // Create the ClassGroup
        ClassGroupDTO classGroupDTO = classGroupMapper.toDto(classGroup);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restClassGroupMockMvc.perform(put("/api/class-groups")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(classGroupDTO)))
            .andExpect(status().isCreated());

        // Validate the ClassGroup in the database
        List<ClassGroup> classGroupList = classGroupRepository.findAll();
        assertThat(classGroupList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteClassGroup() throws Exception {
        // Initialize the database
        classGroupRepository.saveAndFlush(classGroup);
        int databaseSizeBeforeDelete = classGroupRepository.findAll().size();

        // Get the classGroup
        restClassGroupMockMvc.perform(delete("/api/class-groups/{id}", classGroup.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<ClassGroup> classGroupList = classGroupRepository.findAll();
        assertThat(classGroupList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassGroup.class);
        ClassGroup classGroup1 = new ClassGroup();
        classGroup1.setId(1L);
        ClassGroup classGroup2 = new ClassGroup();
        classGroup2.setId(classGroup1.getId());
        assertThat(classGroup1).isEqualTo(classGroup2);
        classGroup2.setId(2L);
        assertThat(classGroup1).isNotEqualTo(classGroup2);
        classGroup1.setId(null);
        assertThat(classGroup1).isNotEqualTo(classGroup2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClassGroupDTO.class);
        ClassGroupDTO classGroupDTO1 = new ClassGroupDTO();
        classGroupDTO1.setId(1L);
        ClassGroupDTO classGroupDTO2 = new ClassGroupDTO();
        assertThat(classGroupDTO1).isNotEqualTo(classGroupDTO2);
        classGroupDTO2.setId(classGroupDTO1.getId());
        assertThat(classGroupDTO1).isEqualTo(classGroupDTO2);
        classGroupDTO2.setId(2L);
        assertThat(classGroupDTO1).isNotEqualTo(classGroupDTO2);
        classGroupDTO1.setId(null);
        assertThat(classGroupDTO1).isNotEqualTo(classGroupDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(classGroupMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(classGroupMapper.fromId(null)).isNull();
    }
}
