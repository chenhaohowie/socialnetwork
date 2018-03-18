package com.sp.sn.fm.model;

import com.sp.sn.fm.validator.EmailValidator;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmailReqModel {

    @NotEmpty
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void validate() {
        if (email == null) {
            throw new ValidationException("Requested email cannot be null");
        }
        if (! EmailValidator.validate(email)) {
            throw new ValidationException(("must be a well-formed email address"));
        }
    }
}
