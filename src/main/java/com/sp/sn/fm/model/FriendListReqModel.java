package com.sp.sn.fm.model;

import com.sp.sn.fm.exception.BusinessException;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.util.List;

public class FriendListReqModel {

    @Size(min = 2, message = "Incorrect number of person in the request")
    private List<@Email String> friends;

    public List<String> getFriends() {
        return friends;
    }

    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

}
