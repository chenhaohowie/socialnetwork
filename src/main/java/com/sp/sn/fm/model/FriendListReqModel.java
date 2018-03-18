package com.sp.sn.fm.model;

import javax.validation.ValidationException;
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

    public void validate() {
        if (friends == null || friends.size() != 2) {
            throw new ValidationException("Invalid request input");
        }
        for (String email : friends) {
            if (! com.sp.sn.fm.validator.EmailValidator.validate(email)) {
                throw new ValidationException(("must be a well-formed email address"));
            }
        }
    }
}
