package org.addin.batchapp.web.rest;

import org.addin.batchapp.BatchappApp;

import org.addin.batchapp.domain.Student;
import org.addin.batchapp.repository.StudentRepository;
import org.addin.batchapp.service.StudentService;
import org.addin.batchapp.service.dto.StudentDTO;
import org.addin.batchapp.service.mapper.StudentMapper;
import org.addin.batchapp.web.rest.errors.ExceptionTranslator;
import org.addin.batchapp.service.dto.StudentCriteria;
import org.addin.batchapp.service.StudentQueryService;

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

import org.addin.batchapp.domain.enumeration.Gender;
/**
 * Test class for the StudentResource REST controller.
 *
 * @see StudentResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BatchappApp.class)
public class StudentResourceIntTest {

    private static final String DEFAULT_REG_NUM = "AAAAAAAAAA";
    private static final String UPDATED_REG_NUM = "BBBBBBBBBB";

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final Gender DEFAULT_GENDER = Gender.MALE;
    private static final Gender UPDATED_GENDER = Gender.FEMALE;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private StudentService studentService;

    @Autowired
    private StudentQueryService studentQueryService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restStudentMockMvc;

    private Student student;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final StudentResource studentResource = new StudentResource(studentService, studentQueryService);
        this.restStudentMockMvc = MockMvcBuilders.standaloneSetup(studentResource)
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
    public static Student createEntity(EntityManager em) {
        Student student = new Student()
            .regNum(DEFAULT_REG_NUM)
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .gender(DEFAULT_GENDER);
        return student;
    }

    @Before
    public void initTest() {
        student = createEntity(em);
    }

    @Test
    @Transactional
    public void createStudent() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);
        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate + 1);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getRegNum()).isEqualTo(DEFAULT_REG_NUM);
        assertThat(testStudent.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testStudent.getGender()).isEqualTo(DEFAULT_GENDER);
    }

    @Test
    @Transactional
    public void createStudentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = studentRepository.findAll().size();

        // Create the Student with an existing ID
        student.setId(1L);
        StudentDTO studentDTO = studentMapper.toDto(student);

        // An entity with an existing ID cannot be created, so this API call must fail
        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkRegNumIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setRegNum(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setFirstName(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setLastName(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkGenderIsRequired() throws Exception {
        int databaseSizeBeforeTest = studentRepository.findAll().size();
        // set the field null
        student.setGender(null);

        // Create the Student, which fails.
        StudentDTO studentDTO = studentMapper.toDto(student);

        restStudentMockMvc.perform(post("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isBadRequest());

        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStudents() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList
        restStudentMockMvc.perform(get("/api/students?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].regNum").value(hasItem(DEFAULT_REG_NUM.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }

    @Test
    @Transactional
    public void getStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", student.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(student.getId().intValue()))
            .andExpect(jsonPath("$.regNum").value(DEFAULT_REG_NUM.toString()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.gender").value(DEFAULT_GENDER.toString()));
    }

    @Test
    @Transactional
    public void getAllStudentsByRegNumIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where regNum equals to DEFAULT_REG_NUM
        defaultStudentShouldBeFound("regNum.equals=" + DEFAULT_REG_NUM);

        // Get all the studentList where regNum equals to UPDATED_REG_NUM
        defaultStudentShouldNotBeFound("regNum.equals=" + UPDATED_REG_NUM);
    }

    @Test
    @Transactional
    public void getAllStudentsByRegNumIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where regNum in DEFAULT_REG_NUM or UPDATED_REG_NUM
        defaultStudentShouldBeFound("regNum.in=" + DEFAULT_REG_NUM + "," + UPDATED_REG_NUM);

        // Get all the studentList where regNum equals to UPDATED_REG_NUM
        defaultStudentShouldNotBeFound("regNum.in=" + UPDATED_REG_NUM);
    }

    @Test
    @Transactional
    public void getAllStudentsByRegNumIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where regNum is not null
        defaultStudentShouldBeFound("regNum.specified=true");

        // Get all the studentList where regNum is null
        defaultStudentShouldNotBeFound("regNum.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName equals to DEFAULT_FIRST_NAME
        defaultStudentShouldBeFound("firstName.equals=" + DEFAULT_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.equals=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName in DEFAULT_FIRST_NAME or UPDATED_FIRST_NAME
        defaultStudentShouldBeFound("firstName.in=" + DEFAULT_FIRST_NAME + "," + UPDATED_FIRST_NAME);

        // Get all the studentList where firstName equals to UPDATED_FIRST_NAME
        defaultStudentShouldNotBeFound("firstName.in=" + UPDATED_FIRST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByFirstNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where firstName is not null
        defaultStudentShouldBeFound("firstName.specified=true");

        // Get all the studentList where firstName is null
        defaultStudentShouldNotBeFound("firstName.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName equals to DEFAULT_LAST_NAME
        defaultStudentShouldBeFound("lastName.equals=" + DEFAULT_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.equals=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName in DEFAULT_LAST_NAME or UPDATED_LAST_NAME
        defaultStudentShouldBeFound("lastName.in=" + DEFAULT_LAST_NAME + "," + UPDATED_LAST_NAME);

        // Get all the studentList where lastName equals to UPDATED_LAST_NAME
        defaultStudentShouldNotBeFound("lastName.in=" + UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void getAllStudentsByLastNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where lastName is not null
        defaultStudentShouldBeFound("lastName.specified=true");

        // Get all the studentList where lastName is null
        defaultStudentShouldNotBeFound("lastName.specified=false");
    }

    @Test
    @Transactional
    public void getAllStudentsByGenderIsEqualToSomething() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender equals to DEFAULT_GENDER
        defaultStudentShouldBeFound("gender.equals=" + DEFAULT_GENDER);

        // Get all the studentList where gender equals to UPDATED_GENDER
        defaultStudentShouldNotBeFound("gender.equals=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStudentsByGenderIsInShouldWork() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender in DEFAULT_GENDER or UPDATED_GENDER
        defaultStudentShouldBeFound("gender.in=" + DEFAULT_GENDER + "," + UPDATED_GENDER);

        // Get all the studentList where gender equals to UPDATED_GENDER
        defaultStudentShouldNotBeFound("gender.in=" + UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void getAllStudentsByGenderIsNullOrNotNull() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);

        // Get all the studentList where gender is not null
        defaultStudentShouldBeFound("gender.specified=true");

        // Get all the studentList where gender is null
        defaultStudentShouldNotBeFound("gender.specified=false");
    }

    /**
     * Executes the search, and checks that the default entity is returned
     */
    private void defaultStudentShouldBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(student.getId().intValue())))
            .andExpect(jsonPath("$.[*].regNum").value(hasItem(DEFAULT_REG_NUM.toString())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
            .andExpect(jsonPath("$.[*].gender").value(hasItem(DEFAULT_GENDER.toString())));
    }

    /**
     * Executes the search, and checks that the default entity is not returned
     */
    private void defaultStudentShouldNotBeFound(String filter) throws Exception {
        restStudentMockMvc.perform(get("/api/students?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());
    }


    @Test
    @Transactional
    public void getNonExistingStudent() throws Exception {
        // Get the student
        restStudentMockMvc.perform(get("/api/students/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Update the student
        Student updatedStudent = studentRepository.findOne(student.getId());
        updatedStudent
            .regNum(UPDATED_REG_NUM)
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .gender(UPDATED_GENDER);
        StudentDTO studentDTO = studentMapper.toDto(updatedStudent);

        restStudentMockMvc.perform(put("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isOk());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate);
        Student testStudent = studentList.get(studentList.size() - 1);
        assertThat(testStudent.getRegNum()).isEqualTo(UPDATED_REG_NUM);
        assertThat(testStudent.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testStudent.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testStudent.getGender()).isEqualTo(UPDATED_GENDER);
    }

    @Test
    @Transactional
    public void updateNonExistingStudent() throws Exception {
        int databaseSizeBeforeUpdate = studentRepository.findAll().size();

        // Create the Student
        StudentDTO studentDTO = studentMapper.toDto(student);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restStudentMockMvc.perform(put("/api/students")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(studentDTO)))
            .andExpect(status().isCreated());

        // Validate the Student in the database
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteStudent() throws Exception {
        // Initialize the database
        studentRepository.saveAndFlush(student);
        int databaseSizeBeforeDelete = studentRepository.findAll().size();

        // Get the student
        restStudentMockMvc.perform(delete("/api/students/{id}", student.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<Student> studentList = studentRepository.findAll();
        assertThat(studentList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Student.class);
        Student student1 = new Student();
        student1.setId(1L);
        Student student2 = new Student();
        student2.setId(student1.getId());
        assertThat(student1).isEqualTo(student2);
        student2.setId(2L);
        assertThat(student1).isNotEqualTo(student2);
        student1.setId(null);
        assertThat(student1).isNotEqualTo(student2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(StudentDTO.class);
        StudentDTO studentDTO1 = new StudentDTO();
        studentDTO1.setId(1L);
        StudentDTO studentDTO2 = new StudentDTO();
        assertThat(studentDTO1).isNotEqualTo(studentDTO2);
        studentDTO2.setId(studentDTO1.getId());
        assertThat(studentDTO1).isEqualTo(studentDTO2);
        studentDTO2.setId(2L);
        assertThat(studentDTO1).isNotEqualTo(studentDTO2);
        studentDTO1.setId(null);
        assertThat(studentDTO1).isNotEqualTo(studentDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(studentMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(studentMapper.fromId(null)).isNull();
    }
}
