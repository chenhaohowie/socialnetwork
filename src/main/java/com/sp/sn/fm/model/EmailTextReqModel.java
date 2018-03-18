package com.sp.sn.fm.model;

import com.sp.sn.fm.validator.EmailValidator;

import javax.validation.ValidationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmailTextReqModel {

    @NotEmpty
    @Email
    private String sender;

    @NotEmpty
    private String text;

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void validate() {
        if (sender == null) {
            throw new ValidationException("Requested email cannot be null");
        }
        if (! EmailValidator.validate(sender)) {
            throw new ValidationException(("must be a well-formed email address"));
        }
    }
}
