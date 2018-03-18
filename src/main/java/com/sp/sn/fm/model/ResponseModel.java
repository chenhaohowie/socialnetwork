package com.sp.sn.fm.model;

import com.fasterxml.jackson.annotation.JsonView;

import java.util.HashSet;
import java.util.Set;

public class ResponseModel {

    @JsonView(Success.class)
    private boolean success = true;

    @JsonView(SuccessListCount.class)
    private Set<String> friends = new HashSet<>();

    @JsonView(SuccessListCount.class)
    private int count = 0;

    @JsonView(SuccessList.class)
    private Set<String> recipients = new HashSet<>();

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Set<String> getFriends() {
        return friends;
    }

    public void setFriends(Set<String> friends) {
        this.friends = friends;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Set<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(Set<String> recipients) {
        this.recipients = recipients;
    }

    public interface Success {}

    public interface SuccessList extends Success {}

    public interface SuccessListCount extends Success {}

}
