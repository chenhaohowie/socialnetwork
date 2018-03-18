package com.sp.sn.fm.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.sp.sn.fm.entity.Person;
import com.sp.sn.fm.model.EmailTextReqModel;
import com.sp.sn.fm.model.FriendListReqModel;
import com.sp.sn.fm.model.EmailReqModel;
import com.sp.sn.fm.model.ResponseModel;
import com.sp.sn.fm.model.EmailEmailReqModel;
import com.sp.sn.fm.service.FriendManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/${app.version}")
public class FriendManagementController {

    @Autowired
    FriendManagementService friendManagementService;

    @PutMapping(value = "/friends")
    @JsonView(ResponseModel.Success.class)
    public ResponseModel addFriends(@RequestBody @Validated FriendListReqModel model) {
        friendManagementService.addFriends(model.getFriends().get(0), model.getFriends().get(1));
        return new ResponseModel();
    }

    @PutMapping(value = "/friends/all")
    @JsonView(ResponseModel.SuccessListCount.class)
    public ResponseModel getAllFriends(@RequestBody @Validated EmailReqModel model) {
        Set<Person> friends = friendManagementService.getAllFriends(model.getEmail());
        ResponseModel responseModel = new ResponseModel();
        friends.forEach(f -> responseModel.getFriends().add(f.getEmail()));
        responseModel.setCount(friends.size());
        return responseModel;
    }

    @PutMapping("/friends/common")
    @JsonView(ResponseModel.SuccessListCount.class)
    public ResponseModel getCommonFriends(@RequestBody @Validated FriendListReqModel model) {
        Set<Person> commonFriends  = friendManagementService.getCommonFriends(model.getFriends().get(0), model.getFriends().get(1));
        ResponseModel responseModel = new ResponseModel();
        commonFriends.stream().forEach(f -> responseModel.getFriends().add(f.getEmail()));
        responseModel.setCount(commonFriends.size());
        return responseModel;
    }

    @PutMapping("/person/block")
    @JsonView(ResponseModel.Success.class)
    public ResponseModel blockPerson(@RequestBody @Validated EmailEmailReqModel model) {
        friendManagementService.blockPerson(model.getRequestor(), model.getTarget());
        return new ResponseModel();
    }

    @PutMapping(value = "/subscriptions")
    @JsonView(ResponseModel.Success.class)
    public ResponseModel subscribeNotification(@RequestBody @Validated EmailEmailReqModel model) {
        friendManagementService.subscribeNotification(model.getRequestor(), model.getTarget());
        return new ResponseModel();
    }

    @PutMapping(value = "/subscriptions/eligibility")
    @JsonView(ResponseModel.SuccessList.class)
    public ResponseModel getAllEligiblePersons(@RequestBody @Validated EmailTextReqModel model) {
        Set<Person> friends = friendManagementService.getAllEligiblePersons(model.getSender());
        ResponseModel responseModel = new ResponseModel();
        friends.stream().forEach(f -> responseModel.getRecipients().add(f.getEmail()));
        return responseModel;
    }
}
