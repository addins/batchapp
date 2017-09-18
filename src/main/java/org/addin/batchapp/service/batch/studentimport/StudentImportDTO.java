package org.addin.batchapp.service.batch.studentimport;

import org.addin.batchapp.domain.enumeration.Gender;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class StudentImportDTO {
    private String regNum;
    private String firstName;
    private String lastName;
    private Gender gender;

    public String getRegNum() {
        return regNum;
    }

    public void setRegNum(String regNum) {
        this.regNum = regNum;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .append("regNum", regNum)
            .append("firstName", firstName)
            .append("lastName", lastName)
            .append("gender", gender)
            .toString();
    }
}
