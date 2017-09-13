package org.addin.batchapp.service.dto;

import java.io.Serializable;
import org.addin.batchapp.domain.enumeration.Gender;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the Student entity. This class is used in StudentResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /students?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class StudentCriteria implements Serializable {
    /**
     * Class for filtering Gender
     */
    public static class GenderFilter extends Filter<Gender> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter regNum;

    private StringFilter firstName;

    private StringFilter lastName;

    private GenderFilter gender;

    public StudentCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getRegNum() {
        return regNum;
    }

    public void setRegNum(StringFilter regNum) {
        this.regNum = regNum;
    }

    public StringFilter getFirstName() {
        return firstName;
    }

    public void setFirstName(StringFilter firstName) {
        this.firstName = firstName;
    }

    public StringFilter getLastName() {
        return lastName;
    }

    public void setLastName(StringFilter lastName) {
        this.lastName = lastName;
    }

    public GenderFilter getGender() {
        return gender;
    }

    public void setGender(GenderFilter gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "StudentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (regNum != null ? "regNum=" + regNum + ", " : "") +
                (firstName != null ? "firstName=" + firstName + ", " : "") +
                (lastName != null ? "lastName=" + lastName + ", " : "") +
                (gender != null ? "gender=" + gender + ", " : "") +
            "}";
    }

}
