package com.sp.sn.fm.service;

import com.sp.sn.fm.entity.Person;

import java.util.Set;

public interface FriendManagementService {

    Person addFriends(String email1, String email2);

    Set<Person> getAllFriends(String email);

    Set<Person> getCommonFriends(String email1, String email2);

    void subscribeNotification(String requestor, String target);

    void blockPerson(String requestor, String target);

    Set<Person> getAllEligiblePersons(String email);
}
