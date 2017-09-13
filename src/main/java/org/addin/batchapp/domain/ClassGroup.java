package org.addin.batchapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import org.addin.batchapp.domain.enumeration.SchoolType;

/**
 * A ClassGroup.
 */
@Entity
@Table(name = "class_group")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class ClassGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Size(min = 1)
    @Column(name = "class_code", nullable = false)
    private String classCode;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "school_type", nullable = false)
    private SchoolType schoolType;

    @NotNull
    @Column(name = "class_level", nullable = false)
    private Integer classLevel;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "class_group_student",
               joinColumns = @JoinColumn(name="class_groups_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="students_id", referencedColumnName="id"))
    private Set<Student> students = new HashSet<>();

    // jhipster-needle-entity-add-field - Jhipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getClassCode() {
        return classCode;
    }

    public ClassGroup classCode(String classCode) {
        this.classCode = classCode;
        return this;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public SchoolType getSchoolType() {
        return schoolType;
    }

    public ClassGroup schoolType(SchoolType schoolType) {
        this.schoolType = schoolType;
        return this;
    }

    public void setSchoolType(SchoolType schoolType) {
        this.schoolType = schoolType;
    }

    public Integer getClassLevel() {
        return classLevel;
    }

    public ClassGroup classLevel(Integer classLevel) {
        this.classLevel = classLevel;
        return this;
    }

    public void setClassLevel(Integer classLevel) {
        this.classLevel = classLevel;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public ClassGroup students(Set<Student> students) {
        this.students = students;
        return this;
    }

    public ClassGroup addStudent(Student student) {
        this.students.add(student);
        student.getClassGroups().add(this);
        return this;
    }

    public ClassGroup removeStudent(Student student) {
        this.students.remove(student);
        student.getClassGroups().remove(this);
        return this;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }
    // jhipster-needle-entity-add-getters-setters - Jhipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClassGroup classGroup = (ClassGroup) o;
        if (classGroup.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), classGroup.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClassGroup{" +
            "id=" + getId() +
            ", classCode='" + getClassCode() + "'" +
            ", schoolType='" + getSchoolType() + "'" +
            ", classLevel='" + getClassLevel() + "'" +
            "}";
    }
}
