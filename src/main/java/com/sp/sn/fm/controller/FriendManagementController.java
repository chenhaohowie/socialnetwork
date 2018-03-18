package com.sp.sn.fm.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sp.sn.fm.entity.Person;
import com.sp.sn.fm.model.EmailTextReqModel;
import com.sp.sn.fm.model.FriendListReqModel;
import com.sp.sn.fm.model.EmailReqModel;
import com.sp.sn.fm.model.ResponseModel;
import com.sp.sn.fm.model.EmailEmailReqModel;
import com.sp.sn.fm.service.FriendManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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

    @GetMapping(value = "/friends/{person:.+}")
    @JsonView(ResponseModel.SuccessListCount.class)
    public ResponseModel getAllFriends(@PathVariable("person") String reqStr) throws IOException {
        reqStr = decode(reqStr);
        ObjectMapper objectMapper = new ObjectMapper();
        EmailReqModel model = objectMapper.readValue(reqStr, EmailReqModel.class);
        model.validate();
        Set<Person> friends = friendManagementService.getAllFriends(model.getEmail());
        ResponseModel responseModel = new ResponseModel();
        friends.forEach(f -> responseModel.getFriends().add(f.getEmail()));
        responseModel.setCount(friends.size());
        return responseModel;
    }

    @GetMapping("/friends/common/{persons:.+}")
    @JsonView(ResponseModel.SuccessListCount.class)
    public ResponseModel getCommonFriends(@PathVariable("persons") String reqStr) throws IOException {
        reqStr = decode(reqStr);
        ObjectMapper objectMapper = new ObjectMapper();
        FriendListReqModel model = objectMapper.readValue(reqStr, FriendListReqModel.class);
        model.validate();
        Set<Person> commonFriends  = friendManagementService.getCommonFriends(model.getFriends().get(0), model.getFriends().get(1));
        ResponseModel responseModel = new ResponseModel();
        commonFriends.stream().forEach(f -> responseModel.getFriends().add(f.getEmail()));
        responseModel.setCount(commonFriends.size());
        return responseModel;
    }

    @PutMapping("/person/blocking")
    @JsonView(ResponseModel.Success.class)
    public ResponseModel blockPerson(@RequestBody @Validated EmailEmailReqModel model) {
        friendManagementService.blockPerson(model.getRequestor(), model.getTarget());
        return new ResponseModel();
    }

    @PutMapping(value = "/person/subscriptions")
    @JsonView(ResponseModel.Success.class)
    public ResponseModel subscribeNotification(@RequestBody @Validated EmailEmailReqModel model) {
        friendManagementService.subscribeNotification(model.getRequestor(), model.getTarget());
        return new ResponseModel();
    }

    @GetMapping(value = "/subscriptions/{person:.+}/eligibilities")
    @JsonView(ResponseModel.SuccessList.class)
    public ResponseModel getAllEligibleSubscriptions(@PathVariable("person") String reqStr) throws IOException {
        reqStr = decode(reqStr);
        ObjectMapper objectMapper = new ObjectMapper();
        EmailTextReqModel model = objectMapper.readValue(reqStr, EmailTextReqModel.class);
        model.validate();
        Set<Person> friends = friendManagementService.getAllEligibleSubscriptions(model.getSender());
        ResponseModel responseModel = new ResponseModel();
        friends.stream().forEach(f -> responseModel.getRecipients().add(f.getEmail()));
        return responseModel;
    }

    private String decode(String uri) {
        if (uri != null && uri.startsWith("%")) {
            try {
                return URLDecoder.decode(uri, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return uri;
    }
}
