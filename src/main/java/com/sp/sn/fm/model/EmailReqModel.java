package com.sp.sn.fm.model;

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
}
