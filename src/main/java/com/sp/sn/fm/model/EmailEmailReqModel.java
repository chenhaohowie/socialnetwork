package com.sp.sn.fm.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class EmailEmailReqModel {

    @NotEmpty
    @Email
    private String requestor;

    @NotEmpty
    @Email
    private String target;

    public String getRequestor() {
        return requestor;
    }

    public void setRequestor(String requestor) {
        this.requestor = requestor;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }
}
