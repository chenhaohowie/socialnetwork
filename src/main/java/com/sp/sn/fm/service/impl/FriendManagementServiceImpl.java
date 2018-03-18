package com.sp.sn.fm.service.impl;

import com.sp.sn.fm.entity.Person;
import com.sp.sn.fm.exception.BusinessException;
import com.sp.sn.fm.exception.NotFoundException;
import com.sp.sn.fm.repository.PersonRepository;
import com.sp.sn.fm.service.FriendManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional
public class FriendManagementServiceImpl implements FriendManagementService {

    @Autowired
    PersonRepository personRepository;

    /**
     * connect the two persons to be friends.
     * Connection cannot be added if one persons blocks the other or vise versa
     * @param email1 first person
     * @param email2 second
     * @return the person who initiates the connection
     */
    @Override
    public Person addFriends(String email1, String email2) {
        Person person = getPerson(email1);
        Person friend = getPerson(email2);

        if (person.getFriends().contains(friend) || person.getInverseFriends().contains(friend)) {
            throw new BusinessException("Friendship already exists");
        }

        if (person.getBlockedPersons().contains(friend) || person.getBlockingPersons().contains(friend)) {
            throw new BusinessException(String.format("%s connection to %s is blocked", email1, email2));
        }

        person.setEmail(email1);
        friend.setEmail(email2);

        person.getFriends().add(friend);

        return personRepository.save(person);
    }

    /**
     * get all the persons that are friends of the given person
     * @param email person
     * @return all the persons that are friends of the given person
     */
    @Override
    public Set<Person> getAllFriends(String email) {
        Person person = getPerson(email);
        Set<Person> friends = new HashSet<>();
        friends.addAll(person.getFriends());
        friends.addAll(person.getInverseFriends());
        if (friends.size() == 0) {
            throw new BusinessException(String.format("%s has no friends", email));
        }
        return friends;
    }

    /**
     * get the common friends of any given two persons
     * @param email1 the first person
     * @param email2 there second person
     * @return the common friends
     */
    @Override
    public Set<Person> getCommonFriends(String email1, String email2) {
        Person person1 = getPerson(email1);
        Person person2 = getPerson(email2);

        Set<Person> commonFriends = new HashSet<>();
        commonFriends.addAll(person1.getFriends());
        commonFriends.addAll(person1.getInverseFriends());

        Set<Person> set = new HashSet<>();
        set.addAll(person2.getFriends());
        set.addAll(person2.getInverseFriends());

        commonFriends.retainAll(set);

        if (commonFriends.isEmpty()) {
            throw new NotFoundException("No common friends found");
        }
        return commonFriends;
    }

    /**
     * Enable a person to subscribe notification of another person
     * @param requestor the subscriber
     * @param target the notifier
     */
    @Override
    public void subscribeNotification(String requestor, String target) {
        Person req = getPerson(requestor);
        Person tar = getPerson(target);

        if (req.getBlockedPersons().contains(tar) || req.getBlockingPersons().contains(tar)) {
            throw new BusinessException(String.format("%s connection to %s is blocked", requestor, target));
        }

        if (tar.getSubscriptors().contains(req)) {
            throw new BusinessException(String.format("%s has already subscribed to the %s", requestor, target));
        }
        tar.getSubscriptors().add(req);
        personRepository.save(tar);
    }

    /**
     * Enable a person to block another person
     * @param requestor the person who initates the request
     * @param target the person who is blocked
     */
    @Override
    public void blockPerson(String requestor, String target) {
        Person req = getPerson(requestor);
        Person tar = getPerson(target);

        if (req.getBlockedPersons().contains(tar)) {
            throw new BusinessException(String.format("%s has already been blocked by %s", target, requestor));
        }
        req.getBlockedPersons().add(tar);
        personRepository.save(req);
    }

    /**
     * get all persons that are eligible for receiving notification of a given person
     * conditions: persons who can receive notification include:
     *             1, friends that they do not block this person or are blocked by this person, or
     *             2, persons that have subscribed to this person if they do not block this person or are blocked by this person
     * @param email the person who notifies
     * @return the eligible persons
     */
    @Override
    public Set<Person> getAllEligiblePersons(String email) {
        Person person = getPerson(email);
        Set<Person> persons = new HashSet<>();
        persons.addAll(person.getFriends());
        persons.addAll(person.getInverseFriends());
        persons.addAll(person.getSubscriptors());
        persons.removeAll(person.getBlockedPersons());
        persons.removeAll(person.getBlockingPersons());

        if(persons.size() == 0) {
            throw new NotFoundException(String.format("No persons subscribed to %s", email));
        }

        return persons;
    }

    private Person getPerson(String email) {
        Person person = personRepository.findByEmail(email);
        if (Objects.isNull(person)) {
            throw new NotFoundException(String.format("Person %s does not exist", email));
        }
        return person;
    }

}
