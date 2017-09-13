package org.addin.batchapp.service.dto;

import java.io.Serializable;
import org.addin.batchapp.domain.enumeration.SchoolType;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;






/**
 * Criteria class for the ClassGroup entity. This class is used in ClassGroupResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /class-groups?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class ClassGroupCriteria implements Serializable {
    /**
     * Class for filtering SchoolType
     */
    public static class SchoolTypeFilter extends Filter<SchoolType> {
    }

    private static final long serialVersionUID = 1L;


    private LongFilter id;

    private StringFilter classCode;

    private SchoolTypeFilter schoolType;

    private IntegerFilter classLevel;

    public ClassGroupCriteria() {
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getClassCode() {
        return classCode;
    }

    public void setClassCode(StringFilter classCode) {
        this.classCode = classCode;
    }

    public SchoolTypeFilter getSchoolType() {
        return schoolType;
    }

    public void setSchoolType(SchoolTypeFilter schoolType) {
        this.schoolType = schoolType;
    }

    public IntegerFilter getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(IntegerFilter classLevel) {
        this.classLevel = classLevel;
    }

    @Override
    public String toString() {
        return "ClassGroupCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (classCode != null ? "classCode=" + classCode + ", " : "") +
                (schoolType != null ? "schoolType=" + schoolType + ", " : "") +
                (classLevel != null ? "classLevel=" + classLevel + ", " : "") +
            "}";
    }

}
